import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITauxDImposition } from '../taux-d-imposition.model';

@Component({
  selector: 'jhi-taux-d-imposition-detail',
  templateUrl: './taux-d-imposition-detail.component.html',
})
export class TauxDImpositionDetailComponent implements OnInit {
  tauxDImposition: ITauxDImposition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tauxDImposition }) => {
      this.tauxDImposition = tauxDImposition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
