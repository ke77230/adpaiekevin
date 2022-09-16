import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TauxDImpositionComponent } from './list/taux-d-imposition.component';
import { TauxDImpositionDetailComponent } from './detail/taux-d-imposition-detail.component';
import { TauxDImpositionUpdateComponent } from './update/taux-d-imposition-update.component';
import { TauxDImpositionDeleteDialogComponent } from './delete/taux-d-imposition-delete-dialog.component';
import { TauxDImpositionRoutingModule } from './route/taux-d-imposition-routing.module';

@NgModule({
  imports: [SharedModule, TauxDImpositionRoutingModule],
  declarations: [
    TauxDImpositionComponent,
    TauxDImpositionDetailComponent,
    TauxDImpositionUpdateComponent,
    TauxDImpositionDeleteDialogComponent,
  ],
})
export class TauxDImpositionModule {}
