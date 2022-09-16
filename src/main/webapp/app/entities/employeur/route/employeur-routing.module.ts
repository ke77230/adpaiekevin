import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeurComponent } from '../list/employeur.component';
import { EmployeurDetailComponent } from '../detail/employeur-detail.component';
import { EmployeurUpdateComponent } from '../update/employeur-update.component';
import { EmployeurRoutingResolveService } from './employeur-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const employeurRoute: Routes = [
  {
    path: '',
    component: EmployeurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeurDetailComponent,
    resolve: {
      employeur: EmployeurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeurUpdateComponent,
    resolve: {
      employeur: EmployeurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeurUpdateComponent,
    resolve: {
      employeur: EmployeurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeurRoute)],
  exports: [RouterModule],
})
export class EmployeurRoutingModule {}
