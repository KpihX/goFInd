import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">GoFind</Translate>
    </span>
    <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Objects = () => (
  <NavItem>
    <NavLink tag={Link} to="/objects" className="d-flex align-items-center">
      {/* <FontAwesomeIcon icon="home" /> */}
      <span>
        <Translate contentKey="global.menu.objects">Objects</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Itinaries = () => (
  <NavItem>
    <NavLink tag={Link} to="/itinaries" className="d-flex align-items-center">
      {/* <FontAwesomeIcon icon="home" /> */}
      <span>
        <Translate contentKey="global.menu.itinaries">Itinaries</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Houses = () => (
  <NavItem>
    <NavLink tag={Link} to="/houses" className="d-flex align-items-center">
      {/* <FontAwesomeIcon icon="home" /> */}
      <span>
        <Translate contentKey="global.menu.houses">Houses</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Locations = () => (
  <NavItem>
    <NavLink tag={Link} to="/locations" className="d-flex align-items-center">
      {/* <FontAwesomeIcon icon="home" /> */}
      <span>
        <Translate contentKey="global.menu.locations">Locations</Translate>
      </span>
    </NavLink>
  </NavItem>
);
