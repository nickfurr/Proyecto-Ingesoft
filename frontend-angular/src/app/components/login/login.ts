import {Component, inject} from '@angular/core';
import { LoginService } from '../../services/login.service';
import { PropietarioDto } from '../../models/propietario-dto';
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
  private LoginService = inject(LoginService);
  private router = inject(Router);

  errorMessage = '';
  descripcion = '';
  id = 0;
  username = '';
  password = '';
  email = '';
  nombreCompleto = '';
  telefono = '';
  cuentaBancaria = '';
  activo = false;
  cargando = false;

  login(): void {
    this.errorMessage = '';
    this.cargando = true;

    const data: PropietarioDto = {
      descripcion: this.descripcion,
      id: this.id,
      username: this.username,
      email: this.email,
      password: this.password,
      nombreCompleto: this.nombreCompleto,
      telefono: this.telefono,
      cuentaBancaria:this.cuentaBancaria,
      activo:this.activo
    };

    this.LoginService.loginPropietario(data).subscribe({
      next: (res) => {
        this.cargando = false;
        if(res.activo){
          localStorage.setItem('propietarioId', String(res.id));
          localStorage.setItem('nombrePropietario', res.nombreCompleto ?? '');
          this.router.navigate(['/dashboard-propietario']);

        }else{
          this.errorMessage = res.descripcion;
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
  }
}
