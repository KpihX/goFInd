import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface ITrajet {
  id?: number;
  depart?: string;
  arrivee?: string;
  dateHeureDepart?: dayjs.Dayjs;
  places?: number;
  prix?: number;
  proprietaire?: IUtilisateur | null;
  engages?: IUtilisateur[] | null;
}

export const defaultValue: Readonly<ITrajet> = {};
