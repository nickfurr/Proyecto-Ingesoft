import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { BuscarDisponibilidad } from '../buscar-disponibilidad/buscar-disponibilidad';
import { DashboardService } from '../../services/dashboard.service';
import { CasaRuralService } from '../../services/casa-rural.service';
import { CasaRuralDto } from '../../models/casa-rural-dto';
import { DetalleCasaDialog } from '../detalle-casa-dialog/detalle-casa-dialog';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, BuscarDisponibilidad, MatDialogModule, MatButtonModule],
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class Landing {
  private dashboardService = inject(DashboardService);
  private casaRuralService = inject(CasaRuralService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  private dialog = inject(MatDialog);

  casas: CasaRuralDto[] = [];
  loading = false;
  errorMsg = '';
  busquedaActiva = false;

  onBuscar(params: {
    fechaEntrada: string;
    fechaSalida: string;
    ciudad?: string;
    precioMin?: number;
    precioMax?: number;
  }): void {
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

          let casasFiltradas = data ?? [];

          // Filtrar por ciudad
          if (params.ciudad && params.ciudad.trim() !== '') {

            casasFiltradas = casasFiltradas.filter(casa =>
              casa.ciudad?.toLowerCase()
                .includes(params.ciudad!.toLowerCase())
            );

          }
          // Filtrar por precio mínimo
          if (params.precioMin != null) {

            casasFiltradas = casasFiltradas.filter(casa =>
              casa.precio >= params.precioMin!
            );

          }
          // Filtrar por precio máximo
          if (params.precioMax != null) {

            casasFiltradas = casasFiltradas.filter(casa =>
              casa.precio <= params.precioMax!
            );

          }

          this.casas = casasFiltradas;

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
    console.log('Abriendo detalle para casa:', codigo);
    this.casaRuralService.obtenerDetalleCasa(codigo).subscribe({
      next: (casaDetall) => {
        console.log('Detalles obtenidos:', casaDetall);
        this.dialog.open(DetalleCasaDialog, {
          width: '90%',
          maxWidth: '10000px',
          height: 'auto',
          maxHeight: '90vh',
          data: codigo,
          panelClass: 'casa-detall-dialog'
        });
      },
      error: (error) => {
        console.error('Error al obtener detalles de la casa:', error);
        alert('No se pudo cargar los detalles de la casa. Intenta de nuevo.');
      }
    });
  }
}
