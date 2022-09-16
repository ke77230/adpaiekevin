import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConventionCollective } from '../convention-collective.model';

@Component({
  selector: 'jhi-convention-collective-detail',
  templateUrl: './convention-collective-detail.component.html',
})
export class ConventionCollectiveDetailComponent implements OnInit {
  conventionCollective: IConventionCollective | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conventionCollective }) => {
      this.conventionCollective = conventionCollective;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
