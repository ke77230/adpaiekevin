import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeur } from '../employeur.model';

@Component({
  selector: 'jhi-employeur-detail',
  templateUrl: './employeur-detail.component.html',
})
export class EmployeurDetailComponent implements OnInit {
  employeur: IEmployeur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeur }) => {
      this.employeur = employeur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
