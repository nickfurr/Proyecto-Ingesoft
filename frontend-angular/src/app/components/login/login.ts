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

  private limpiarSesionPropietario(): void {
    localStorage.removeItem('propietarioId');
    localStorage.removeItem('propietario-username');
    localStorage.removeItem('nombrePropietario');
    localStorage.removeItem('propietario-telefono');
    localStorage.removeItem('propietario-activo');
    localStorage.removeItem('propietario-cuentaBancaria');
  }

  private limpiarSesionCliente(): void {
    localStorage.removeItem('usuarioId');
    localStorage.removeItem('usuario-username');
    localStorage.removeItem('nombreUsuario');
    localStorage.removeItem('usuario-telefono');
    localStorage.removeItem('usuario-activo');
  }

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
  loginType: 'cliente' | 'propietario' = 'cliente';

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
            this.limpiarSesionCliente();
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
        this.limpiarSesionPropietario();
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

  // Register fields
  regType: 'cliente' | 'propietario' = 'cliente';
  regUsername = '';
  regPassword = '';
  regConfirmPassword = '';
  regEmail = '';
  regNombreCompleto = '';
  regTelefono = '';
  regCuentaBancaria = '';
  regErrorMessage = '';
  regSuccessMessage = '';
  regCargando = false;

  activeTab: 'login' | 'register' = 'login';

  setTab(tab: 'login' | 'register'): void {
    this.activeTab = tab;
    this.errorMessage = '';
    this.regErrorMessage = '';
    this.regSuccessMessage = '';
  }

  registrar(): void {
    this.regErrorMessage = '';
    this.regSuccessMessage = '';

    if (!this.regUsername.trim() || !this.regPassword.trim()) {
      this.regErrorMessage = 'El nombre de usuario y la contraseña son obligatorios.';
      return;
    }
    if (this.regPassword !== this.regConfirmPassword) {
      this.regErrorMessage = 'Las contraseñas no coinciden.';
      return;
    }
    if (!this.regEmail.trim()) {
      this.regErrorMessage = 'El correo electrónico es obligatorio.';
      return;
    }

    this.regCargando = true;

    if (this.regType === 'propietario') {
      const data: PropietarioDto = {
        description: '',
        id: null,
        username: this.regUsername.trim(),
        password: this.regPassword,
        email: this.regEmail.trim(),
        nombreCompleto: this.regNombreCompleto.trim() || null,
        telefono: this.regTelefono.trim() || null,
        numeroCuentaBancaria: this.regCuentaBancaria.trim() || null,
        activo: null
      };

      this.loginService.registrarPropietario(data).subscribe({
        next: (res) => {
          this.regCargando = false;
          if (res.description === 'Registro exitoso' || res.id) {
            this.regSuccessMessage = '¡Cuenta de propietario creada exitosamente! Ya puedes iniciar sesión.';
            this.regUsername = '';
            this.regPassword = '';
            this.regConfirmPassword = '';
            this.regEmail = '';
            this.regNombreCompleto = '';
            this.regTelefono = '';
            this.regCuentaBancaria = '';
          } else {
            this.regErrorMessage = res.description || 'No se pudo completar el registro.';
          }
        },
        error: (e) => {
          this.regCargando = false;
          if (e.status === 409) {
            this.regErrorMessage = e.error?.description || 'El usuario o correo ya está registrado.';
          } else {
            this.regErrorMessage = 'Ocurrió un error al registrarse. Intenta de nuevo.';
          }
        }
      });
    } else {
      const data: ClienteDto = {
        description: '',
        id: null,
        username: this.regUsername.trim(),
        email: this.regEmail.trim(),
        password: this.regPassword,
        telefonoContacto: this.regTelefono.trim() || null
      };

      this.loginService.registrarCliente(data).subscribe({
        next: (res) => {
          this.regCargando = false;
          if (res.description === 'Registro exitoso' || res.id) {
            this.regSuccessMessage = '¡Cuenta de cliente creada exitosamente! Ya puedes iniciar sesión.';
            this.regUsername = '';
            this.regPassword = '';
            this.regConfirmPassword = '';
            this.regEmail = '';
            this.regTelefono = '';
            this.regNombreCompleto = '';
            this.regCuentaBancaria = '';
          } else {
            this.regErrorMessage = res.description || 'No se pudo completar el registro.';
          }
        },
        error: (e) => {
          this.regCargando = false;
          if (e.status === 409) {
            this.regErrorMessage = e.error?.description || 'El usuario o correo ya está registrado.';
          } else {
            this.regErrorMessage = 'Ocurrió un error al registrarse como cliente. Intenta de nuevo.';
          }
        }
      });
    }
  }

}
