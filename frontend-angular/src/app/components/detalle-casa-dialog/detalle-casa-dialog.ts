import { Component, OnInit, inject, Inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CasaDetallDto } from '../../models/casa-detall-dto';
import { CasaRuralService } from '../../services/casa-rural.service';

@Component({
  selector: 'app-detalle-casa-dialog',
  imports: [CommonModule, MatIconModule],
  templateUrl: './detalle-casa-dialog.html',
  styleUrl: './detalle-casa-dialog.css',
})
export class DetalleCasaDialog implements OnInit {
  private casaRuralService = inject(CasaRuralService);
  private cdr = inject(ChangeDetectorRef);

  casa: CasaDetallDto | null = null;
  imagenPlaceholder = 'https://via.placeholder.com/800x400?text=Casa+Rural';
  mensaje: string = '';
  cargando: boolean = true;

  constructor(@Inject(MAT_DIALOG_DATA) public casaId: number) {}

  ngOnInit(): void {
    if (this.casaId) {
      console.log('Obteniendo detalle de casa con ID desde diálogo:', this.casaId);
      this.casaRuralService.obtenerDetalleCasa(this.casaId).subscribe({
        next: (data) => {
          console.log('Detalle de casa obtenido:', data);
          this.casa = data;
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
    } else {
      this.mensaje = 'No se especificó una casa para mostrar.';
      this.cargando = false;
      this.cdr.markForCheck();
    }
  }
}
