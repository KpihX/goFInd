import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Objet from './objet';
import ObjetDetail from './objet-detail';
import ObjetUpdate from './objet-update';
import ObjetDeleteDialog from './objet-delete-dialog';

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
