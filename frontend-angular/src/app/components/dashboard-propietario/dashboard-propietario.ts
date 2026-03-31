import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { CasaRuralDto } from '../../models/casa-rural-dto';
import { ReservaDto } from '../../models/reserva-dto';
import { PaqueteAlquilerDto } from '../../models/paquete-alquiler-dto';

@Component({
  selector: 'app-dashboard-propietario',
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-propietario.html',
  styleUrl: './dashboard-propietario.css'
})
export class DashboardPropietario implements OnInit {
  private platformId = inject(PLATFORM_ID);
  private router = inject(Router);
  private dashboardService = inject(DashboardService);

  activePanel: 'info' | 'casas' | 'historico' = 'info';

  panelTitle = 'Mi información';
  panelSub = 'Datos personales y cuenta';

  panelMeta = {
    info: { title: 'Mi información', sub: 'Datos personales y cuenta' },
    casas: { title: 'Mis casas registradas', sub: 'Lista de propiedades activas' },
    historico: { title: 'Histórico de paquetes', sub: 'Consulta y filtra todos tus paquetes' }
  };

  nombreAcronimo = '';
  nombrePropietario = '';
  usernamePropietario = '';
  telefonoPropietario = '';
  cuentaBancariaPropietario = '';
  activoPropietario = false;
  propietarioId = 0;

  casasRegistradas = 0;
  paquetesActivos = 0;
  reservasTotales = 0;

  casas: CasaRuralDto[] = [];
  reservas: ReservaDto[] = [];
  paquetesActivosLista: PaqueteAlquilerDto[] = [];

  fechaDesde = '2024-01-01';
  fechaHasta = '2025-03-30';
  modalidad = '';

  historico = [];
  historicoFiltrado = [...this.historico];

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.propietarioId = Number(localStorage.getItem('propietarioId') ?? '0');
      this.usernamePropietario = localStorage.getItem('propietario-username') ?? '';
      this.nombrePropietario = localStorage.getItem('nombrePropietario') ?? '';
      this.telefonoPropietario = localStorage.getItem('propietario-telefono') ?? '';
      this.cuentaBancariaPropietario = localStorage.getItem('propietario-cuentaBancaria') ?? '';
      this.activoPropietario = localStorage.getItem('propietario-activo') === 'true';
      this.nombreAcronimo = this.nombrePropietario.slice(0,2).toUpperCase();

        this.cargarInfoPropietario();
    }
  }

  cargarInfoPropietario(): void {
    if (this.propietarioId <= 0) return;

    this.dashboardService.obtenerCasasPorPropietario(this.propietarioId).subscribe({
      next: (res) => {
        this.casas = res;
        this.casasRegistradas = res.length;
      },
      error: (err) => console.error('Error cargando casas', err)
    });

    this.dashboardService.obtenerPaquetesActivosPorPropietario(this.propietarioId).subscribe({
      next: (res) => {
        this.paquetesActivosLista = res;
        this.paquetesActivos = res.length;
      },
      error: (err) => console.error('Error cargando paquetes activos', err)
    });

    this.dashboardService.obtenerReservasPorPropietario(this.propietarioId).subscribe({
      next: (res) => {
        this.reservas = res;
        this.reservasTotales = res.length;
      },
      error: (err) => console.error('Error cargando reservas', err)
    });
  }

  showPanel(id: 'info' | 'casas' | 'historico') {
    this.activePanel = id;
    this.panelTitle = this.panelMeta[id].title;
    this.panelSub = this.panelMeta[id].sub;

    if (id === 'info') {
      this.cargarInfoPropietario();
    }
  }

  volverLogin(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.clear();
    }
    this.router.navigate(['/']);
  }

  filtrarHistorico() {
    this.historicoFiltrado = this.historico.filter((item: any) => {
      const inRange =
        (!this.fechaDesde || item.inicio >= this.fechaDesde) &&
        (!this.fechaHasta || item.fin <= this.fechaHasta);

      const inMod =
        !this.modalidad ||
        item.modalidad.toLowerCase().includes(this.modalidad.toLowerCase());

      return inRange && inMod;
    });
  }

  get totalPaquetes(): number {
    return this.historicoFiltrado.length;
  }

  get vigentes(): number {
    return this.historicoFiltrado.filter((item: any) => item.vigente === 'Sí').length;
  }
}
