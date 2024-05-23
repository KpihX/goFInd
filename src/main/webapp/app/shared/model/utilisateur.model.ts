import { IUser } from 'app/shared/model/user.model';
import { ITrajet } from 'app/shared/model/trajet.model';

export interface IUtilisateur {
  id?: number;
  telephone?: string;
  login?: IUser | null;
  trajets?: ITrajet[] | null;
}

export const defaultValue: Readonly<IUtilisateur> = {};
