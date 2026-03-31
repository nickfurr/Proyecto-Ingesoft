import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CasaRuralDto } from '../models/casa-rural-dto';
import { ReservaDto } from '../models/reserva-dto';
import { PaqueteAlquilerDto } from '../models/paquete-alquiler-dto';

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
}
