import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFicheDePaie } from '../fiche-de-paie.model';
import { FicheDePaieService } from '../service/fiche-de-paie.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fiche-de-paie-delete-dialog.component.html',
})
export class FicheDePaieDeleteDialogComponent {
  ficheDePaie?: IFicheDePaie;

  constructor(protected ficheDePaieService: FicheDePaieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ficheDePaieService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
