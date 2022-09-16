import { IMention, NewMention } from './mention.model';

export const sampleWithRequiredData: IMention = {
  id: 84657,
  mention: 'tolerance Practical',
};

export const sampleWithPartialData: IMention = {
  id: 23658,
  mention: 'la Plastic',
};

export const sampleWithFullData: IMention = {
  id: 26001,
  mention: 'Assistant Ergonomic metrics',
};

export const sampleWithNewData: NewMention = {
  mention: 'facilitate SAS b',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
