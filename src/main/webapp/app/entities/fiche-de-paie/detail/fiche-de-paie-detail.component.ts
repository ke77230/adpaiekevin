import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFicheDePaie } from '../fiche-de-paie.model';

@Component({
  selector: 'jhi-fiche-de-paie-detail',
  templateUrl: './fiche-de-paie-detail.component.html',
})
export class FicheDePaieDetailComponent implements OnInit {
  ficheDePaie: IFicheDePaie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ficheDePaie }) => {
      this.ficheDePaie = ficheDePaie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
