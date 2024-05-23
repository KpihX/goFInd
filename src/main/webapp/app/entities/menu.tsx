import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/utilisateur">
        <Translate contentKey="global.menu.entities.utilisateur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/objet">
        <Translate contentKey="global.menu.entities.objet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trajet">
        <Translate contentKey="global.menu.entities.trajet" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/maison">
        <Translate contentKey="global.menu.entities.maison" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/piece">
        <Translate contentKey="global.menu.entities.piece" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/location">
        <Translate contentKey="global.menu.entities.location" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
