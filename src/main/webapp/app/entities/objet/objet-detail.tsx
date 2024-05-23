import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './objet.reducer';

export const ObjetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const objetEntity = useAppSelector(state => state.objet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="objetDetailsHeading">
          <Translate contentKey="goFindApp.objet.detail.title">Objet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{objetEntity.id}</dd>
          <dt>
            <span id="libelle">
              <Translate contentKey="goFindApp.objet.libelle">Libelle</Translate>
            </span>
          </dt>
          <dd>{objetEntity.libelle}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="goFindApp.objet.description">Description</Translate>
            </span>
          </dt>
          <dd>{objetEntity.description}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="goFindApp.objet.type">Type</Translate>
            </span>
          </dt>
          <dd>{objetEntity.type}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="goFindApp.objet.image">Image</Translate>
            </span>
          </dt>
          <dd>{objetEntity.image}</dd>
          <dt>
            <span id="identifiant">
              <Translate contentKey="goFindApp.objet.identifiant">Identifiant</Translate>
            </span>
          </dt>
          <dd>{objetEntity.identifiant}</dd>
          <dt>
            <span id="etat">
              <Translate contentKey="goFindApp.objet.etat">Etat</Translate>
            </span>
          </dt>
          <dd>{objetEntity.etat}</dd>
          <dt>
            <Translate contentKey="goFindApp.objet.proprietaire">Proprietaire</Translate>
          </dt>
          <dd>{objetEntity.proprietaire ? objetEntity.proprietaire.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/objet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/objet/${objetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ObjetDetail;
