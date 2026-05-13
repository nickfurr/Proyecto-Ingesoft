import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  private router = inject(Router);

  private get storage(): Storage | null {
    return typeof localStorage !== 'undefined' ? localStorage : null;
  }

  get estaAutenticado(): boolean {
    return !!(this.storage?.getItem('propietarioId') || this.storage?.getItem('usuarioId'));
  }

  get esPropietario(): boolean {
    return !!this.storage?.getItem('propietarioId');
  }

  get nombreUsuario(): string {
    return this.storage?.getItem('nombrePropietario')
      || this.storage?.getItem('nombreUsuario')
      || '';
  }

  irAInicio(): void {
    this.router.navigate(['/landing']);
  }

  irAPanel(): void {
    if (this.esPropietario) {
      this.router.navigate(['/dashboard-propietario']);
    } else {
      this.router.navigate(['/dashboard-client']);
    }
  }

  irALogin(): void {
    this.router.navigate(['/login']);
  }

  cerrarSesion(): void {
    if (this.storage) localStorage.clear();
    this.router.navigate(['/']);
  }
}
