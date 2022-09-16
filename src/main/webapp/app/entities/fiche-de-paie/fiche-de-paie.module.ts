import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FicheDePaieComponent } from './list/fiche-de-paie.component';
import { FicheDePaieDetailComponent } from './detail/fiche-de-paie-detail.component';
import { FicheDePaieUpdateComponent } from './update/fiche-de-paie-update.component';
import { FicheDePaieDeleteDialogComponent } from './delete/fiche-de-paie-delete-dialog.component';
import { FicheDePaieRoutingModule } from './route/fiche-de-paie-routing.module';

@NgModule({
  imports: [SharedModule, FicheDePaieRoutingModule],
  declarations: [FicheDePaieComponent, FicheDePaieDetailComponent, FicheDePaieUpdateComponent, FicheDePaieDeleteDialogComponent],
})
export class FicheDePaieModule {}
