import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './utilisateur.reducer';

export const UtilisateurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const utilisateurEntity = useAppSelector(state => state.utilisateur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="utilisateurDetailsHeading">
          <Translate contentKey="goFindApp.utilisateur.detail.title">Utilisateur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.id}</dd>
          <dt>
            <span id="telephone">
              <Translate contentKey="goFindApp.utilisateur.telephone">Telephone</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.telephone}</dd>
          <dt>
            <Translate contentKey="goFindApp.utilisateur.login">Login</Translate>
          </dt>
          <dd>{utilisateurEntity.login ? utilisateurEntity.login.login : ''}</dd>
          <dt>
            <Translate contentKey="goFindApp.utilisateur.trajets">Trajets</Translate>
          </dt>
          <dd>
            {utilisateurEntity.trajets
              ? utilisateurEntity.trajets.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {utilisateurEntity.trajets && i === utilisateurEntity.trajets.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/utilisateur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/utilisateur/${utilisateurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UtilisateurDetail;
