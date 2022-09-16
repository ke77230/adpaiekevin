import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CotisationComponent } from './list/cotisation.component';
import { CotisationDetailComponent } from './detail/cotisation-detail.component';
import { CotisationUpdateComponent } from './update/cotisation-update.component';
import { CotisationDeleteDialogComponent } from './delete/cotisation-delete-dialog.component';
import { CotisationRoutingModule } from './route/cotisation-routing.module';

@NgModule({
  imports: [SharedModule, CotisationRoutingModule],
  declarations: [CotisationComponent, CotisationDetailComponent, CotisationUpdateComponent, CotisationDeleteDialogComponent],
})
export class CotisationModule {}
