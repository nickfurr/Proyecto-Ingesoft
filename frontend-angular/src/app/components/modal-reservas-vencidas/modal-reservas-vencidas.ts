import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaDto } from '../../models/reserva-dto';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-modal-reservas-vencidas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal-reservas-vencidas.html',
  styleUrl: './modal-reservas-vencidas.css'
})
export class ModalReservasVencidas {
  private dashboardService = inject(DashboardService);

  @Input() mostrar = false;
  @Input() reservasVencidas: ReservaDto[] = [];
  @Output() cerrar = new EventEmitter<void>();
  @Output() accionReserva = new EventEmitter<{ numeroReserva: number; accion: 'cancelar' | 'mantener' }>();

  procesando: Set<number> = new Set();
  errorMsg = '';
  exitoMsg = '';

  cerrarModal(): void {
    this.mostrar = false;
    this.cerrar.emit();
  }

  procesarAccion(numeroReserva: number, accion: 'cancelar' | 'mantener'): void {
    this.procesando.add(numeroReserva);
    this.errorMsg = '';
    this.exitoMsg = '';

    // Emitir la acción
    this.accionReserva.emit({ numeroReserva, accion });

    // Limpiar estado después de 2 segundos
    setTimeout(() => {
      this.procesando.delete(numeroReserva);
    }, 2000);
  }

  cancelarReserva(numeroReserva: number): void {
    this.procesarAccion(numeroReserva, 'cancelar');
  }

  mantenerReserva(numeroReserva: number): void {
    this.procesarAccion(numeroReserva, 'mantener');
  }

  get noHayReservas(): boolean {
    return !this.reservasVencidas || this.reservasVencidas.length === 0;
  }
}
