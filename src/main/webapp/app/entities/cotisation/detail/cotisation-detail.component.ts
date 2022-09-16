import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICotisation } from '../cotisation.model';

@Component({
  selector: 'jhi-cotisation-detail',
  templateUrl: './cotisation-detail.component.html',
})
export class CotisationDetailComponent implements OnInit {
  cotisation: ICotisation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cotisation }) => {
      this.cotisation = cotisation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
