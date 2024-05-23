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
import { IMaison } from 'app/shared/model/maison.model';
import { getEntity, updateEntity, createEntity, reset } from './maison.reducer';

export const MaisonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const maisonEntity = useAppSelector(state => state.maison.entity);
  const loading = useAppSelector(state => state.maison.loading);
  const updating = useAppSelector(state => state.maison.updating);
  const updateSuccess = useAppSelector(state => state.maison.updateSuccess);

  const handleClose = () => {
    navigate('/maison');
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

    const entity = {
      ...maisonEntity,
      ...values,
      proprietaire: utilisateurs.find(it => it.id.toString() === values.proprietaire?.toString()),
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
          ...maisonEntity,
          proprietaire: maisonEntity?.proprietaire?.id,
        };

  return (
    <div>
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
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="maison-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
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
              <ValidatedField
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
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/maison" replace color="info">
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

export default MaisonUpdate;
