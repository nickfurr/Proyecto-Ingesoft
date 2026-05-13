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
  private readonly backendBaseUrl = 'http://localhost:8080';

  casa: CasaDetallDto | null = null;
  fotos: string[] = [];
  mensaje: string = '';
  cargando: boolean = true;

  constructor(@Inject(MAT_DIALOG_DATA) public casaId: number) {}

  get tieneImagenes(): boolean {
    return this.fotos && this.fotos.length > 0;
  }

  get imagenes(): string[] {
    return this.fotos || [];
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
    if (this.casaId) {
      console.log('Obteniendo detalle de casa con ID desde diálogo:', this.casaId);
      this.casaRuralService.obtenerDetalleCasa(this.casaId).subscribe({
        next: (data) => {
          console.log('Detalle de casa obtenido:', data);
          this.casa = data;
          this.fotos = Array.from(
            new Set((data.fotos || [])
              .map((foto) => this.normalizarFotoUrl(foto))
              .filter((foto) => foto.length > 0))
          );
          this.cargando = false;
          this.cdr.markForCheck();
          console.log('Fotos cargadas:', this.fotos);
          console.log('Total de fotos:', this.fotos.length);
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
