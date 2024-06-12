import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Objet from './maison';
import ObjetDetail from './maison-detail';
import ObjetUpdate from './maison-update';
import ObjetDeleteDialog from './maison-delete-dialog';

const ObjetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Objet />} />
    <Route path="new" element={<ObjetUpdate />} />
    <Route path=":id">
      <Route index element={<ObjetDetail />} />
      <Route path="edit" element={<ObjetUpdate />} />
      <Route path="delete" element={<ObjetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ObjetRoutes;
