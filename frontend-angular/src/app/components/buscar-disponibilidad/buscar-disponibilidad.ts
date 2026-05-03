import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-buscar-disponibilidad',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './buscar-disponibilidad.html',
  styleUrl: './buscar-disponibilidad.css'
})
export class BuscarDisponibilidad {
  @Input() loading = false;
  @Output() buscar = new EventEmitter<{ fechaEntrada: string; fechaSalida: string }>();
  @Output() limpiar = new EventEmitter<void>();

  fechaEntrada = '';
  fechaSalida = '';
  errorMsg = '';

  onBuscar(): void {
    this.errorMsg = '';

    if (!this.fechaEntrada || !this.fechaSalida) {
      this.errorMsg = 'Debes seleccionar ambas fechas.';
      return;
    }

    const entrada = new Date(this.fechaEntrada);
    const salida = new Date(this.fechaSalida);

    if (Number.isNaN(entrada.getTime()) || Number.isNaN(salida.getTime())) {
      this.errorMsg = 'Las fechas no son validas.';
      return;
    }

    if (salida <= entrada) {
      this.errorMsg = 'La fecha de salida debe ser posterior a la entrada.';
      return;
    }

    this.buscar.emit({
      fechaEntrada: this.fechaEntrada,
      fechaSalida: this.fechaSalida
    });
  }

  onLimpiar(): void {
    this.fechaEntrada = '';
    this.fechaSalida = '';
    this.errorMsg = '';
    this.limpiar.emit();
  }
}
