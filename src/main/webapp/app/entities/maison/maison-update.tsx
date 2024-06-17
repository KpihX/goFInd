import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
<<<<<<< HEAD
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

=======
import { getPaginationState, isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList, overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
>>>>>>> origin
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { IMaison } from 'app/shared/model/maison.model';
import { getEntity, updateEntity, createEntity, reset } from './maison.reducer';

import { Card } from 'reactstrap';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getEntities } from '../piece/piece.reducer';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import InfiniteScroll from 'react-infinite-scroll-component';
import Room from 'app/modules/houses/room';
import { Chip, Stack } from '@mui/material';
import { APP_LOCAL_DATETIME_FORMAT } from 'app/config/constants';
import dayjs from 'dayjs';
import { getEntities as getMaisons } from './maison.reducer';
import { createEntity as createLocation } from '../location/location.reducer';
import { toast } from 'react-toastify';
import { updateEntity as updatePiece } from '../piece/piece.reducer';

export const MaisonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const { locataireId } = useParams<'locataireId'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const maisonEntity = useAppSelector(state => state.maison.entity);
  const loading = useAppSelector(state => state.maison.loading);
  const updating = useAppSelector(state => state.maison.updating);
  const updateSuccess = useAppSelector(state => state.maison.updateSuccess);
  const updateSuccessLoc = useAppSelector(state => state.location.updateSuccess);

  const account = useAppSelector(state => state.authentication.account);

  const pageLocation = useLocation();

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
  const locationEntity = useAppSelector(state => state.location.entity);

  useEffect(() => {
    resetAll();
  }, []);

  // eslint-disable-next-line complexity
  const handleRent = values => {
    console.log('* valuesLoc:', values);

    const differenceInMonths = dayjs(values.dateHeureFin).diff(dayjs(values.dateHeureDebut), 'month', true);
    const montantTotal = total * differenceInMonths;

    values.dateHeureDebut = convertDateTimeToServer(values.dateHeureDebut);
    values.dateHeureFin = convertDateTimeToServer(values.dateHeureFin);

    const locataire = utilisateurs.find(it => it?.loginId?.toString() === account?.id?.toString());
    const maison = maisonList.find(it => it?.id?.toString() === id?.toString());

    console.log('* maisonList: ', maisonList);
    console.log('* maisonId: ', id);

    const entity = {
      ...values,
      maison,
      maisonId: maison?.id,
      pieces: selectedPieces,
      locataire,
      locataireId: locataire?.id,
      prix: montantTotal,
    };

    toast.success("Le coût total s'élève à : " + montantTotal + '!');

    console.log('* location:', entity);

    dispatch(createLocation(entity));
  };

  useEffect(() => {
    if (updateSuccessLoc) {
      let finalPieces = [];
      let piece;
      for (let i = 0; i < selectedPieces.length; i++) {
        piece = { ...selectedPieces[i], etat: 'LOUE', location: locationEntity, locationId: locationEntity?.id };
        finalPieces = [...finalPieces, piece];
      }

      console.log('* finalPieces', finalPieces);

      for (let i = 0; i < finalPieces.length; i++) {
        dispatch(updatePiece(finalPieces[i]));
      }

      resetAll();
    }
  }, [updateSuccessLoc]);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
    dispatch(getMaisons({}));
  };

  const resetAll = () => {
    setTotal(0);
    setSelectedPieces([]);
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
    dispatch(getMaisons({}));
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUtilisateurs({}));
  };

  useEffect(() => {
    console.log('* locataireId: ', locataireId);
  }, [locataireId]);

  useEffect(() => {
    if (updateSuccessPieces) {
      resetAll();
    }
  }, [updateSuccessPieces]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

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
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
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

  const [imagesPieces, setImagesPieces] = useState<string[]>([]);
  const [prix, setPrix] = useState<number>(0);

  const handleClose = () => {
    navigate('/houses');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUtilisateurs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
<<<<<<< HEAD
    const entity = {
      ...maisonEntity,
      ...values,
      proprietaire: utilisateurs.find(it => it.id.toString() === values.proprietaire.toString()),
=======
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const proprietaire = utilisateurs.find(it => it?.loginId?.toString() === account?.id?.toString());

    const entity = {
      ...maisonEntity,
      ...values,
      proprietaire,
      proprietaireId: proprietaire.id,
>>>>>>> origin
    };

    if (isNew) {
      dispatch(createEntity({ entity, imagesPieces, prix }));
    } else {
      dispatch(updateEntity({ entity, imagesPieces, prix }));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...maisonEntity,
          proprietaire: maisonEntity?.proprietaire?.id,
        };

<<<<<<< HEAD
  const handleAddImagePiece = () => {
    setImagesPieces([...imagesPieces, '']);
  };

  const handleRemoveImagePiece = index => {
    setImagesPieces(imagesPieces.filter((_, i) => i !== index));
  };

  const handleImagePieceChange = (index, value) => {
    const updatedImages = [...imagesPieces];
    updatedImages[index] = value;
    setImagesPieces(updatedImages);
  };
=======
  const defaultValuesRent = () => ({
    dateHeureDebut: displayDefaultDateTime(),
    dateHeureFin: dayjs(displayDefaultDateTime()).add(1, 'month').format(APP_LOCAL_DATETIME_FORMAT),
  });
>>>>>>> origin

  return (
    <div>
      {locataireId === '0' && (
        <>
          <Row className="justify-content-center">
            <Col md="8">
              <h2 id="goFindApp.maison.home.createOrEditLabel" data-cy="MaisonCreateUpdateHeading">
                <Translate contentKey="goFindApp.maison.home.createOrEditLabel">Create or edit a Maison</Translate>
              </h2>
            </Col>
          </Row>
          <Row className="justify-content-center">
            <Col md="8">
              {loading ? (
                <p>Loading...</p>
              ) : (
                <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                  {/* {!isNew ? (
                    <ValidatedField
                      name="id"
                      required
                      readOnly
                      id="maison-id"
                      label={translate('global.field.id')}
                      validate={{ required: true }}
                    />
                  ) : null} */}
                  <ValidatedField
                    label={translate('goFindApp.maison.adresse')}
                    id="maison-adresse"
                    name="adresse"
                    data-cy="adresse"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('goFindApp.maison.description')}
                    id="maison-description"
                    name="description"
                    data-cy="description"
                    type="text"
                  />
                  <ValidatedField label={translate('goFindApp.maison.image')} id="maison-image" name="image" data-cy="image" type="text" />
                  {/* <ValidatedField
                id="maison-proprietaire"
                name="proprietaire"
                data-cy="proprietaire"
                label={translate('goFindApp.maison.proprietaire')}
                type="select"
              >
                <option value="" key="0" />
                {utilisateurs
                  ? utilisateurs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
<<<<<<< HEAD
              </ValidatedField>
              <div>
                <h5>Images des pièces</h5>
                {imagesPieces.map((image, index) => (
                  <div key={index} className="d-flex mb-2">
                    <ValidatedField
                      name={`piece-image-${index}`}
                      label={`Image ${index + 1}`}
                      value={image}
                      onChange={e => handleImagePieceChange(index, e.target.value)}
                      data-cy={`piece-image-${index}`}
                      type="text"
                    />
                    <Button type="button" color="danger" onClick={() => handleRemoveImagePiece(index)} className="ms-2">
                      <FontAwesomeIcon icon="trash" />
                    </Button>
                  </div>
                ))}
                <Button type="button" color="primary" onClick={handleAddImagePiece}>
                  <FontAwesomeIcon icon="plus" /> Ajouter une image de pièce
                </Button>
              </div>
              <ValidatedField
                label={translate('goFindApp.maison.prix')}
                id="maison-prix"
                name="prix"
                data-cy="prix"
                type="number"
                value={prix}
                onChange={e => setPrix(Number(e.target.value))}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/maison" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
=======
              </ValidatedField> */}
                  <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/houses" replace color="info">
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </span>
                  </Button>
                  &nbsp;
                  <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp;
                    <Translate contentKey="entity.action.save">Save</Translate>
                  </Button>
                </ValidatedForm>
              )}
            </Col>
          </Row>
        </>
      )}
      {id && (
        <Row className="justify-content-center">
          <Card className="jh-card">
            <h3 id="goFindApp.maison.home.createOrEditLabel" data-cy="MaisonCreateUpdateHeading">
              {locataireId !== '0' ? <p>Choisissez vos pièces</p> : <p>Gestion des pièces de la maison</p>}
              {/* <Translate contentKey="goFindApp.maison.home.createOrEditLabel">Create or edit a Maison</Translate> */}
              <div className="d-flex justify-content-end">
                <Button className="me-2" color="info" onClick={handleSyncList} disabled={loadingPieces}>
                  <FontAwesomeIcon icon="sync" spin={loading} />{' '}
                  <Translate contentKey="goFindApp.piece.home.refreshListLabel">Refresh List</Translate>
                </Button>
                {locataireId !== '0' ? null : (
                  <Link
                    to={`/piece/new/${id}`}
                    className="btn btn-primary jh-create-entity"
                    id="jh-create-entity"
                    data-cy="entityCreateButton"
                  >
                    <FontAwesomeIcon icon="plus" />
                    {/* <>{console.log("** maisonId: ", id)}</> */}
                    &nbsp;
                    <Translate contentKey="goFindApp.piece.home.createLabel">Create new Piece</Translate>
                  </Link>
                )}
              </div>
            </h3>
          </Card>
          <InfiniteScroll
            dataLength={maisonEntity.pieces ? maisonEntity.pieces.length : 0}
            next={handleLoadMore}
            hasMore={paginationState.activePage - 1 < links.next}
            loader={<div className="loader">Loading ...</div>}
          >
            {maisonEntity.pieces && maisonEntity.pieces.length > 0 ? (
              <div className="houses-grid">
                {maisonEntity.pieces.map((objet, i) => (
                  <Room
                    key={`entity-${i}`}
                    id={objet.id}
                    libelle={objet.libelle}
                    etat={objet.etat}
                    image={objet.image}
                    proprietaire={maisonEntity.proprietaire}
                    locataireId={locataireId}
                    selectedPieces={selectedPieces}
                    setSelectedPieces={setSelectedPieces}
                    prix={objet.prix}
                    pieces={maisonEntity.pieces}
                    total={total}
                    setTotal={setTotal}
                    locationId={objet.locationId}
                  />
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
      )}
      {locataireId !== '0' && (
        <Row>
          <ValidatedForm defaultValues={defaultValuesRent()} onSubmit={handleRent}>
            <ValidatedField
              // label={translate('goFindApp.trajet.dateHeureDepart')}
              label="Date et heure de début"
              id="location-dateHeureDebut"
              name="dateHeureDebut"
              data-cy="dateHeureDebut"
              type="datetime-local"
              placeholder="YYYY-MM-DD HH:mm"
              validate={{
                required: { value: true, message: translate('entity.validation.required') },
              }}
            />
            <ValidatedField
              // label={translate('goFindApp.trajet.dateHeureDepart')}
              label="Date et heure de fin"
              id="location-dateHeureFin"
              name="dateHeureFin"
              data-cy="dateHeureFin"
              type="datetime-local"
              placeholder="YYYY-MM-DD HH:mm"
              validate={{
                required: { value: true, message: translate('entity.validation.required') },
              }}
            />
            {/* <ValidatedField
                id="location-maison"
                name="maison"
                data-cy="maison"
                label={translate('goFindApp.location.maison')}
                type="select"
              >
                <option value="" key="0" />
                {maisons
                  ? maisons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
            {/* <ValidatedField
                label={translate('goFindApp.location.prix')}
                id="location-prix"
                name="prix"
                data-cy="prix"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              /> */}
            <Stack direction="row" spacing={1}>
              <Chip label={`Coût total / mois: ${total}`} variant="outlined" color="success" />
            </Stack>
            <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/houses" replace color="info">
              <FontAwesomeIcon icon="arrow-left" />
>>>>>>> origin
              &nbsp;
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            &nbsp;
            <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
              <FontAwesomeIcon icon="save" />
              &nbsp; Louer
              {/* <Translate contentKey="entity.action.save">Save</Translate> */}
            </Button>
          </ValidatedForm>
        </Row>
      )}
    </div>
  );
};

export default MaisonUpdate;
