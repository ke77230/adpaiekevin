import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITauxDImposition } from '../taux-d-imposition.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../taux-d-imposition.test-samples';

import { TauxDImpositionService, RestTauxDImposition } from './taux-d-imposition.service';

const requireRestSample: RestTauxDImposition = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('TauxDImposition Service', () => {
  let service: TauxDImpositionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITauxDImposition | ITauxDImposition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TauxDImpositionService);
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

    it('should create a TauxDImposition', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tauxDImposition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tauxDImposition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TauxDImposition', () => {
      const tauxDImposition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tauxDImposition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TauxDImposition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TauxDImposition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TauxDImposition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTauxDImpositionToCollectionIfMissing', () => {
      it('should add a TauxDImposition to an empty array', () => {
        const tauxDImposition: ITauxDImposition = sampleWithRequiredData;
        expectedResult = service.addTauxDImpositionToCollectionIfMissing([], tauxDImposition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tauxDImposition);
      });

      it('should not add a TauxDImposition to an array that contains it', () => {
        const tauxDImposition: ITauxDImposition = sampleWithRequiredData;
        const tauxDImpositionCollection: ITauxDImposition[] = [
          {
            ...tauxDImposition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTauxDImpositionToCollectionIfMissing(tauxDImpositionCollection, tauxDImposition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TauxDImposition to an array that doesn't contain it", () => {
        const tauxDImposition: ITauxDImposition = sampleWithRequiredData;
        const tauxDImpositionCollection: ITauxDImposition[] = [sampleWithPartialData];
        expectedResult = service.addTauxDImpositionToCollectionIfMissing(tauxDImpositionCollection, tauxDImposition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tauxDImposition);
      });

      it('should add only unique TauxDImposition to an array', () => {
        const tauxDImpositionArray: ITauxDImposition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tauxDImpositionCollection: ITauxDImposition[] = [sampleWithRequiredData];
        expectedResult = service.addTauxDImpositionToCollectionIfMissing(tauxDImpositionCollection, ...tauxDImpositionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tauxDImposition: ITauxDImposition = sampleWithRequiredData;
        const tauxDImposition2: ITauxDImposition = sampleWithPartialData;
        expectedResult = service.addTauxDImpositionToCollectionIfMissing([], tauxDImposition, tauxDImposition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tauxDImposition);
        expect(expectedResult).toContain(tauxDImposition2);
      });

      it('should accept null and undefined values', () => {
        const tauxDImposition: ITauxDImposition = sampleWithRequiredData;
        expectedResult = service.addTauxDImpositionToCollectionIfMissing([], null, tauxDImposition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tauxDImposition);
      });

      it('should return initial array if no TauxDImposition is added', () => {
        const tauxDImpositionCollection: ITauxDImposition[] = [sampleWithRequiredData];
        expectedResult = service.addTauxDImpositionToCollectionIfMissing(tauxDImpositionCollection, undefined, null);
        expect(expectedResult).toEqual(tauxDImpositionCollection);
      });
    });

    describe('compareTauxDImposition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTauxDImposition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTauxDImposition(entity1, entity2);
        const compareResult2 = service.compareTauxDImposition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTauxDImposition(entity1, entity2);
        const compareResult2 = service.compareTauxDImposition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTauxDImposition(entity1, entity2);
        const compareResult2 = service.compareTauxDImposition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
