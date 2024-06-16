import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './piece.reducer';

export const PieceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pieceEntity = useAppSelector(state => state.piece.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pieceDetailsHeading">
          <Translate contentKey="goFindApp.piece.detail.title">Piece</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pieceEntity.id}</dd>
          <dt>
            <span id="libelle">
              <Translate contentKey="goFindApp.piece.libelle">Libelle</Translate>
            </span>
          </dt>
          <dd>{pieceEntity.libelle}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="goFindApp.piece.image">Image</Translate>
            </span>
          </dt>
          <dd>{pieceEntity.image}</dd>
          <dt>
            <span id="etat">
              <Translate contentKey="goFindApp.piece.etat">Etat</Translate>
            </span>
          </dt>
          <dd>{pieceEntity.etat}</dd>
          <dt>
            <span id="prix">
              Prix
              {/* <Translate contentKey="goFindApp.location.prix">Prix</Translate> */}
            </span>
          </dt>
          <dd>{pieceEntity.prix}</dd>
          <dt>
            <Translate contentKey="goFindApp.piece.maison">Maison</Translate>
          </dt>
          <dd>{pieceEntity.maison ? pieceEntity.maison.id : ''}</dd>
          <dt>
            <Translate contentKey="goFindApp.piece.location">Location</Translate>
          </dt>
          <dd>{pieceEntity.location ? pieceEntity.location.id : ''}</dd>
        </dl>
        <Button tag={Link} to={-1} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/piece/${pieceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PieceDetail;
