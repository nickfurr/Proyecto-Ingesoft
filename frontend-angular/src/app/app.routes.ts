import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import {DashboardPropietario} from './components/dashboard-propietario/dashboard-propietario';
import { DashboardClient } from './components/dashboard-client/dashboard-client';
import { DetalleCasa } from './components/detalle-casa/detalle-casa';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'dashboard-propietario', component: DashboardPropietario },
  { path: 'dashboard-client', component: DashboardClient },
  { path: 'detalle-casa/:codigo', component: DetalleCasa }
];
