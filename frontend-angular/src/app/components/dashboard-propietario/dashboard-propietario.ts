import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { CasaRuralDto } from '../../models/casa-rural-dto';
import { ReservaDto } from '../../models/reserva-dto';
import { PaqueteAlquilerDto } from '../../models/paquete-alquiler-dto';
import { HistoricoPaqueteDto } from '../../models/historico-paquete-dto';
import { ModalReservasVencidas } from '../modal-reservas-vencidas/modal-reservas-vencidas';
import { ChangeDetectorRef } from '@angular/core';

interface HabitacionForm {
  numeroCamas: number;
  tipoCama: string;
  tieneBano: boolean;
}

interface BanoForm {
  compartido: boolean;
}

interface CocinaForm {
  tieneLavadora: boolean;
  tieneLavavajillas: boolean;
}

interface CasaForm {
  poblacion: string;
  descripcionGeneral: string;
  numeroComedores: number;
  plazasGaraje: number;
  propietarioId: number;
  fotos: string[];

  habitaciones: HabitacionForm[];
  banos: BanoForm[];
  cocinas: CocinaForm[];
}

@Component({
  selector: 'app-dashboard-propietario',
  imports: [CommonModule, FormsModule, ModalReservasVencidas],
  templateUrl: './dashboard-propietario.html',
  styleUrl: './dashboard-propietario.css'
})

export class DashboardPropietario implements OnInit {
  private platformId = inject(PLATFORM_ID);
  private router = inject(Router);
  private dashboardService = inject(DashboardService);
  private cdr = inject(ChangeDetectorRef);

  registrandoPagoReservaId: number | null = null;
  errorReserva = '';
  exitoReserva = '';
  mostrarModalVencidas = false;

  activePanel: 'reservas' | 'crearPaquete' | 'crearCasa' | 'info' | 'casas' | 'historico' | 'buscarUsuario' = 'info';

  panelTitle = 'Mi información';
  panelSub = 'Datos personales y cuenta';

  // — Búsqueda de usuarios —
  searchUsername = '';
  searchType: 'cliente' | 'propietario' = 'cliente';
  searchResult: any = null;
  searchError = '';
  searchLoading = false;

  panelMeta = {
    info: { title: 'Mi información', sub: 'Datos personales y cuenta' },
    buscarUsuario: { title: 'Buscar usuario', sub: 'Consulta por username' },
    casas: { title: 'Mis casas registradas', sub: 'Lista de propiedades activas' },
    historico: { title: 'Histórico de paquetes', sub: 'Consulta y filtra todos tus paquetes' },
    crearCasa: { title: 'Crear una nueva casa', sub: 'Llena cada campo para crear tu casa' },
    crearPaquete: { title: 'Crear un nuevo paquete', sub: 'Llena cada campo para crear un nuevo paquete' },
    reservas: { title: 'Reserva', sub: 'Lista de reservas activas' }
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
  reservaTemporal: ReservaDto[] | null = [];
  paquetesActivosLista: PaqueteAlquilerDto[] = [];

  fechaDesde = '2024-01-01';
  fechaHasta = '2025-03-30';
  modalidad = '';

  historico: HistoricoPaqueteDto[] = [];
  historicoFiltrado: HistoricoPaqueteDto[] = [];

  mostrarFormularioPaquete = false;
  modoEdicionPaquete = false;
  paqueteEditandoId: number | null = null;
  guardandoPaquete = false;
  errorPaquete = '';
  exitoPaquete = '';

  codigoBusquedaCasa = '';
  errorBusquedaCasa = '';

  fotosInputs: string[] = [''];

  paqueteForm: PaqueteAlquilerDto = this.crearFormularioVacio();

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.propietarioId = Number(localStorage.getItem('propietarioId') ?? '0');
      this.usernamePropietario = localStorage.getItem('propietario-username') ?? '';
      this.nombrePropietario = localStorage.getItem('nombrePropietario') ?? '';
      this.telefonoPropietario = localStorage.getItem('propietario-telefono') ?? '';
      this.cuentaBancariaPropietario = localStorage.getItem('propietario-cuentaBancaria') ?? '';
      this.activoPropietario = localStorage.getItem('propietario-activo') === 'true';
      this.nombreAcronimo = this.nombrePropietario.slice(0, 2).toUpperCase();

      this.cargarInfoPropietario();
      const panel = this.router.parseUrl(this.router.url).queryParams['panel'];
      if (panel === 'casas') {
        this.showPanel('casas');
      }
    }
  }

  crearFormularioVacio(): PaqueteAlquilerDto {
    return {
      casaRuralId: 0,
      fechaInicio: '',
      fechaFin: '',
      modalidad: '',
      precioCasaEntera: null,
      precioPorHabitacion: null
    };
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
        this.buscarReservasVencidas(); // Revisa si hay reservas vencidas al cargar la información del propietario
      },
      error: (err) => console.error('Error cargando reservas', err)
    });
  }

  // verifica reservas con el estado vendido y las guarda en reservaTemporal
  buscarReservasVencidas(): void {
    this.reservaTemporal = [];

    // Recorre todas las reservas y filtra las que están vencidas
    this.reservas.forEach(reserva => {
      if (reserva.estadoPago === 'VENCIDA') {
        this.reservaTemporal!.push(reserva);
      }
    });

    // Mostrar modal si hay reservas vencidas
    if (this.reservaTemporal && this.reservaTemporal.length > 0) {
      this.mostrarModalVencidas = true;
    }
  }

  cerrarModalReservasVencidas(): void {
    this.mostrarModalVencidas = false;
  }

  procesarAccionReservaVencida(evento: { numeroReserva: number; accion: 'cancelar' | 'mantener' }): void {
    const { numeroReserva, accion } = evento;

    // Aquí puedes agregar lógica para cancelar o mantener la reserva
    if (accion === 'cancelar') {
      console.log('Cancelando reserva:', numeroReserva);
      // Llamar al servicio para cancelar
      //informacion dto de la reserva a cancelar
      const reservaACancelar = this.reservas.find(r => r.numeroReserva === numeroReserva);
      if (reservaACancelar) {
        reservaACancelar.estadoPago = 'CANCELADA';
        this.dashboardService.actualizarReservaEstado(reservaACancelar).subscribe({
          next: (reservaActualizada) => {
            this.reservas = this.reservas.map(item => item.numeroReserva === reservaActualizada.numeroReserva ? reservaActualizada : item);
            console.log('Reserva cancelada:', reservaACancelar);
          }
        });
      }
    } else if (accion === 'mantener') {
      console.log('Manteniendo reserva:', numeroReserva);
      // Llamar al servicio para mantener/resolver

      // informacion dto de la reserva a mantener
      const reservaAMantener = this.reservas.find(r => r.numeroReserva === numeroReserva);
      if (reservaAMantener) {
        reservaAMantener.estadoPago = 'PENDIENTE_PAGO';
        this.dashboardService.actualizarReservaEstado(reservaAMantener).subscribe({
          next: (reservaActualizada) => {
            this.reservas = this.reservas.map(item => item.numeroReserva === reservaActualizada.numeroReserva ? reservaActualizada : item);
            console.log('Reserva actualizada:', reservaActualizada);
          }
        });
        console.log('Reserva mantenida:', reservaAMantener);
      }
    }

    // Remover de la lista temporal
    this.reservaTemporal = this.reservaTemporal?.filter(
      r => r.numeroReserva !== numeroReserva
    ) || [];

    // Cerrar modal si no hay más reservas
    if (!this.reservaTemporal || this.reservaTemporal.length === 0) {
      this.mostrarModalVencidas = false;
    }
  }

  showPanel(id: 'reservas' | 'info' | 'casas' | 'historico' | 'crearPaquete' | 'crearCasa' | 'buscarUsuario'): void {
    this.activePanel = id;
    this.panelTitle = this.panelMeta[id].title;
    this.panelSub = this.panelMeta[id].sub;

    if (id === 'info') {
      this.cargarInfoPropietario();
    }

    if (id === 'historico') {
      this.cargarHistorico();
      this.cerrarFormularioPaquete();
    }

    if (id === 'crearPaquete') {
      this.abrirFormularioCrear();
    }
  }

  volverLogin(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.clear();
    }
    this.router.navigate(['/']);
  }

  filtrarHistorico(): void {
    if (this.propietarioId <= 0) return;

    this.dashboardService
      .obtenerHistoricoPaquetes(this.propietarioId, this.fechaDesde, this.fechaHasta)
      .subscribe({
        next: (res) => {
          this.historico = res;
          this.historicoFiltrado = res.filter(item =>
            !this.modalidad || item.modalidad === this.modalidad
          );
        },
        error: (err) => console.error('Error cargando histórico', err)
      });
  }

  cargarHistorico(): void {
    if (this.propietarioId <= 0) return;

    this.dashboardService
      .obtenerHistoricoPaquetes(this.propietarioId, this.fechaDesde, this.fechaHasta)
      .subscribe({
        next: (res) => {
          this.historico = res;
          this.historicoFiltrado = !this.modalidad
            ? res
            : res.filter(item => item.modalidad === this.modalidad);
        },
        error: (err) => console.error('Error cargando histórico', err)
      });
  }

  abrirFormularioCrear(): void {
    this.modoEdicionPaquete = false;
    this.paqueteEditandoId = null;
    this.paqueteForm = this.crearFormularioVacio();
    this.mostrarFormularioPaquete = true;
    this.errorPaquete = '';
    this.exitoPaquete = '';
  }

  editarPaquete(item: HistoricoPaqueteDto): void {
    this.activePanel = 'historico';
    this.modoEdicionPaquete = true;
    this.paqueteEditandoId = item.idPaquete;
    this.mostrarFormularioPaquete = true;
    this.errorPaquete = '';
    this.exitoPaquete = '';

    this.paqueteForm = {
      casaRuralId: item.casaRuralId,
      fechaInicio: item.fechaInicio,
      fechaFin: item.fechaFin,
      modalidad: item.modalidad,
      precioCasaEntera: item.precioCasaEntera ?? null,
      precioPorHabitacion: item.precioPorHabitacion ?? null
    };
  }

  cerrarFormularioPaquete(): void {
    this.mostrarFormularioPaquete = false;
    this.modoEdicionPaquete = false;
    this.paqueteEditandoId = null;
    this.paqueteForm = this.crearFormularioVacio();
    this.errorPaquete = '';
    this.exitoPaquete = '';
  }

  validarFormularioPaquete(): string {
    if (!this.paqueteForm.casaRuralId || this.paqueteForm.casaRuralId <= 0) {
      return 'Debes seleccionar una casa.';
    }

    if (!this.paqueteForm.fechaInicio || !this.paqueteForm.fechaFin) {
      return 'Debes ingresar fecha de inicio y fecha de fin.';
    }

    if (this.paqueteForm.fechaInicio > this.paqueteForm.fechaFin) {
      return 'La fecha de inicio no puede ser mayor a la fecha de fin.';
    }

    if (!this.paqueteForm.modalidad) {
      return 'Debes seleccionar una modalidad.';
    }

    if (
      (this.paqueteForm.modalidad === 'CASA_ENTERA' || this.paqueteForm.modalidad === 'AMBAS') &&
      (!this.paqueteForm.precioCasaEntera || this.paqueteForm.precioCasaEntera <= 0)
    ) {
      return 'Debes ingresar un precio válido para casa entera.';
    }

    if (
      (this.paqueteForm.modalidad === 'POR_HABITACIONES' || this.paqueteForm.modalidad === 'AMBAS') &&
      (!this.paqueteForm.precioPorHabitacion || this.paqueteForm.precioPorHabitacion <= 0)
    ) {
      return 'Debes ingresar un precio válido por habitación.';
    }

    return '';
  }

  onModalidadChange(): void {
    if (this.paqueteForm.modalidad === 'CASA_ENTERA') {
      this.paqueteForm.precioPorHabitacion = null;
    }

    if (this.paqueteForm.modalidad === 'POR_HABITACIONES') {
      this.paqueteForm.precioCasaEntera = null;
    }
  }

  guardarPaquete(): void {
    this.errorPaquete = '';
    this.exitoPaquete = '';

    const errorValidacion = this.validarFormularioPaquete();
    if (errorValidacion) {
      this.errorPaquete = errorValidacion;
      return;
    }

    this.guardandoPaquete = true;

    const payload: PaqueteAlquilerDto = {
      casaRuralId: Number(this.paqueteForm.casaRuralId),
      fechaInicio: this.paqueteForm.fechaInicio,
      fechaFin: this.paqueteForm.fechaFin,
      modalidad: this.paqueteForm.modalidad,
      precioCasaEntera:
        this.paqueteForm.modalidad === 'POR_HABITACIONES'
          ? null
          : this.paqueteForm.precioCasaEntera,
      precioPorHabitacion:
        this.paqueteForm.modalidad === 'CASA_ENTERA'
          ? null
          : this.paqueteForm.precioPorHabitacion
    };

    const request = this.modoEdicionPaquete && this.paqueteEditandoId
      ? this.dashboardService.modificarPaquete(this.paqueteEditandoId, this.propietarioId, payload)
      : this.dashboardService.crearPaquete(this.propietarioId, payload);

    request.subscribe({
      next: () => {
        this.guardandoPaquete = false;
        this.exitoPaquete = this.modoEdicionPaquete
          ? 'Paquete modificado correctamente.'
          : 'Paquete creado correctamente.';

        this.cargarInfoPropietario();
        this.cargarHistorico();

        if (!this.modoEdicionPaquete) {
          this.paqueteForm = this.crearFormularioVacio();
        }
      },
      error: (err) => {
        this.guardandoPaquete = false;
        this.errorPaquete = err?.error?.message || 'Ocurrió un error al guardar el paquete.';
        console.error('Error guardando paquete', err);
      }
    });
  }

  crearCasa(): void {
    this.errorPaquete = '';
    this.exitoPaquete = '';

    const fotosFiltradas = this.fotosInputs
      .map(f => f.trim())
      .filter(f => f !== '');

    if (!this.casaForm.poblacion || !this.casaForm.descripcionGeneral) {
      this.errorPaquete = 'Población y descripción son obligatorias';
      return;
    }

    const payload = {
      ...this.casaForm,
      propietarioId: this.propietarioId,
      fotos: fotosFiltradas
    };

    console.log(JSON.stringify(payload, null, 2));

    this.dashboardService.crearCasa(payload).subscribe({
      next: () => {
        this.exitoPaquete = 'Casa creada correctamente';

        // reset formulario
        this.fotosInputs = [''];
        this.casaForm = {
          poblacion: '',
          descripcionGeneral: '',
          numeroComedores: 0,
          plazasGaraje: 0,
          propietarioId: 0,
          fotos: [],

          habitaciones: [
            {
              numeroCamas: 1,
              tipoCama: 'SENCILLA',
              tieneBano: false
            },
            {
              numeroCamas: 1,
              tipoCama: 'SENCILLA',
              tieneBano: false
            },
            {
              numeroCamas: 1,
              tipoCama: 'SENCILLA',
              tieneBano: false
            }
          ],

          banos: [
            {
              compartido: true
            },
            {
              compartido: true
            }
          ],

          cocinas: [
            {
              tieneLavadora: false,
              tieneLavavajillas: false
            }
          ]
        };

        this.cargarInfoPropietario();
        this.showPanel('casas');
      },
      error: (err) => {
        this.errorPaquete = err?.error?.message || 'Error al crear la casa';
        console.error(err);
      }
    });
  }

  darDeBajaCasa(casaId: number): void {
    this.errorPaquete = '';
    this.exitoPaquete = '';

    this.dashboardService.darDeBajaCasa(casaId, this.propietarioId).subscribe({
      next: () => {
        this.exitoPaquete = 'Casa dada de baja correctamente';

        // 🔥 refrescar lista
        this.cargarInfoPropietario();

        // opcional: limpiar mensajes después de unos segundos
        setTimeout(() => {
          this.exitoPaquete = '';
        }, 3000);
      },
      error: (err) => {
        this.errorPaquete = err?.error?.message || 'Error al dar de baja la casa';
        console.error(err);
      }
    });
  }

  confirmarDarDeBaja(casaId: number): void {
    const confirmacion = confirm('¿Seguro que deseas dar de baja esta casa?');

    if (!confirmacion) return;

    this.darDeBajaCasa(casaId);
  }

  agregarFotoInput(): void {
    this.fotosInputs.push('');
  }

  eliminarFotoInput(index: number): void {
    this.fotosInputs.splice(index, 1);
  }

  actualizarHabitaciones(cantidad: number): void {
    cantidad = Number(cantidad);

    while (this.casaForm.habitaciones.length < cantidad) {
      this.casaForm.habitaciones.push({
        numeroCamas: 1,
        tipoCama: 'SENCILLA',
        tieneBano: false
      });
    }

    while (this.casaForm.habitaciones.length > cantidad) {
      this.casaForm.habitaciones.pop();
    }
  }

  actualizarBanos(cantidad: number): void {
    cantidad = Number(cantidad);

    while (this.casaForm.banos.length < cantidad) {
      this.casaForm.banos.push({
        compartido: true
      });
    }

    while (this.casaForm.banos.length > cantidad) {
      this.casaForm.banos.pop();
    }
  }

  actualizarCocinas(cantidad: number): void {
    cantidad = Number(cantidad);

    while (this.casaForm.cocinas.length < cantidad) {
      this.casaForm.cocinas.push({
        tieneLavadora: false,
        tieneLavavajillas: false
      });
    }

    while (this.casaForm.cocinas.length > cantidad) {
      this.casaForm.cocinas.pop();
    }
  }

  get totalPaquetes(): number {
    return this.historicoFiltrado.length;
  }

  get vigentes(): number {
    return this.historicoFiltrado.filter(item => item.vigente === true).length;
  }

  get precioPromedio(): number {
    if (this.historicoFiltrado.length === 0) return 0;

    const suma = this.historicoFiltrado.reduce((acc, item) => {
      const precio =
        item.precioCasaEntera ??
        item.precioPorHabitacion ??
        0;

      return acc + precio;
    }, 0);

    return suma / this.historicoFiltrado.length;
  }

  get precioPromedioTexto(): string {
    return `$${this.precioPromedio.toFixed(0)}`;
  }

  registrarPago(reserva: ReservaDto): void {
    if (reserva.estadoPago !== 'PAGADA_PARCIAL') {
      return;
    }

    this.errorReserva = '';
    this.exitoReserva = '';
    this.registrandoPagoReservaId = reserva.numeroReserva;

    this.dashboardService.registrarPago(reserva.numeroReserva).subscribe({
      next: (reservaActualizada) => {
        this.reservas = this.reservas.map(item =>
          item.numeroReserva === reservaActualizada.numeroReserva ? reservaActualizada : item
        );

        this.exitoReserva = `La reserva #${reserva.numeroReserva} fue confirmada correctamente.`;
        this.registrandoPagoReservaId = null;
      },
      error: (err) => {
        this.errorReserva = err?.error?.message || 'No se pudo registrar el pago.';
        this.registrandoPagoReservaId = null;
        console.error('Error registrando pago', err);
      }
    });
  }

  puedeRegistrarPago(reserva: ReservaDto): boolean {
    return reserva.estadoPago === 'PAGADA_PARCIAL';
  }

  formatearEstadoReserva(estado: string): string {
    switch (estado) {
      case 'PENDIENTE_PAGO':
        return 'Pendiente de pago';
      case 'PAGADA_PARCIAL':
        return 'Pago reportado';
      case 'CONFIRMADA':
        return 'Confirmada';
      case 'VENCIDA':
        return 'Vencida';
      case 'CANCELADA':
        return 'Cancelada';
      default:
        return estado;
    }
  }

  claseEstadoReserva(estado: string): string {
    switch (estado) {
      case 'PENDIENTE_PAGO':
        return 'estado-pendiente';
      case 'PAGADA_PARCIAL':
        return 'estado-parcial';
      case 'CONFIRMADA':
        return 'estado-confirmada';
      case 'VENCIDA':
        return 'estado-vencido';
      case 'CANCELADA':
        return 'estado-cancelada';
      default:
        return 'estado-pendiente';
    }
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

  buscarCasaPorCodigo(): void {
    this.errorBusquedaCasa = '';

    const codigo = Number(this.codigoBusquedaCasa);

    if (!this.codigoBusquedaCasa.trim()) {
      this.errorBusquedaCasa = 'Debes ingresar un código.';
      return;
    }

    if (isNaN(codigo) || codigo <= 0) {
      this.errorBusquedaCasa = 'El código debe ser numérico y mayor que cero.';
      return;
    }

    const casaEncontrada = this.casas.find(casa => casa.codigo === codigo);

    if (!casaEncontrada) {
      this.errorBusquedaCasa = 'No se encontró una casa con ese código.';
      return;
    }

    localStorage.setItem('casaDetalleTemporal', JSON.stringify(casaEncontrada));
    this.router.navigate(['/detalle-casa', codigo]);
  }

  // Filtro para buscar reversas vencidas

  limpiarBusquedaCasa(): void {
    this.codigoBusquedaCasa = '';
    this.errorBusquedaCasa = '';
  }

  casaForm: CasaForm = {
    poblacion: '',
    descripcionGeneral: '',
    numeroComedores: 0,
    plazasGaraje: 0,
    propietarioId: 0,
    fotos: [],

    habitaciones: [
      {
        numeroCamas: 1,
        tipoCama: 'SENCILLA',
        tieneBano: false
      },
      {
        numeroCamas: 1,
        tipoCama: 'SENCILLA',
        tieneBano: false
      },
      {
        numeroCamas: 1,
        tipoCama: 'SENCILLA',
        tieneBano: false
      }
    ],

    banos: [
      {
        compartido: true
      },
      {
        compartido: true
      }
    ],

    cocinas: [
      {
        tieneLavadora: false,
        tieneLavavajillas: false
      }
    ]
  };
}
