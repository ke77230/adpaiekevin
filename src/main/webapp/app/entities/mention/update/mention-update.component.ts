import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MentionFormService, MentionFormGroup } from './mention-form.service';
import { IMention } from '../mention.model';
import { MentionService } from '../service/mention.service';

@Component({
  selector: 'jhi-mention-update',
  templateUrl: './mention-update.component.html',
})
export class MentionUpdateComponent implements OnInit {
  isSaving = false;
  mention: IMention | null = null;

  editForm: MentionFormGroup = this.mentionFormService.createMentionFormGroup();

  constructor(
    protected mentionService: MentionService,
    protected mentionFormService: MentionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mention }) => {
      this.mention = mention;
      if (mention) {
        this.updateForm(mention);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mention = this.mentionFormService.getMention(this.editForm);
    if (mention.id !== null) {
      this.subscribeToSaveResponse(this.mentionService.update(mention));
    } else {
      this.subscribeToSaveResponse(this.mentionService.create(mention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMention>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mention: IMention): void {
    this.mention = mention;
    this.mentionFormService.resetForm(this.editForm, mention);
  }
}
