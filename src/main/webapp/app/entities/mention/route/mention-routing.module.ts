import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MentionComponent } from '../list/mention.component';
import { MentionDetailComponent } from '../detail/mention-detail.component';
import { MentionUpdateComponent } from '../update/mention-update.component';
import { MentionRoutingResolveService } from './mention-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const mentionRoute: Routes = [
  {
    path: '',
    component: MentionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MentionDetailComponent,
    resolve: {
      mention: MentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MentionUpdateComponent,
    resolve: {
      mention: MentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MentionUpdateComponent,
    resolve: {
      mention: MentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mentionRoute)],
  exports: [RouterModule],
})
export class MentionRoutingModule {}
