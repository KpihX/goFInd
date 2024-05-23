import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Utilisateur from './utilisateur';
import UtilisateurDetail from './utilisateur-detail';
import UtilisateurUpdate from './utilisateur-update';
import UtilisateurDeleteDialog from './utilisateur-delete-dialog';

const UtilisateurRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Utilisateur />} />
    <Route path="new" element={<UtilisateurUpdate />} />
    <Route path=":id">
      <Route index element={<UtilisateurDetail />} />
      <Route path="edit" element={<UtilisateurUpdate />} />
      <Route path="delete" element={<UtilisateurDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UtilisateurRoutes;
