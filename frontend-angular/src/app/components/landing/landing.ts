import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { BuscarDisponibilidad } from '../buscar-disponibilidad/buscar-disponibilidad';
import { DashboardService } from '../../services/dashboard.service';
import { CasaRuralDto } from '../../models/casa-rural-dto';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, BuscarDisponibilidad],
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class Landing {
  private dashboardService = inject(DashboardService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  casas: CasaRuralDto[] = [];
  loading = false;
  errorMsg = '';
  busquedaActiva = false;

  onBuscar(params: { fechaEntrada: string; fechaSalida: string }): void {
    this.errorMsg = '';
    this.loading = true;
    this.busquedaActiva = true;

    this.dashboardService.buscarCasasDisponibles(params.fechaEntrada, params.fechaSalida)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (data) => {
          console.log('Casas disponibles:', data);
          this.casas = data ?? [];
        },
        error: () => {
          this.errorMsg = 'No se pudo cargar la disponibilidad. Intenta de nuevo.';
          this.casas = [];
        }
      });
  }

  onLimpiar(): void {
    this.casas = [];
    this.errorMsg = '';
    this.busquedaActiva = false;
  }

  verDetalle(codigo: number): void {
    this.router.navigate(['/detalle-casa', codigo]);
  }
}
