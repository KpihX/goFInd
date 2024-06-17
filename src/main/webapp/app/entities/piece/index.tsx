import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Piece from './piece';
import PieceDetail from './piece-detail';
import PieceUpdate from './piece-update';
import PieceDeleteDialog from './piece-delete-dialog';

const PieceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Piece />} />
    <Route path="new/:maisonId" element={<PieceUpdate />} />
    <Route path=":id">
      <Route index element={<PieceDetail />} />
      <Route path="edit" element={<PieceUpdate />} />
      <Route path="delete" element={<PieceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PieceRoutes;
