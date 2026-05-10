import { Routes } from '@angular/router';

import { Admin } from './component/admin/admin';
import { AdminTicketsComponents } from './component/admin-tickets-components/admin-tickets-components';
import { AdminTicketsDetalleComponents } from './component/admin-tickets-detalle-components/admin-tickets-detalle-components';
import { Carrito } from './component/carrito/carrito';
import { HomeComponents } from './component/home-components/home-components';
import { Login } from './component/login/login';
import { MisPedidos } from './component/mis-pedidos/mis-pedidos';
import { ResetPasswordComponent } from './component/password/reset-password';
import { PedidoDetalle } from './component/pedido-detalle/pedido-detalle';
import { PerfilComponent } from './component/perfil/perfil';
import { Tickets } from './component/tickets/tickets';
import { UltimosAccesosComponent } from './component/usuario/ultimos-accesos';
import { UsuariosComponent } from './component/usuario/usuarios';
import { AuthGuard } from './guard/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'login', component: Login },
  {
    path: 'admin',
    component: Admin,
    canActivate: [AuthGuard],
    data: { roles: ['ADMINISTRADOR'] },
    children: [
      { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
      { path: 'usuarios', component: UsuariosComponent },
      { path: 'accesos', component: UltimosAccesosComponent },
      { path: 'tickets', component: AdminTicketsComponents },
      { path: 'detalle/:id', component: AdminTicketsDetalleComponents },
    ],
  },
  { path: 'home', component: HomeComponents },
  { path: 'tickets', component: Tickets, canActivate: [AuthGuard], data: { roles: ['CLIENTE'] } },
  { path: 'carrito', component: Carrito, canActivate: [AuthGuard], data: { roles: ['CLIENTE'] } },
  { path: 'mis-pedidos', component: MisPedidos, canActivate: [AuthGuard], data: { roles: ['CLIENTE'] } },
  { path: 'pedido/:id', component: PedidoDetalle, canActivate: [AuthGuard], data: { roles: ['CLIENTE'] } },
  { path: 'reset-password', component: ResetPasswordComponent },
  {
    path: 'perfil',
    component: PerfilComponent,
    canActivate: [AuthGuard],
    data: { roles: ['CLIENTE', 'ADMINISTRADOR'] },
  },
  { path: '**', redirectTo: '/login' },
];
