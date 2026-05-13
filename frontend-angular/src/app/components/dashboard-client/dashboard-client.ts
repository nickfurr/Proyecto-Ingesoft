import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { Navbar } from '../navbar/navbar';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-dashboard-client',
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './dashboard-client.html',
  styleUrl: './dashboard-client.css'
})

export class DashboardClient implements OnInit {
  private platformId = inject(PLATFORM_ID);
  private router = inject(Router);
  private dashboardService = inject(DashboardService);
  private cdr = inject(ChangeDetectorRef);

  nombreAcronimo = '';
  nombreUsuario = '';
  usernameUsuario = '';
  telefonoUsuario = '';
  activeUsuario = false;
  usuarioId = 0;

  activePanel: 'info' | 'buscarUsuario' = 'info';

  panelTitle = 'Mi información';
  panelSub = 'Datos personales y cuenta';

  panelMeta = {
    info: { title: 'Mi información', sub: 'Datos personales y cuenta' },
    buscarUsuario: { title: 'Buscar usuario', sub: 'Consulta por username' }
  };

  // — Búsqueda de usuarios —
  searchUsername = '';
  searchType: 'cliente' | 'propietario' = 'cliente';
  searchResult: any = null;
  searchError = '';
  searchLoading = false;
  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.usuarioId = Number(localStorage.getItem('usuarioId') ?? '0');
      this.usernameUsuario = localStorage.getItem('usuario-username') ?? '';
      this.nombreUsuario = localStorage.getItem('nombreUsuario') ?? '';
      this.telefonoUsuario = localStorage.getItem('usuario-telefono') ?? '';
      this.activeUsuario = localStorage.getItem('usuario-activo') === 'true';
      this.nombreAcronimo = this.nombreUsuario.slice(0, 2).toUpperCase();
    }
  }

  showPanel(id: 'info' | 'buscarUsuario'): void {
    this.activePanel = id;
    this.panelTitle = this.panelMeta[id].title;
    this.panelSub = this.panelMeta[id].sub;
  }

  volverLogin(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.clear();
    }
    this.router.navigate(['/']);
  }

  buscarUsuario(): void {
    if (!this.searchUsername.trim()) return;
    this.searchLoading = true;
    this.searchResult = null;
    this.searchError = '';

    const obs = this.searchType === 'cliente'
      ? this.dashboardService.buscarClientePorUsername(this.searchUsername.trim())
      : this.dashboardService.buscarPropietarioPorUsername(this.searchUsername.trim());

    obs.subscribe({
      next: (data) => {
        this.searchResult = data;
        this.searchLoading = false;
        console.log("llego el cabron encontrado xD");
        this.cdr.detectChanges();
      },
      error: () => {
        this.searchError = this.searchType === 'cliente'
          ? 'Cliente no encontrado'
          : 'Propietario no encontrado';
        this.searchLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  limpiarBusqueda(): void {
    this.searchUsername = '';
    this.searchResult = null;
    this.searchError = '';
  }
}
