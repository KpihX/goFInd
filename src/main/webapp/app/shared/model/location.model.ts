import { IMaison } from 'app/shared/model/maison.model';

export interface ILocation {
  id?: number;
  prix?: number;
  maison?: IMaison | null;
}

export const defaultValue: Readonly<ILocation> = {};
