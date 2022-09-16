import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IConge } from '../conge.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../conge.test-samples';

import { CongeService, RestConge } from './conge.service';

const requireRestSample: RestConge = {
  ...sampleWithRequiredData,
  holdateStart: sampleWithRequiredData.holdateStart?.format(DATE_FORMAT),
  holdateEnd: sampleWithRequiredData.holdateEnd?.format(DATE_FORMAT),
  dateDemande: sampleWithRequiredData.dateDemande?.format(DATE_FORMAT),
  dateReponse: sampleWithRequiredData.dateReponse?.format(DATE_FORMAT),
};

describe('Conge Service', () => {
  let service: CongeService;
  let httpMock: HttpTestingController;
  let expectedResult: IConge | IConge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CongeService);
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

    it('should create a Conge', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const conge = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(conge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Conge', () => {
      const conge = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(conge).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Conge', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Conge', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Conge', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCongeToCollectionIfMissing', () => {
      it('should add a Conge to an empty array', () => {
        const conge: IConge = sampleWithRequiredData;
        expectedResult = service.addCongeToCollectionIfMissing([], conge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conge);
      });

      it('should not add a Conge to an array that contains it', () => {
        const conge: IConge = sampleWithRequiredData;
        const congeCollection: IConge[] = [
          {
            ...conge,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCongeToCollectionIfMissing(congeCollection, conge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Conge to an array that doesn't contain it", () => {
        const conge: IConge = sampleWithRequiredData;
        const congeCollection: IConge[] = [sampleWithPartialData];
        expectedResult = service.addCongeToCollectionIfMissing(congeCollection, conge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conge);
      });

      it('should add only unique Conge to an array', () => {
        const congeArray: IConge[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const congeCollection: IConge[] = [sampleWithRequiredData];
        expectedResult = service.addCongeToCollectionIfMissing(congeCollection, ...congeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conge: IConge = sampleWithRequiredData;
        const conge2: IConge = sampleWithPartialData;
        expectedResult = service.addCongeToCollectionIfMissing([], conge, conge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conge);
        expect(expectedResult).toContain(conge2);
      });

      it('should accept null and undefined values', () => {
        const conge: IConge = sampleWithRequiredData;
        expectedResult = service.addCongeToCollectionIfMissing([], null, conge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conge);
      });

      it('should return initial array if no Conge is added', () => {
        const congeCollection: IConge[] = [sampleWithRequiredData];
        expectedResult = service.addCongeToCollectionIfMissing(congeCollection, undefined, null);
        expect(expectedResult).toEqual(congeCollection);
      });
    });

    describe('compareConge', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConge(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConge(entity1, entity2);
        const compareResult2 = service.compareConge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConge(entity1, entity2);
        const compareResult2 = service.compareConge(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConge(entity1, entity2);
        const compareResult2 = service.compareConge(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
