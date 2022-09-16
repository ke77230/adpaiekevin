import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBonus } from '../bonus.model';
import { BonusService } from '../service/bonus.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bonus-delete-dialog.component.html',
})
export class BonusDeleteDialogComponent {
  bonus?: IBonus;

  constructor(protected bonusService: BonusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bonusService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
