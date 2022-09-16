import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MentionComponent } from './list/mention.component';
import { MentionDetailComponent } from './detail/mention-detail.component';
import { MentionUpdateComponent } from './update/mention-update.component';
import { MentionDeleteDialogComponent } from './delete/mention-delete-dialog.component';
import { MentionRoutingModule } from './route/mention-routing.module';

@NgModule({
  imports: [SharedModule, MentionRoutingModule],
  declarations: [MentionComponent, MentionDetailComponent, MentionUpdateComponent, MentionDeleteDialogComponent],
})
export class MentionModule {}
