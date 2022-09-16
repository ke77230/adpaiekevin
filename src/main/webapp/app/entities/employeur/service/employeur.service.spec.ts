import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeur } from '../employeur.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employeur.test-samples';

import { EmployeurService } from './employeur.service';

const requireRestSample: IEmployeur = {
  ...sampleWithRequiredData,
};

describe('Employeur Service', () => {
  let service: EmployeurService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeur | IEmployeur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeurService);
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

    it('should create a Employeur', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeur = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Employeur', () => {
      const employeur = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeur).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Employeur', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Employeur', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Employeur', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeurToCollectionIfMissing', () => {
      it('should add a Employeur to an empty array', () => {
        const employeur: IEmployeur = sampleWithRequiredData;
        expectedResult = service.addEmployeurToCollectionIfMissing([], employeur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeur);
      });

      it('should not add a Employeur to an array that contains it', () => {
        const employeur: IEmployeur = sampleWithRequiredData;
        const employeurCollection: IEmployeur[] = [
          {
            ...employeur,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeurToCollectionIfMissing(employeurCollection, employeur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Employeur to an array that doesn't contain it", () => {
        const employeur: IEmployeur = sampleWithRequiredData;
        const employeurCollection: IEmployeur[] = [sampleWithPartialData];
        expectedResult = service.addEmployeurToCollectionIfMissing(employeurCollection, employeur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeur);
      });

      it('should add only unique Employeur to an array', () => {
        const employeurArray: IEmployeur[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeurCollection: IEmployeur[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeurToCollectionIfMissing(employeurCollection, ...employeurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeur: IEmployeur = sampleWithRequiredData;
        const employeur2: IEmployeur = sampleWithPartialData;
        expectedResult = service.addEmployeurToCollectionIfMissing([], employeur, employeur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeur);
        expect(expectedResult).toContain(employeur2);
      });

      it('should accept null and undefined values', () => {
        const employeur: IEmployeur = sampleWithRequiredData;
        expectedResult = service.addEmployeurToCollectionIfMissing([], null, employeur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeur);
      });

      it('should return initial array if no Employeur is added', () => {
        const employeurCollection: IEmployeur[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeurToCollectionIfMissing(employeurCollection, undefined, null);
        expect(expectedResult).toEqual(employeurCollection);
      });
    });

    describe('compareEmployeur', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeur(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeur(entity1, entity2);
        const compareResult2 = service.compareEmployeur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeur(entity1, entity2);
        const compareResult2 = service.compareEmployeur(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeur(entity1, entity2);
        const compareResult2 = service.compareEmployeur(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
