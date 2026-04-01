import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import {DashboardPropietario} from './components/dashboard-propietario/dashboard-propietario';
import { DetalleCasa } from './components/detalle-casa/detalle-casa';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'dashboard-propietario', component: DashboardPropietario },
  { path: 'detalle-casa/:codigo', component: DetalleCasa }
];
