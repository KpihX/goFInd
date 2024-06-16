import { Link, useLocation, useParams } from 'react-router-dom';
import { Button, Row, Col, Card } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './location.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';
import InfiniteScroll from 'react-infinite-scroll-component';
import Room from './room';

import React, { useState, useEffect } from 'react';
import { getPaginationState, isNumber, translate, ValidatedField, ValidatedForm } from 'react-jhipster';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList, overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { IMaison } from 'app/shared/model/maison.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getEntities } from '../piece/piece.reducer';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';

import { Chip, Stack } from '@mui/material';
import { APP_LOCAL_DATETIME_FORMAT } from 'app/config/constants';
import dayjs from 'dayjs';

import { createEntity as createLocation } from '../location/location.reducer';
import { toast } from 'react-toastify';
import { updateEntity as updatePiece } from '../piece/piece.reducer';

export const LocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const pageLocation = useLocation();

  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const maisonEntity = useAppSelector(state => state.maison.entity);
  const loading = useAppSelector(state => state.maison.loading);
  const updating = useAppSelector(state => state.maison.updating);
  const updateSuccess = useAppSelector(state => state.maison.updateSuccess);
  const updateSuccessLoc = useAppSelector(state => state.location.updateSuccess);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const loadingPieces = useAppSelector(state => state.piece.loading);
  const links = useAppSelector(state => state.piece.links);
  const updateSuccessPieces = useAppSelector(state => state.piece.updateSuccess);
  const [selectedPieces, setSelectedPieces] = React.useState([]);
  const [total, setTotal] = React.useState(0);
  const maisonList = useAppSelector(state => state.maison.entities);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const locationEntity = useAppSelector(state => state.location.entity);

  // useEffect(() => {
  //   if (updateSuccessPieces) {
  //     resetAll();
  //   }
  // }, [updateSuccessPieces]);

  // useEffect(() => {
  //   getAllEntities();
  // }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      // getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    // dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    // resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  // const handleClose = () => {
  //   navigate('/houses');
  // };

  return (
    <>
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
            <dd>{locationEntity ? locationEntity.id : ''}</dd>
            <dt>
              Locataire
              {/* <Translate contentKey="goFindApp.location.maison">Maison</Translate> */}
            </dt>
            <dd>{locationEntity.locataire ? locationEntity.locataire.id : ''}</dd>
          </dl>
          &nbsp;
          {/* <Button tag={Link} to={`/location/${locationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button> */}
        </Col>
      </Row>

      <Row className="justify-content-center">
        <Card className="jh-card">
          <h3 id="goFindApp.maison.home.createOrEditLabel" data-cy="MaisonCreateUpdateHeading">
            <p>Voici les pièces concernées</p>
            {/* <Translate contentKey="goFindApp.maison.home.createOrEditLabel">Create or edit a Maison</Translate> */}
          </h3>
        </Card>
        <InfiniteScroll
          dataLength={locationEntity.pieces ? locationEntity.pieces.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {locationEntity.pieces && locationEntity.pieces.length > 0 ? (
            <div className="houses-grid">
              {locationEntity.pieces.map((objet, i) => (
                <Room key={`entity-${i}`} id={objet.id} libelle={objet.libelle} image={objet.image} prix={objet.prix} />
              ))}
            </div>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                Aucune pièce enrégistrée actuellement!
                {/* <Translate contentKey="goFindApp.objet.home.notFound">No Objets found</Translate> */}
              </div>
            )
          )}
        </InfiniteScroll>
      </Row>
      <Row>
        <Button tag={Link} to="/locations" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
      </Row>
    </>
  );
};

export default LocationDetail;
