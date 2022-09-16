import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICotisation } from '../cotisation.model';
import { CotisationService } from '../service/cotisation.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './cotisation-delete-dialog.component.html',
})
export class CotisationDeleteDialogComponent {
  cotisation?: ICotisation;

  constructor(protected cotisationService: CotisationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cotisationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
