import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConventionCollective } from '../convention-collective.model';
import { ConventionCollectiveService } from '../service/convention-collective.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './convention-collective-delete-dialog.component.html',
})
export class ConventionCollectiveDeleteDialogComponent {
  conventionCollective?: IConventionCollective;

  constructor(protected conventionCollectiveService: ConventionCollectiveService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conventionCollectiveService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
