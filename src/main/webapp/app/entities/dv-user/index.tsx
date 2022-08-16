import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DVUser from './dv-user';
import DVUserDetail from './dv-user-detail';
import DVUserUpdate from './dv-user-update';
import DVUserDeleteDialog from './dv-user-delete-dialog';

const DVUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DVUser />} />
    <Route path="new" element={<DVUserUpdate />} />
    <Route path=":id">
      <Route index element={<DVUserDetail />} />
      <Route path="edit" element={<DVUserUpdate />} />
      <Route path="delete" element={<DVUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DVUserRoutes;
