import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICotisation } from '../cotisation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cotisation.test-samples';

import { CotisationService, RestCotisation } from './cotisation.service';

const requireRestSample: RestCotisation = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.format(DATE_FORMAT),
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('Cotisation Service', () => {
  let service: CotisationService;
  let httpMock: HttpTestingController;
  let expectedResult: ICotisation | ICotisation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CotisationService);
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

    it('should create a Cotisation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cotisation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cotisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cotisation', () => {
      const cotisation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cotisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cotisation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cotisation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cotisation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCotisationToCollectionIfMissing', () => {
      it('should add a Cotisation to an empty array', () => {
        const cotisation: ICotisation = sampleWithRequiredData;
        expectedResult = service.addCotisationToCollectionIfMissing([], cotisation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cotisation);
      });

      it('should not add a Cotisation to an array that contains it', () => {
        const cotisation: ICotisation = sampleWithRequiredData;
        const cotisationCollection: ICotisation[] = [
          {
            ...cotisation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCotisationToCollectionIfMissing(cotisationCollection, cotisation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cotisation to an array that doesn't contain it", () => {
        const cotisation: ICotisation = sampleWithRequiredData;
        const cotisationCollection: ICotisation[] = [sampleWithPartialData];
        expectedResult = service.addCotisationToCollectionIfMissing(cotisationCollection, cotisation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cotisation);
      });

      it('should add only unique Cotisation to an array', () => {
        const cotisationArray: ICotisation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cotisationCollection: ICotisation[] = [sampleWithRequiredData];
        expectedResult = service.addCotisationToCollectionIfMissing(cotisationCollection, ...cotisationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cotisation: ICotisation = sampleWithRequiredData;
        const cotisation2: ICotisation = sampleWithPartialData;
        expectedResult = service.addCotisationToCollectionIfMissing([], cotisation, cotisation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cotisation);
        expect(expectedResult).toContain(cotisation2);
      });

      it('should accept null and undefined values', () => {
        const cotisation: ICotisation = sampleWithRequiredData;
        expectedResult = service.addCotisationToCollectionIfMissing([], null, cotisation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cotisation);
      });

      it('should return initial array if no Cotisation is added', () => {
        const cotisationCollection: ICotisation[] = [sampleWithRequiredData];
        expectedResult = service.addCotisationToCollectionIfMissing(cotisationCollection, undefined, null);
        expect(expectedResult).toEqual(cotisationCollection);
      });
    });

    describe('compareCotisation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCotisation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCotisation(entity1, entity2);
        const compareResult2 = service.compareCotisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCotisation(entity1, entity2);
        const compareResult2 = service.compareCotisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCotisation(entity1, entity2);
        const compareResult2 = service.compareCotisation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
