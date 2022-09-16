import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConventionCollective } from '../convention-collective.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../convention-collective.test-samples';

import { ConventionCollectiveService } from './convention-collective.service';

const requireRestSample: IConventionCollective = {
  ...sampleWithRequiredData,
};

describe('ConventionCollective Service', () => {
  let service: ConventionCollectiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IConventionCollective | IConventionCollective[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConventionCollectiveService);
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

    it('should create a ConventionCollective', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const conventionCollective = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(conventionCollective).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConventionCollective', () => {
      const conventionCollective = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(conventionCollective).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConventionCollective', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConventionCollective', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConventionCollective', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConventionCollectiveToCollectionIfMissing', () => {
      it('should add a ConventionCollective to an empty array', () => {
        const conventionCollective: IConventionCollective = sampleWithRequiredData;
        expectedResult = service.addConventionCollectiveToCollectionIfMissing([], conventionCollective);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conventionCollective);
      });

      it('should not add a ConventionCollective to an array that contains it', () => {
        const conventionCollective: IConventionCollective = sampleWithRequiredData;
        const conventionCollectiveCollection: IConventionCollective[] = [
          {
            ...conventionCollective,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConventionCollectiveToCollectionIfMissing(conventionCollectiveCollection, conventionCollective);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConventionCollective to an array that doesn't contain it", () => {
        const conventionCollective: IConventionCollective = sampleWithRequiredData;
        const conventionCollectiveCollection: IConventionCollective[] = [sampleWithPartialData];
        expectedResult = service.addConventionCollectiveToCollectionIfMissing(conventionCollectiveCollection, conventionCollective);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conventionCollective);
      });

      it('should add only unique ConventionCollective to an array', () => {
        const conventionCollectiveArray: IConventionCollective[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const conventionCollectiveCollection: IConventionCollective[] = [sampleWithRequiredData];
        expectedResult = service.addConventionCollectiveToCollectionIfMissing(conventionCollectiveCollection, ...conventionCollectiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conventionCollective: IConventionCollective = sampleWithRequiredData;
        const conventionCollective2: IConventionCollective = sampleWithPartialData;
        expectedResult = service.addConventionCollectiveToCollectionIfMissing([], conventionCollective, conventionCollective2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conventionCollective);
        expect(expectedResult).toContain(conventionCollective2);
      });

      it('should accept null and undefined values', () => {
        const conventionCollective: IConventionCollective = sampleWithRequiredData;
        expectedResult = service.addConventionCollectiveToCollectionIfMissing([], null, conventionCollective, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conventionCollective);
      });

      it('should return initial array if no ConventionCollective is added', () => {
        const conventionCollectiveCollection: IConventionCollective[] = [sampleWithRequiredData];
        expectedResult = service.addConventionCollectiveToCollectionIfMissing(conventionCollectiveCollection, undefined, null);
        expect(expectedResult).toEqual(conventionCollectiveCollection);
      });
    });

    describe('compareConventionCollective', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConventionCollective(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConventionCollective(entity1, entity2);
        const compareResult2 = service.compareConventionCollective(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConventionCollective(entity1, entity2);
        const compareResult2 = service.compareConventionCollective(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConventionCollective(entity1, entity2);
        const compareResult2 = service.compareConventionCollective(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
