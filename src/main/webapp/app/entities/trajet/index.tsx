import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trajet from './trajet';
import TrajetDetail from './trajet-detail';
import TrajetUpdate from './trajet-update';
import TrajetDeleteDialog from './trajet-delete-dialog';

const TrajetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Trajet />} />
    <Route path="new" element={<TrajetUpdate />} />
    <Route path=":id">
      <Route index element={<TrajetDetail />} />
      <Route path="edit" element={<TrajetUpdate />} />
      <Route path="delete" element={<TrajetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TrajetRoutes;
