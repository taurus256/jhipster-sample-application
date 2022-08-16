import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DVJob from './dv-job';
import DVJobDetail from './dv-job-detail';
import DVJobUpdate from './dv-job-update';
import DVJobDeleteDialog from './dv-job-delete-dialog';

const DVJobRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DVJob />} />
    <Route path="new" element={<DVJobUpdate />} />
    <Route path=":id">
      <Route index element={<DVJobDetail />} />
      <Route path="edit" element={<DVJobUpdate />} />
      <Route path="delete" element={<DVJobDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DVJobRoutes;
