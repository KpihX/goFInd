import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trajet.reducer';

export const TrajetDetail = () => {
  const dispatch = useAppDispatch();

  const account = useAppSelector(state => state.authentication.account);

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const trajetEntity = useAppSelector(state => state.trajet.entity);

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="trajetDetailsHeading">
          <Translate contentKey="goFindApp.trajet.detail.title">Trajet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{trajetEntity.id}</dd>
          <dt>
            <span id="depart">
              <Translate contentKey="goFindApp.trajet.depart">Depart</Translate>
            </span>
          </dt>
          <dd>{trajetEntity.depart}</dd>
          <dt>
            <span id="arrivee">
              <Translate contentKey="goFindApp.trajet.arrivee">Arrivee</Translate>
            </span>
          </dt>
          <dd>{trajetEntity.arrivee}</dd>
          <dt>
            <span id="dateHeureDepart">
              <Translate contentKey="goFindApp.trajet.dateHeureDepart">Date Heure Depart</Translate>
            </span>
          </dt>
          <dd>
            {trajetEntity.dateHeureDepart ? <TextFormat value={trajetEntity.dateHeureDepart} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="places">
              <Translate contentKey="goFindApp.trajet.places">Places</Translate>
            </span>
          </dt>
          <dd>{trajetEntity.places}</dd>
          <dt>
            <span id="prix">
              <Translate contentKey="goFindApp.trajet.prix">Prix</Translate>
            </span>
          </dt>
          <dd>{trajetEntity.prix}</dd>
          <dt>
            <Translate contentKey="goFindApp.trajet.proprietaire">Proprietaire</Translate>
          </dt>

          <dd>{trajetEntity.proprietaire ? trajetEntity.proprietaireId : 'maeva'}</dd>
          <dt>
            <Translate contentKey="goFindApp.trajet.engages">Engages</Translate>
          </dt>
          <dd>
            {trajetEntity.engages
              ? trajetEntity.engages.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {trajetEntity.engages && i === trajetEntity.engages.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/trajet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trajet/${trajetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TrajetDetail;
