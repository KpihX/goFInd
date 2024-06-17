import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { ITrajet } from 'app/shared/model/trajet.model';
import { getEntity, updateEntity, createEntity, reset } from './trajet.reducer';

export const TrajetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const trajetEntity = useAppSelector(state => state.trajet.entity);
  const loading = useAppSelector(state => state.trajet.loading);
  const updating = useAppSelector(state => state.trajet.updating);
  const updateSuccess = useAppSelector(state => state.trajet.updateSuccess);

  const account = useAppSelector(state => state.authentication.account);

  const handleClose = () => {
    navigate('/itinaries');
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

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dateHeureDepart = convertDateTimeToServer(values.dateHeureDepart);
    if (values.places !== undefined && typeof values.places !== 'number') {
      values.places = Number(values.places);
    }
    if (values.prix !== undefined && typeof values.prix !== 'number') {
      values.prix = Number(values.prix);
    }

    const proprietaire = utilisateurs.find(it => it?.loginId?.toString() === account?.id?.toString());

    let entity = {
      ...trajetEntity,
      ...values,
      proprietaire,
      proprietaireId: proprietaire.id,
      engages: trajetEntity.engages,
    };

    if (isNew) {
      entity = {
        ...entity,
        engages: [],
      };
    }

    console.log('* trajet:', entity);

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity({ entity, motif: 'prop' }));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateHeureDepart: displayDefaultDateTime(),
        }
      : {
          ...trajetEntity,
          dateHeureDepart: convertDateTimeFromServer(trajetEntity.dateHeureDepart),
          proprietaire: trajetEntity?.proprietaire?.id,
          engages: trajetEntity?.engages?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="goFindApp.trajet.home.createOrEditLabel" data-cy="TrajetCreateUpdateHeading">
            <Translate contentKey="goFindApp.trajet.home.createOrEditLabel">Create or edit a Trajet</Translate>
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
                  id="trajet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('goFindApp.trajet.depart')}
                id="trajet-depart"
                name="depart"
                data-cy="depart"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('goFindApp.trajet.arrivee')}
                id="trajet-arrivee"
                name="arrivee"
                data-cy="arrivee"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('goFindApp.trajet.dateHeureDepart')}
                id="trajet-dateHeureDepart"
                name="dateHeureDepart"
                data-cy="dateHeureDepart"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('goFindApp.trajet.places')}
                id="trajet-places"
                name="places"
                data-cy="places"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v =>
                    (isNumber(v) &&
                      (v >= trajetEntity.engages.length ||
                        'Il y a déjà ' + trajetEntity.engages.length + ' engagés. Vous ne pouvez allez en deça!')) ||
                    translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('goFindApp.trajet.prix')}
                id="trajet-prix"
                name="prix"
                data-cy="prix"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              {/* <ValidatedField
                id="trajet-proprietaire"
                name="proprietaire"
                data-cy="proprietaire"
                label={translate('goFindApp.trajet.proprietaire')}
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
              </ValidatedField> */}
              {/* <ValidatedField
                label={translate('goFindApp.trajet.engages')}
                id="trajet-engages"
                data-cy="engages"
                type="select"
                multiple
                name="engages"
              >
                <option value="" key="0" />
                {utilisateurs
                  ? utilisateurs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/itinaries" replace color="info">
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

export default TrajetUpdate;
