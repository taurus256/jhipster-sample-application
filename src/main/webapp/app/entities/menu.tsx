import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/dv-user">
        <Translate contentKey="global.menu.entities.dvUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dv-task">
        <Translate contentKey="global.menu.entities.dvTask" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dv-job">
        <Translate contentKey="global.menu.entities.dvJob" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
