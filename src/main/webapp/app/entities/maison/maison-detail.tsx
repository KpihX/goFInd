import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './maison.reducer';

export const MaisonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, [id]);

  const maisonEntity = useAppSelector(state => state.maison.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="maisonDetailsHeading">
          <Translate contentKey="goFindApp.maison.detail.title">Maison</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{maisonEntity.id}</dd>
          <dt>
            <span id="localisation">
              <Translate contentKey="goFindApp.maison.localisation">Localisation</Translate>
            </span>
          </dt>
          <dd>{maisonEntity.localisation}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="goFindApp.maison.description">Description</Translate>
            </span>
          </dt>
          <dd>{maisonEntity.description}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="goFindApp.maison.image">Image</Translate>
            </span>
          </dt>
          <dd>{maisonEntity.image}</dd>
          <dt>
            <span id="identifiant">
              <Translate contentKey="goFindApp.maison.identifiant">Identifiant</Translate>
            </span>
          </dt>
          <dd>{maisonEntity.identifiant}</dd>
        </dl>
        <Button tag={Link} to="/maison" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/maison/${maisonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MaisonDetail;
