import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMention } from '../mention.model';

@Component({
  selector: 'jhi-mention-detail',
  templateUrl: './mention-detail.component.html',
})
export class MentionDetailComponent implements OnInit {
  mention: IMention | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mention }) => {
      this.mention = mention;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
