import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMaison } from 'app/shared/model/maison.model';
import { getEntities as getMaisons } from 'app/entities/maison/maison.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IPiece } from 'app/shared/model/piece.model';
import { EtatPiece } from 'app/shared/model/enumerations/etat-piece.model';
import { getEntity, updateEntity, createEntity, reset } from './piece.reducer';
import { useLocation } from 'react-router-dom';

export const PieceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const maisons = useAppSelector(state => state.maison.entities);
  const locations = useAppSelector(state => state.location.entities);
  const pieceEntity = useAppSelector(state => state.piece.entity);
  const loading = useAppSelector(state => state.piece.loading);
  const updating = useAppSelector(state => state.piece.updating);
  const updateSuccess = useAppSelector(state => state.piece.updateSuccess);
  const etatPieceValues = Object.keys(EtatPiece);

  const location = useLocation();

  const { maisonId } = location.state || {};

  useEffect(() => {
    console.log('* state: ', location);
    console.log('* maisonId: ', maisonId);
  }, [maisonId]);

  const handleClose = () => {
    // navigate('/houses');
    navigate(`/maison/${maisonId}/edit`);
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getMaisons({}));
    dispatch(getLocations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    let maisonId2 = maisonId;

    if (!maisonId2) {
      maisonId2 = values.maison;
    }

    const entity = {
      ...pieceEntity,
      ...values,
      maison: maisons.find(it => it.id.toString() === maisonId2?.toString()),
      maisonId: maisonId2,
      location: locations.find(it => it.id.toString() === values.location?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          etat: 'EN ATTENTE LOCATION',
        }
      : {
          etat: 'EN ATTENTE LOCATION',
          ...pieceEntity,
          maison: pieceEntity?.maison?.id,
          location: pieceEntity?.location?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="goFindApp.piece.home.createOrEditLabel" data-cy="PieceCreateUpdateHeading">
            <Translate contentKey="goFindApp.piece.home.createOrEditLabel">Create or edit a Piece</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="piece-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('goFindApp.piece.libelle')}
                id="piece-libelle"
                name="libelle"
                data-cy="libelle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('goFindApp.piece.image')} id="piece-image" name="image" data-cy="image" type="text" />
              <ValidatedField label={translate('goFindApp.piece.etat')} id="piece-etat" name="etat" data-cy="etat" type="select">
                {etatPieceValues.map(etatPiece => (
                  <option value={etatPiece} key={etatPiece}>
                    {translate('goFindApp.EtatPiece.' + etatPiece)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="piece-maison" name="maison" data-cy="maison" label={translate('goFindApp.piece.maison')} type="select">
                <option value="" key="0" />
                {maisons
                  ? maisons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {/* <ValidatedField
                id="piece-location"
                name="location"
                data-cy="location"
                label={translate('goFindApp.piece.location')}
                type="select"
              >
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={-1} replace color="info">
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
    </div>
  );
};

export default PieceUpdate;
