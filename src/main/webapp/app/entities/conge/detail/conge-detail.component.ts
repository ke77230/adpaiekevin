import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConge } from '../conge.model';

@Component({
  selector: 'jhi-conge-detail',
  templateUrl: './conge-detail.component.html',
})
export class CongeDetailComponent implements OnInit {
  conge: IConge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conge }) => {
      this.conge = conge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
