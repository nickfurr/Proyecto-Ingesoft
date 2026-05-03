import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CasaRuralDto } from '../models/casa-rural-dto';
import { ReservaDto } from '../models/reserva-dto';
import { PaqueteAlquilerDto } from '../models/paquete-alquiler-dto';
import { HistoricoPaqueteDto } from '../models/historico-paquete-dto';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private http = inject(HttpClient);

  private apiCasas = 'http://localhost:8080/api/v1/casas';
  private apiReservas = 'http://localhost:8080/api/v1/reservas';
  private apiPaquetes = 'http://localhost:8080/api/v1/paquetes';

  obtenerCasasPorPropietario(propietarioId: number): Observable<CasaRuralDto[]> {
    return this.http.get<CasaRuralDto[]>(`${this.apiCasas}/propietario/${propietarioId}`);
  }

  obtenerReservasPorPropietario(propietarioId: number): Observable<ReservaDto[]> {
    return this.http.get<ReservaDto[]>(`${this.apiReservas}/propietario/${propietarioId}`);
  }

  obtenerPaquetesActivosPorPropietario(propietarioId: number): Observable<PaqueteAlquilerDto[]> {
    return this.http.get<PaqueteAlquilerDto[]>(`${this.apiPaquetes}/propietario/${propietarioId}/activos`);
  }
  obtenerHistoricoPaquetes(propietarioId: number, fechaInicio: string, fechaFin: string): Observable<HistoricoPaqueteDto[]> {
    return this.http.get<HistoricoPaqueteDto[]>(
      `${this.apiPaquetes}/propietario/${propietarioId}/historico`,
      {
        params: {
          fechaInicio,
          fechaFin
        }
      }
    );
  }
  crearPaquete(propietarioId: number, payload: PaqueteAlquilerDto): Observable<any> {
    return this.http.post(`${this.apiPaquetes}`, payload, {
      params: { propietarioId }
    });
  }

  modificarPaquete(paqueteId: number, propietarioId: number, payload: PaqueteAlquilerDto): Observable<any> {
    return this.http.put(`${this.apiPaquetes}/${paqueteId}`, payload, {
      params: { propietarioId }
    });
  }

  registrarPago(numeroReserva: number): Observable<ReservaDto> {
    return this.http.patch<ReservaDto>(`${this.apiReservas}/${numeroReserva}/registrar-pago`, {});
  }

  crearCasa(payload: any): Observable<any> {
    return this.http.post(this.apiCasas, payload);
  }

  darDeBajaCasa(casaId: number, propietarioId: number): Observable<any> {
    return this.http.delete(`${this.apiCasas}/${casaId}/propietario/${propietarioId}`);
  }
}
