import {Component, inject} from '@angular/core';
import { LoginService } from '../../services/login.service';
import { PropietarioDto } from '../../models/propietario-dto';
import { ClienteDto } from '../../models/cliente-dto';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private loginService = inject(LoginService);
  private router = inject(Router);

  errorMessage = '';
  description = '';
  id = 0;
  username = '';
  password = '';
  email = '';
  nombreCompleto = '';
  telefono = '';
  numeroCuentaBancaria = '';
  activo = false;
  cargando = false;
  loginType: 'cliente' | 'propietario' = 'propietario';

  login(): void {
    this.errorMessage = '';
    this.cargando = true;

    const identificador = this.email.trim();
    if (!identificador || !this.password.trim()) {
      this.errorMessage = 'Debes ingresar usuario y contraseña.';
      this.cargando = false;
      return;
    }

    if (this.loginType === 'propietario') {
      const data: PropietarioDto = {
        description: this.description,
        id: this.id,
        username: identificador,
        email: identificador,
        password: this.password,
        nombreCompleto: this.nombreCompleto,
        telefono: this.telefono,
        numeroCuentaBancaria: this.numeroCuentaBancaria,
        activo: this.activo
      };

      this.loginService.loginPropietario(data).subscribe({
        next: (res) => {
          this.cargando = false;
          if (res.activo) {
            localStorage.setItem('propietarioId', String(res.id));
            localStorage.setItem('propietario-username', res.username ?? '');
            localStorage.setItem('nombrePropietario', res.nombreCompleto ?? '');
            localStorage.setItem('propietario-telefono', res.telefono ?? '');
            localStorage.setItem('propietario-activo', String(res.activo));
            localStorage.setItem('propietario-cuentaBancaria', res.numeroCuentaBancaria ?? '');

            this.router.navigate(['/dashboard-propietario']);
          } else {
            this.errorMessage = res.description || 'Tu cuenta está inactiva.';
          }
        },
        error: (e) => {
          this.cargando = false;

          if (e.status === 401) {
            this.errorMessage = 'Credenciales incorrectas';
          } else {
            this.errorMessage = 'Ocurrió un error al iniciar sesión';
          }
        }
      });
      return;
    }

    const data: ClienteDto = {
      description: '',
      id: 0,
      username: identificador,
      email: identificador,
      password: this.password,
      telefonoContacto: null
    };

    this.loginService.loginCliente(data).subscribe({
      next: (res) => {
        this.cargando = false;
        localStorage.setItem('usuarioId', String(res.id));
        localStorage.setItem('usuario-username', res.username ?? '');
        localStorage.setItem('nombreUsuario', res.username ?? '');
        localStorage.setItem('usuario-telefono', res.telefonoContacto ?? '');
        localStorage.setItem('usuario-activo', 'true');

        this.router.navigate(['/dashboard-client']);
      },
      error: (e) => {
        this.cargando = false;

        if (e.status === 401) {
          this.errorMessage = 'Credenciales incorrectas';
        } else {
          this.errorMessage = 'Ocurrió un error al iniciar sesión';
        }
      }
    });
  }
}
