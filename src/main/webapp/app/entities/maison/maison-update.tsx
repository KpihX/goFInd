import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { IMaison } from 'app/shared/model/maison.model';
import { getEntity, updateEntity, createEntity, reset } from './maison.reducer';

export const MaisonUpdate = () => {
  const account = useAppSelector(state => state.authentication.account);
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
    navigate('/maisons');
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
    const proprietaire = utilisateurs.find(it => it?.loginId?.toString() === account?.id?.toString());

    const entity = {
      ...maisonEntity,
      ...values,
      proprietaire,
      proprietaireId: proprietaire.id,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity({ entity, report: 'ras' }));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...maisonEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="appMaison.home.createOrEditLabel" data-cy="MaisonCreateUpdateHeading">
            Create or edit a Maison
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="maison-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Localisation"
                id="maison-localisation"
                name="localisation"
                data-cy="localisation"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="maison-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Image" id="maison-image" name="image" data-cy="image" type="text" />
              <ValidatedField
                label="Identifiant"
                id="maison-identifiant"
                name="identifiant"
                data-cy="identifiant"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/maisons" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MaisonUpdate;
