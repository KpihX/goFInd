import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Maison from './maison';
import MaisonDetail from './maison-detail';
import MaisonUpdate from './maison-update';
import MaisonDeleteDialog from './maison-delete-dialog';

const MaisonRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Maison />} />
    <Route path="new" element={<MaisonUpdate />} />
    <Route path=":id">
      <Route index element={<MaisonDetail />} />
      <Route path="edit/:locataireId" element={<MaisonUpdate />} />
      <Route path="delete" element={<MaisonDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MaisonRoutes;
