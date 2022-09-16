import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FicheDePaieFormService } from './fiche-de-paie-form.service';
import { FicheDePaieService } from '../service/fiche-de-paie.service';
import { IFicheDePaie } from '../fiche-de-paie.model';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';
import { IEmployeur } from 'app/entities/employeur/employeur.model';
import { EmployeurService } from 'app/entities/employeur/service/employeur.service';
import { ITauxDImposition } from 'app/entities/taux-d-imposition/taux-d-imposition.model';
import { TauxDImpositionService } from 'app/entities/taux-d-imposition/service/taux-d-imposition.service';
import { ICotisation } from 'app/entities/cotisation/cotisation.model';
import { CotisationService } from 'app/entities/cotisation/service/cotisation.service';
import { IMention } from 'app/entities/mention/mention.model';
import { MentionService } from 'app/entities/mention/service/mention.service';

import { FicheDePaieUpdateComponent } from './fiche-de-paie-update.component';

describe('FicheDePaie Management Update Component', () => {
  let comp: FicheDePaieUpdateComponent;
  let fixture: ComponentFixture<FicheDePaieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ficheDePaieFormService: FicheDePaieFormService;
  let ficheDePaieService: FicheDePaieService;
  let contratService: ContratService;
  let employeurService: EmployeurService;
  let tauxDImpositionService: TauxDImpositionService;
  let cotisationService: CotisationService;
  let mentionService: MentionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FicheDePaieUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FicheDePaieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FicheDePaieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ficheDePaieFormService = TestBed.inject(FicheDePaieFormService);
    ficheDePaieService = TestBed.inject(FicheDePaieService);
    contratService = TestBed.inject(ContratService);
    employeurService = TestBed.inject(EmployeurService);
    tauxDImpositionService = TestBed.inject(TauxDImpositionService);
    cotisationService = TestBed.inject(CotisationService);
    mentionService = TestBed.inject(MentionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contrat query and add missing value', () => {
      const ficheDePaie: IFicheDePaie = { id: 456 };
      const contrat: IContrat = { id: 70096 };
      ficheDePaie.contrat = contrat;

      const contratCollection: IContrat[] = [{ id: 6119 }];
      jest.spyOn(contratService, 'query').mockReturnValue(of(new HttpResponse({ body: contratCollection })));
      const additionalContrats = [contrat];
      const expectedCollection: IContrat[] = [...additionalContrats, ...contratCollection];
      jest.spyOn(contratService, 'addContratToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      expect(contratService.query).toHaveBeenCalled();
      expect(contratService.addContratToCollectionIfMissing).toHaveBeenCalledWith(
        contratCollection,
        ...additionalContrats.map(expect.objectContaining)
      );
      expect(comp.contratsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employeur query and add missing value', () => {
      const ficheDePaie: IFicheDePaie = { id: 456 };
      const employeur: IEmployeur = { id: 72334 };
      ficheDePaie.employeur = employeur;

      const employeurCollection: IEmployeur[] = [{ id: 70074 }];
      jest.spyOn(employeurService, 'query').mockReturnValue(of(new HttpResponse({ body: employeurCollection })));
      const additionalEmployeurs = [employeur];
      const expectedCollection: IEmployeur[] = [...additionalEmployeurs, ...employeurCollection];
      jest.spyOn(employeurService, 'addEmployeurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      expect(employeurService.query).toHaveBeenCalled();
      expect(employeurService.addEmployeurToCollectionIfMissing).toHaveBeenCalledWith(
        employeurCollection,
        ...additionalEmployeurs.map(expect.objectContaining)
      );
      expect(comp.employeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TauxDImposition query and add missing value', () => {
      const ficheDePaie: IFicheDePaie = { id: 456 };
      const imposition: ITauxDImposition = { id: 21164 };
      ficheDePaie.imposition = imposition;

      const tauxDImpositionCollection: ITauxDImposition[] = [{ id: 96464 }];
      jest.spyOn(tauxDImpositionService, 'query').mockReturnValue(of(new HttpResponse({ body: tauxDImpositionCollection })));
      const additionalTauxDImpositions = [imposition];
      const expectedCollection: ITauxDImposition[] = [...additionalTauxDImpositions, ...tauxDImpositionCollection];
      jest.spyOn(tauxDImpositionService, 'addTauxDImpositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      expect(tauxDImpositionService.query).toHaveBeenCalled();
      expect(tauxDImpositionService.addTauxDImpositionToCollectionIfMissing).toHaveBeenCalledWith(
        tauxDImpositionCollection,
        ...additionalTauxDImpositions.map(expect.objectContaining)
      );
      expect(comp.tauxDImpositionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cotisation query and add missing value', () => {
      const ficheDePaie: IFicheDePaie = { id: 456 };
      const cotisations: ICotisation[] = [{ id: 26886 }];
      ficheDePaie.cotisations = cotisations;

      const cotisationCollection: ICotisation[] = [{ id: 77734 }];
      jest.spyOn(cotisationService, 'query').mockReturnValue(of(new HttpResponse({ body: cotisationCollection })));
      const additionalCotisations = [...cotisations];
      const expectedCollection: ICotisation[] = [...additionalCotisations, ...cotisationCollection];
      jest.spyOn(cotisationService, 'addCotisationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      expect(cotisationService.query).toHaveBeenCalled();
      expect(cotisationService.addCotisationToCollectionIfMissing).toHaveBeenCalledWith(
        cotisationCollection,
        ...additionalCotisations.map(expect.objectContaining)
      );
      expect(comp.cotisationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Mention query and add missing value', () => {
      const ficheDePaie: IFicheDePaie = { id: 456 };
      const mentions: IMention[] = [{ id: 38610 }];
      ficheDePaie.mentions = mentions;

      const mentionCollection: IMention[] = [{ id: 46957 }];
      jest.spyOn(mentionService, 'query').mockReturnValue(of(new HttpResponse({ body: mentionCollection })));
      const additionalMentions = [...mentions];
      const expectedCollection: IMention[] = [...additionalMentions, ...mentionCollection];
      jest.spyOn(mentionService, 'addMentionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      expect(mentionService.query).toHaveBeenCalled();
      expect(mentionService.addMentionToCollectionIfMissing).toHaveBeenCalledWith(
        mentionCollection,
        ...additionalMentions.map(expect.objectContaining)
      );
      expect(comp.mentionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ficheDePaie: IFicheDePaie = { id: 456 };
      const contrat: IContrat = { id: 52367 };
      ficheDePaie.contrat = contrat;
      const employeur: IEmployeur = { id: 14327 };
      ficheDePaie.employeur = employeur;
      const imposition: ITauxDImposition = { id: 92147 };
      ficheDePaie.imposition = imposition;
      const cotisation: ICotisation = { id: 55741 };
      ficheDePaie.cotisations = [cotisation];
      const mention: IMention = { id: 6006 };
      ficheDePaie.mentions = [mention];

      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      expect(comp.contratsSharedCollection).toContain(contrat);
      expect(comp.employeursSharedCollection).toContain(employeur);
      expect(comp.tauxDImpositionsSharedCollection).toContain(imposition);
      expect(comp.cotisationsSharedCollection).toContain(cotisation);
      expect(comp.mentionsSharedCollection).toContain(mention);
      expect(comp.ficheDePaie).toEqual(ficheDePaie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFicheDePaie>>();
      const ficheDePaie = { id: 123 };
      jest.spyOn(ficheDePaieFormService, 'getFicheDePaie').mockReturnValue(ficheDePaie);
      jest.spyOn(ficheDePaieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ficheDePaie }));
      saveSubject.complete();

      // THEN
      expect(ficheDePaieFormService.getFicheDePaie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ficheDePaieService.update).toHaveBeenCalledWith(expect.objectContaining(ficheDePaie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFicheDePaie>>();
      const ficheDePaie = { id: 123 };
      jest.spyOn(ficheDePaieFormService, 'getFicheDePaie').mockReturnValue({ id: null });
      jest.spyOn(ficheDePaieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ficheDePaie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ficheDePaie }));
      saveSubject.complete();

      // THEN
      expect(ficheDePaieFormService.getFicheDePaie).toHaveBeenCalled();
      expect(ficheDePaieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFicheDePaie>>();
      const ficheDePaie = { id: 123 };
      jest.spyOn(ficheDePaieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ficheDePaie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ficheDePaieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContrat', () => {
      it('Should forward to contratService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contratService, 'compareContrat');
        comp.compareContrat(entity, entity2);
        expect(contratService.compareContrat).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployeur', () => {
      it('Should forward to employeurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeurService, 'compareEmployeur');
        comp.compareEmployeur(entity, entity2);
        expect(employeurService.compareEmployeur).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTauxDImposition', () => {
      it('Should forward to tauxDImpositionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tauxDImpositionService, 'compareTauxDImposition');
        comp.compareTauxDImposition(entity, entity2);
        expect(tauxDImpositionService.compareTauxDImposition).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCotisation', () => {
      it('Should forward to cotisationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cotisationService, 'compareCotisation');
        comp.compareCotisation(entity, entity2);
        expect(cotisationService.compareCotisation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMention', () => {
      it('Should forward to mentionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(mentionService, 'compareMention');
        comp.compareMention(entity, entity2);
        expect(mentionService.compareMention).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
