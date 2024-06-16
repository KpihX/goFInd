import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './location.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';

export const LocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const locationEntity = useAppSelector(state => state.location.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="locationDetailsHeading">
          <Translate contentKey="goFindApp.location.detail.title">Location</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{locationEntity.id}</dd>
          <dt>
            <span id="prix">
              <Translate contentKey="goFindApp.location.prix">Prix</Translate>
            </span>
          </dt>
          <dd>{locationEntity.prix}</dd>
          <dt>
            <span id="dateHeureDebut">
              Date et heure de début
              {/* <Translate contentKey="goFindApp.trajet.dateHeureDepart">Date Heure Depart</Translate> */}
            </span>
          </dt>
          <dd>
            {locationEntity.dateHeureDebut ? (
              <TextFormat value={locationEntity.dateHeureDebut} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateHeureDebut">
              Date et heure de Fin
              {/* <Translate contentKey="goFindApp.trajet.dateHeureDepart">Date Heure Depart</Translate> */}
            </span>
          </dt>
          <dd>
            {locationEntity.dateHeureFin ? <TextFormat value={locationEntity.dateHeureFin} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="goFindApp.location.maison">Maison</Translate>
          </dt>
          <dd>{locationEntity.maison ? locationEntity.maison.id : ''}</dd>
          <dt>
            Locataire
            {/* <Translate contentKey="goFindApp.location.maison">Maison</Translate> */}
          </dt>
          <dd>{locationEntity.locataire ? locationEntity.locataire.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/location" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/location/${locationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocationDetail;
