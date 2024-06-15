import { IPiece } from 'app/shared/model/piece.model'; //IPiece from './piece.model'

export interface IMaison {
  id?: number;
  adresse?: string;
  description?: string;
  image?: string;
  proprietaire?: any;
  signalant?: any;
  pieces?: IPiece;
  prix?: number;
}

export const defaultValue: Readonly<IMaison> = {};
