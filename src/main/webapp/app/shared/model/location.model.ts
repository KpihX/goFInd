import dayjs from 'dayjs';
import { IMaison } from 'app/shared/model/maison.model';
import { IUtilisateur } from './utilisateur.model';

export interface ILocation {
  id?: number;
  prix?: number;
  maison?: IMaison | null;
  dateHeureDebut?: dayjs.Dayjs;
  dateHeureFin?: dayjs.Dayjs;
  locataire?: IUtilisateur | null;
}

export const defaultValue: Readonly<ILocation> = {};
