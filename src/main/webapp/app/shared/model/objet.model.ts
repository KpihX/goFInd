import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { TypeObjet } from 'app/shared/model/enumerations/type-objet.model';
import { EtatObjet } from 'app/shared/model/enumerations/etat-objet.model';

export interface IObjet {
  id?: number;
  libelle?: string;
  description?: string | null;
  type?: keyof typeof TypeObjet;
  image?: string | null;
  identifiant?: string;
  etat?: keyof typeof EtatObjet;
  proprietaire?: IUtilisateur | null;
}

export const defaultValue: Readonly<IObjet> = {};
