import { IMaison } from 'app/shared/model/maison.model';
import { ILocation } from 'app/shared/model/location.model';
import { EtatPiece } from 'app/shared/model/enumerations/etat-piece.model';

export interface IPiece {
  id?: number;
  libelle?: string;
  image?: string | null;
  etat?: keyof typeof EtatPiece | null;
  prix?: number;
  maison?: IMaison | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IPiece> = {};
