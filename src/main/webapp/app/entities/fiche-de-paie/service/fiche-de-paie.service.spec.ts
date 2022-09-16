import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFicheDePaie } from '../fiche-de-paie.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fiche-de-paie.test-samples';

import { FicheDePaieService, RestFicheDePaie } from './fiche-de-paie.service';

const requireRestSample: RestFicheDePaie = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
  datepaiement: sampleWithRequiredData.datepaiement?.format(DATE_FORMAT),
};

describe('FicheDePaie Service', () => {
  let service: FicheDePaieService;
  let httpMock: HttpTestingController;
  let expectedResult: IFicheDePaie | IFicheDePaie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FicheDePaieService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FicheDePaie', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ficheDePaie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ficheDePaie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FicheDePaie', () => {
      const ficheDePaie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ficheDePaie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FicheDePaie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FicheDePaie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FicheDePaie', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFicheDePaieToCollectionIfMissing', () => {
      it('should add a FicheDePaie to an empty array', () => {
        const ficheDePaie: IFicheDePaie = sampleWithRequiredData;
        expectedResult = service.addFicheDePaieToCollectionIfMissing([], ficheDePaie);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ficheDePaie);
      });

      it('should not add a FicheDePaie to an array that contains it', () => {
        const ficheDePaie: IFicheDePaie = sampleWithRequiredData;
        const ficheDePaieCollection: IFicheDePaie[] = [
          {
            ...ficheDePaie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFicheDePaieToCollectionIfMissing(ficheDePaieCollection, ficheDePaie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FicheDePaie to an array that doesn't contain it", () => {
        const ficheDePaie: IFicheDePaie = sampleWithRequiredData;
        const ficheDePaieCollection: IFicheDePaie[] = [sampleWithPartialData];
        expectedResult = service.addFicheDePaieToCollectionIfMissing(ficheDePaieCollection, ficheDePaie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ficheDePaie);
      });

      it('should add only unique FicheDePaie to an array', () => {
        const ficheDePaieArray: IFicheDePaie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ficheDePaieCollection: IFicheDePaie[] = [sampleWithRequiredData];
        expectedResult = service.addFicheDePaieToCollectionIfMissing(ficheDePaieCollection, ...ficheDePaieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ficheDePaie: IFicheDePaie = sampleWithRequiredData;
        const ficheDePaie2: IFicheDePaie = sampleWithPartialData;
        expectedResult = service.addFicheDePaieToCollectionIfMissing([], ficheDePaie, ficheDePaie2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ficheDePaie);
        expect(expectedResult).toContain(ficheDePaie2);
      });

      it('should accept null and undefined values', () => {
        const ficheDePaie: IFicheDePaie = sampleWithRequiredData;
        expectedResult = service.addFicheDePaieToCollectionIfMissing([], null, ficheDePaie, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ficheDePaie);
      });

      it('should return initial array if no FicheDePaie is added', () => {
        const ficheDePaieCollection: IFicheDePaie[] = [sampleWithRequiredData];
        expectedResult = service.addFicheDePaieToCollectionIfMissing(ficheDePaieCollection, undefined, null);
        expect(expectedResult).toEqual(ficheDePaieCollection);
      });
    });

    describe('compareFicheDePaie', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFicheDePaie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFicheDePaie(entity1, entity2);
        const compareResult2 = service.compareFicheDePaie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFicheDePaie(entity1, entity2);
        const compareResult2 = service.compareFicheDePaie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFicheDePaie(entity1, entity2);
        const compareResult2 = service.compareFicheDePaie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
