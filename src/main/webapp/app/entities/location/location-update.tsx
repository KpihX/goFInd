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
import { getEntity, updateEntity, createEntity, reset } from './location.reducer';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';

export const LocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const maisons = useAppSelector(state => state.maison.entities);
  const locationEntity = useAppSelector(state => state.location.entity);
  const loading = useAppSelector(state => state.location.loading);
  const updating = useAppSelector(state => state.location.updating);
  const updateSuccess = useAppSelector(state => state.location.updateSuccess);

  const account = useAppSelector(state => state.authentication.account);
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);

  const handleClose = () => {
    navigate('/locations');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getMaisons({}));
    dispatch(getUtilisateurs({}));
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
    if (values.prix !== undefined && typeof values.prix !== 'number') {
      values.prix = Number(values.prix);
    }

    const locataire = utilisateurs.find(it => it?.loginId?.toString() === account?.id?.toString());

    const entity = {
      ...locationEntity,
      ...values,
      maison: maisons.find(it => it.id.toString() === values.maison?.toString()),
      locataire,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...locationEntity,
          maison: locationEntity?.maison?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="goFindApp.location.home.createOrEditLabel" data-cy="LocationCreateUpdateHeading">
            <Translate contentKey="goFindApp.location.home.createOrEditLabel">Create or edit a Location</Translate>
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
                  id="location-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('goFindApp.location.prix')}
                id="location-prix"
                name="prix"
                data-cy="prix"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
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
              <ValidatedField
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
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/locations" replace color="info">
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

export default LocationUpdate;
