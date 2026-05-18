import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PropietarioDto } from '../models/propietario-dto';
import { ClienteDto } from '../models/cliente-dto';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/v1/propietarios';
  private apiClientes = 'http://localhost:8080/api/v1/clientes';

  loginPropietario(data: PropietarioDto): Observable<PropietarioDto> {
    return this.http.post<PropietarioDto>(`${this.apiUrl}/login-propietario`, data);
  }

  loginCliente(data: ClienteDto): Observable<ClienteDto> {
    return this.http.post<ClienteDto>(`${this.apiClientes}/login-cliente`, data);
  }

  registrarPropietario(data: PropietarioDto): Observable<PropietarioDto> {
    return this.http.post<PropietarioDto>(`${this.apiUrl}/registro`, data);
  }

  registrarCliente(data: ClienteDto): Observable<ClienteDto> {
    return this.http.post<ClienteDto>(`${this.apiClientes}/registro`, data);
  }
}
