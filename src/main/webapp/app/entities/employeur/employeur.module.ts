import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeurComponent } from './list/employeur.component';
import { EmployeurDetailComponent } from './detail/employeur-detail.component';
import { EmployeurUpdateComponent } from './update/employeur-update.component';
import { EmployeurDeleteDialogComponent } from './delete/employeur-delete-dialog.component';
import { EmployeurRoutingModule } from './route/employeur-routing.module';

@NgModule({
  imports: [SharedModule, EmployeurRoutingModule],
  declarations: [EmployeurComponent, EmployeurDetailComponent, EmployeurUpdateComponent, EmployeurDeleteDialogComponent],
})
export class EmployeurModule {}
