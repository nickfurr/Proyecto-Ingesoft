import { Component, OnInit, inject, Inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CasaDetallDto } from '../../models/casa-detall-dto';
import { CasaRuralService } from '../../services/casa-rural.service';
import { DashboardService } from '../../services/dashboard.service';
import { CrearReservaDto } from '../../models/crear-reserva-dto';
import { ReservaDto } from '../../models/reserva-dto';

@Component({
  selector: 'app-detalle-casa-dialog',
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './detalle-casa-dialog.html',
  styleUrl: './detalle-casa-dialog.css',
})
export class DetalleCasaDialog implements OnInit {
  private casaRuralService = inject(CasaRuralService);
  private dashboardService = inject(DashboardService);
  private cdr = inject(ChangeDetectorRef);
  private readonly backendBaseUrl = 'http://localhost:8080';

  casa: CasaDetallDto | null = null;
  fotos: string[] = [];
  mensaje = '';
  cargando = true;
  clienteId = 0;
  clienteUsername = '';
  reservaEnviando = false;
  reservaError = '';
  reservaExito = '';
  reservaCreada: ReservaDto | null = null;
  reservaForm: CrearReservaDto = {
    clienteId: 0,
    casaRuralId: 0,
    fechaEntrada: '',
    fechaSalida: '',
    modalidad: 'CASA_ENTERA'
  };

  constructor(@Inject(MAT_DIALOG_DATA) public casaId: number) {}

  get tieneImagenes(): boolean {
    return this.fotos.length > 0;
  }

  get imagenes(): string[] {
    return this.fotos;
  }

  get puedeReservar(): boolean {
    return !!this.casa && this.clienteId > 0 && !this.reservaEnviando;
  }

  get nochesEstimadas(): number {
    if (!this.reservaForm.fechaEntrada || !this.reservaForm.fechaSalida) {
      return 0;
    }

    const entrada = new Date(this.reservaForm.fechaEntrada);
    const salida = new Date(this.reservaForm.fechaSalida);

    if (Number.isNaN(entrada.getTime()) || Number.isNaN(salida.getTime())) {
      return 0;
    }

    return Math.max(0, Math.ceil((salida.getTime() - entrada.getTime()) / (1000 * 60 * 60 * 24)));
  }

  private normalizarFotoUrl(url: string): string {
    const valor = (url || '').trim();

    if (!valor) {
      return '';
    }

    if (valor.startsWith('http://') || valor.startsWith('https://') || valor.startsWith('data:image/')) {
      return valor;
    }

    if (valor.startsWith('//')) {
      return `https:${valor}`;
    }

    if (valor.startsWith('www.')) {
      return `https://${valor}`;
    }

    if (valor.startsWith('/')) {
      return `${this.backendBaseUrl}${valor}`;
    }

    return `${this.backendBaseUrl}/${valor}`;
  }

  ngOnInit(): void {
    if (typeof window !== 'undefined') {
      this.clienteId = Number(localStorage.getItem('usuarioId') ?? '0');
      this.clienteUsername = localStorage.getItem('usuario-username') ?? '';
    }

    if (!this.casaId) {
      this.mensaje = 'No se especificó una casa para mostrar.';
      this.cargando = false;
      this.cdr.markForCheck();
      return;
    }

    this.casaRuralService.obtenerDetalleCasa(this.casaId).subscribe({
      next: (data) => {
        this.casa = data;
        this.fotos = Array.from(
          new Set((data.fotos || [])
            .map((foto) => this.normalizarFotoUrl(foto))
            .filter((foto) => foto.length > 0))
        );
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        console.error('Error al obtener detalles de la casa:', error);
        this.mensaje = 'No se pudo cargar la información de la casa. Por favor, intenta de nuevo.';
        this.cargando = false;
        this.cdr.markForCheck();
      }
    });
  }

  reservarCasa(): void {
    this.reservaError = '';
    this.reservaExito = '';
    this.reservaCreada = null;

    if (!this.casa) {
      this.reservaError = 'No se encontró la casa seleccionada.';
      return;
    }

    if (this.clienteId <= 0) {
      this.reservaError = 'Debes iniciar sesión para reservar una casa.';
      return;
    }

    if (!this.reservaForm.fechaEntrada || !this.reservaForm.fechaSalida) {
      this.reservaError = 'Debes seleccionar la fecha de entrada y salida.';
      return;
    }

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    const fechaEntrada = new Date(this.reservaForm.fechaEntrada);

    if (Number.isNaN(fechaEntrada.getTime()) || fechaEntrada < hoy) {
      this.reservaError = 'La fecha de entrada no puede ser anterior a hoy.';
      return;
    }

    if (this.nochesEstimadas <= 0) {
      this.reservaError = 'La fecha de salida debe ser posterior a la de entrada.';
      return;
    }

    this.reservaEnviando = true;
    this.reservaForm.clienteId = this.clienteId;
    this.reservaForm.casaRuralId = this.casa.codigo;

    this.dashboardService.registrarReserva(this.reservaForm).subscribe({
      next: (reserva) => {
        this.reservaCreada = reserva;
        this.reservaExito = `Reserva creada correctamente. N° ${reserva.numeroReserva} en estado ${reserva.estadoPago}.`;
        this.reservaEnviando = false;
        this.cdr.markForCheck();
      },
      error: (error) => {
        this.reservaEnviando = false;
        this.reservaError = error?.error?.detail || error?.error?.message || 'No se pudo registrar la reserva.';
        this.cdr.markForCheck();
      }
    });
  }
}