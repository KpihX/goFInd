import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IMaison {
  id?: number;
  adresse?: string;
  description?: string | null;
  image?: string | null;
  proprietaire?: IUtilisateur | null;
}

export const defaultValue: Readonly<IMaison> = {};
