import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 91847,
  streetName: "Serge d'Argenteuil",
  numeroRue: 'a capacitor',
  postalCode: 'monetize indigo',
  city: 'Rémichester',
  stateProvince: 'Computer Computer',
};

export const sampleWithPartialData: ILocation = {
  id: 82461,
  streetName: 'Roy Laffitte',
  numeroRue: 'Checking',
  postalCode: 'redundant Plastic panel',
  city: 'West Élisabethton',
  stateProvince: 'Shoes',
};

export const sampleWithFullData: ILocation = {
  id: 67336,
  streetName: 'Perez du Havre',
  numeroRue: 'Upgradable',
  postalCode: 'JBOD redundant',
  city: 'Vitry-sur-Seine',
  stateProvince: 'Salad Books capacitor',
};

export const sampleWithNewData: NewLocation = {
  streetName: 'Robert Saint-Denis',
  numeroRue: 'Cambridgeshire up Metal',
  postalCode: 'Gloves 1080p',
  city: 'Marseille',
  stateProvince: 'Shoes Jewelery',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
