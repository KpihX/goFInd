import { IPiece } from 'app/shared/model/piece.model'; //IPiece from './piece.model'
import {ILocation} from 'app/shared/model/location.model';

export interface IMaison {
  id?: number;
  adresse?: string;
  description?: string;
  image?: string;
  proprietaire?: any;
  signalant?: any;
  pieces?: IPiece;
  location?: ILocation;
}

export const defaultValue: Readonly<IMaison> = {};
