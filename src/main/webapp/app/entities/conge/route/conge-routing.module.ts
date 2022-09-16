import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CongeComponent } from '../list/conge.component';
import { CongeDetailComponent } from '../detail/conge-detail.component';
import { CongeUpdateComponent } from '../update/conge-update.component';
import { CongeRoutingResolveService } from './conge-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const congeRoute: Routes = [
  {
    path: '',
    component: CongeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CongeDetailComponent,
    resolve: {
      conge: CongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CongeUpdateComponent,
    resolve: {
      conge: CongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CongeUpdateComponent,
    resolve: {
      conge: CongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(congeRoute)],
  exports: [RouterModule],
})
export class CongeRoutingModule {}
