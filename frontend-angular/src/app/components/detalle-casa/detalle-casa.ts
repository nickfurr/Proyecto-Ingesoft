import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { CasaRuralDto } from '../../models/casa-rural-dto';

@Component({
  selector: 'app-detalle-casa',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detalle-casa.html',
  styleUrl: './detalle-casa.css'
})
export class DetalleCasa implements OnInit {
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);

  casa: CasaRuralDto | null = null;
  mensaje = '';

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      this.mensaje = 'No fue posible cargar la casa.';
      return;
    }

    const data = localStorage.getItem('casaDetalleTemporal');

    if (!data) {
      this.mensaje = 'No se encontró la información de la casa.';
      return;
    }

    try {
      this.casa = JSON.parse(data);
      this.mensaje = 'Casa encontrada';
    } catch (error) {
      console.error('Error leyendo casa temporal', error);
      this.mensaje = 'Ocurrió un error al cargar la casa.';
    }
  }

  volverDashboard(): void {
    this.router.navigate(['/dashboard-propietario']);
  }
}
