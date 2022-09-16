import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITauxDImposition } from '../taux-d-imposition.model';
import { TauxDImpositionService } from '../service/taux-d-imposition.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './taux-d-imposition-delete-dialog.component.html',
})
export class TauxDImpositionDeleteDialogComponent {
  tauxDImposition?: ITauxDImposition;

  constructor(protected tauxDImpositionService: TauxDImpositionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tauxDImpositionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
