import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CongeComponent } from './list/conge.component';
import { CongeDetailComponent } from './detail/conge-detail.component';
import { CongeUpdateComponent } from './update/conge-update.component';
import { CongeDeleteDialogComponent } from './delete/conge-delete-dialog.component';
import { CongeRoutingModule } from './route/conge-routing.module';

@NgModule({
  imports: [SharedModule, CongeRoutingModule],
  declarations: [CongeComponent, CongeDetailComponent, CongeUpdateComponent, CongeDeleteDialogComponent],
})
export class CongeModule {}
