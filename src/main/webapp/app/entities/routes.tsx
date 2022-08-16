import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DVUser from './dv-user';
import DVTask from './dv-task';
import DVJob from './dv-job';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="dv-user/*" element={<DVUser />} />
        <Route path="dv-task/*" element={<DVTask />} />
        <Route path="dv-job/*" element={<DVJob />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
