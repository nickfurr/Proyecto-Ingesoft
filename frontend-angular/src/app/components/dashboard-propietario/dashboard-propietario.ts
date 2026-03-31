import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard-propietario',
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-propietario.html',
  styleUrl: './dashboard-propietario.css'
})
export class DashboardPropietario {
  activePanel: 'info' | 'casas' | 'historico' = 'info';

  panelTitle = 'Mi información';
  panelSub = 'Datos personales y cuenta';

  panelMeta = {
    info: { title: 'Mi información', sub: 'Datos personales y cuenta' },
    casas: { title: 'Mis casas registradas', sub: 'Lista de propiedades activas' },
    historico: { title: 'Histórico de paquetes', sub: 'Consulta y filtra todos tus paquetes' }
  };

  fechaDesde = '2024-01-01';
  fechaHasta = '2025-03-30';
  modalidad = '';

  historico = [
    { id: '#PKT-001', inicio: '2024-01-10', fin: '2024-03-31', modalidad: 'Casa entera', casaEntera: '$850.000', porHabitacion: '—', vigente: 'No' },
    { id: '#PKT-002', inicio: '2024-04-01', fin: '2024-06-30', modalidad: 'Por habitaciones', casaEntera: '—', porHabitacion: '$180.000', vigente: 'No' },
    { id: '#PKT-003', inicio: '2024-07-01', fin: '2024-09-30', modalidad: 'Casa entera', casaEntera: '$920.000', porHabitacion: '—', vigente: 'No' },
    { id: '#PKT-004', inicio: '2024-10-01', fin: '2025-03-31', modalidad: 'Ambas', casaEntera: '$780.000', porHabitacion: '$160.000', vigente: 'Sí' },
    { id: '#PKT-005', inicio: '2025-01-15', fin: '2025-06-15', modalidad: 'Por habitaciones', casaEntera: '—', porHabitacion: '$200.000', vigente: 'Sí' },
    { id: '#PKT-006', inicio: '2025-02-01', fin: '2025-08-01', modalidad: 'Casa entera', casaEntera: '$1.050.000', porHabitacion: '—', vigente: 'Sí' }
  ];

  historicoFiltrado = [...this.historico];

  showPanel(id: 'info' | 'casas' | 'historico') {
    this.activePanel = id;
    this.panelTitle = this.panelMeta[id].title;
    this.panelSub = this.panelMeta[id].sub;
  }

  filtrarHistorico() {
    this.historicoFiltrado = this.historico.filter(item => {
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
    return this.historicoFiltrado.filter(item => item.vigente === 'Sí').length;
  }
}
