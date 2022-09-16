import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConventionCollectiveComponent } from './list/convention-collective.component';
import { ConventionCollectiveDetailComponent } from './detail/convention-collective-detail.component';
import { ConventionCollectiveUpdateComponent } from './update/convention-collective-update.component';
import { ConventionCollectiveDeleteDialogComponent } from './delete/convention-collective-delete-dialog.component';
import { ConventionCollectiveRoutingModule } from './route/convention-collective-routing.module';

@NgModule({
  imports: [SharedModule, ConventionCollectiveRoutingModule],
  declarations: [
    ConventionCollectiveComponent,
    ConventionCollectiveDetailComponent,
    ConventionCollectiveUpdateComponent,
    ConventionCollectiveDeleteDialogComponent,
  ],
})
export class ConventionCollectiveModule {}
