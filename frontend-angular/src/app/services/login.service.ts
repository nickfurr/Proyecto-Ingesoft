import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PropietarioDto } from '../models/propietario-dto';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/v1';

  loginPropietario(data: PropietarioDto): Observable<PropietarioDto> {
    return this.http.post<PropietarioDto>(`${this.apiUrl}/login-propietario`, data);
  }
}
