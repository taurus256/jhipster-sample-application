import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DVTask from './dv-task';
import DVTaskDetail from './dv-task-detail';
import DVTaskUpdate from './dv-task-update';
import DVTaskDeleteDialog from './dv-task-delete-dialog';

const DVTaskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DVTask />} />
    <Route path="new" element={<DVTaskUpdate />} />
    <Route path=":id">
      <Route index element={<DVTaskDetail />} />
      <Route path="edit" element={<DVTaskUpdate />} />
      <Route path="delete" element={<DVTaskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DVTaskRoutes;
