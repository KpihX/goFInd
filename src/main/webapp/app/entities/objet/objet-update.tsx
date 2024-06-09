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
import { IObjet } from 'app/shared/model/objet.model';
import { TypeObjet } from 'app/shared/model/enumerations/type-objet.model';
import { EtatObjet } from 'app/shared/model/enumerations/etat-objet.model';
import { getEntity, updateEntity, createEntity, reset } from './objet.reducer';

export const ObjetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const objetEntity = useAppSelector(state => state.objet.entity);
  const loading = useAppSelector(state => state.objet.loading);
  const updating = useAppSelector(state => state.objet.updating);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);
  const typeObjetValues = Object.keys(TypeObjet);
  const etatObjetValues = Object.keys(EtatObjet);

  const handleClose = () => {
    navigate('/objects');
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

    // console.log("* values:", values)

    const signalant = utilisateurs.find(it => it.id.toString() === values.signalant?.toString());

    const entity = {
      ...objetEntity,
      ...values,
      proprietaire: utilisateurs.find(it => it.id.toString() === values.proprietaire?.toString()),
      signalant,
      signalantId: signalant?.id,
    };

    // console.log("* entity:", entity)

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity({ entity, report: 'ras' }));
      // console.log("* entity:", entity)
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'TELEPHONE',
          etat: 'VOLE',
          ...objetEntity,
          proprietaire: objetEntity?.proprietaire?.id,
          signalant: objetEntity?.signalant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="goFindApp.objet.home.createOrEditLabel" data-cy="ObjetCreateUpdateHeading">
            <Translate contentKey="goFindApp.objet.home.createOrEditLabel">Create or edit a Objet</Translate>
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
                  id="objet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('goFindApp.objet.libelle')}
                id="objet-libelle"
                name="libelle"
                data-cy="libelle"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('goFindApp.objet.description')}
                id="objet-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('goFindApp.objet.type')} id="objet-type" name="type" data-cy="type" type="select">
                {typeObjetValues.map(typeObjet => (
                  <option value={typeObjet} key={typeObjet}>
                    {translate('goFindApp.TypeObjet.' + typeObjet)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('goFindApp.objet.image')} id="objet-image" name="image" data-cy="image" type="text" />
              <ValidatedField
                label={translate('goFindApp.objet.identifiant')}
                id="objet-identifiant"
                name="identifiant"
                data-cy="identifiant"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('goFindApp.objet.etat')} id="objet-etat" name="etat" data-cy="etat" type="select">
                {etatObjetValues.map(etatObjet => (
                  <option value={etatObjet} key={etatObjet}>
                    {translate('goFindApp.EtatObjet.' + etatObjet)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="objet-proprietaire"
                name="proprietaire"
                data-cy="proprietaire"
                label={translate('goFindApp.objet.proprietaire')}
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
              <ValidatedField
                id="objet-signalant"
                name="signalant"
                data-cy="signalant"
                label={translate('goFindApp.objet.signalant')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/objects" replace color="info">
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

export default ObjetUpdate;
