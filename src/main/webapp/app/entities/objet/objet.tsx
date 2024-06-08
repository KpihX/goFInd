import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './objet.reducer';

export const Objet = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const objetList = useAppSelector(state => state.objet.entities);
  const loading = useAppSelector(state => state.objet.loading);
  const links = useAppSelector(state => state.objet.links);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

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

  return (
    <div>
      <h2 id="objet-heading" data-cy="ObjetHeading">
        <Translate contentKey="goFindApp.objet.home.title">Objets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="goFindApp.objet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/objet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="goFindApp.objet.home.createLabel">Create new Objet</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={objetList ? objetList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {objetList && objetList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="goFindApp.objet.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('libelle')}>
                    <Translate contentKey="goFindApp.objet.libelle">Libelle</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('libelle')} />
                  </th>
                  <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="goFindApp.objet.description">Description</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                  </th>
                  <th className="hand" onClick={sort('type')}>
                    <Translate contentKey="goFindApp.objet.type">Type</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                  </th>
                  <th className="hand" onClick={sort('image')}>
                    <Translate contentKey="goFindApp.objet.image">Image</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                  </th>
                  <th className="hand" onClick={sort('identifiant')}>
                    <Translate contentKey="goFindApp.objet.identifiant">Identifiant</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('identifiant')} />
                  </th>
                  <th className="hand" onClick={sort('etat')}>
                    <Translate contentKey="goFindApp.objet.etat">Etat</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('etat')} />
                  </th>
                  <th>
                    <Translate contentKey="goFindApp.objet.proprietaire">Proprietaire</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="goFindApp.objet.signalant">Signalant</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {objetList.map((objet, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/objet/${objet.id}`} color="link" size="sm">
                        {objet.id}
                      </Button>
                    </td>
                    <td>{objet.libelle}</td>
                    <td>{objet.description}</td>
                    <td>
                      <Translate contentKey={`goFindApp.TypeObjet.${objet.type}`} />
                    </td>
                    <td>{objet.image}</td>
                    <td>{objet.identifiant}</td>
                    <td>
                      <Translate contentKey={`goFindApp.EtatObjet.${objet.etat}`} />
                    </td>
                    <td>{objet.proprietaire ? <Link to={`/utilisateur/${objet.proprietaire.id}`}>{objet.proprietaire.id}</Link> : ''}</td>
                    <td>{objet.signalant ? <Link to={`/utilisateur/${objet.signalant.id}`}>{objet.signalant.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/objet/${objet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/objet/${objet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/objet/${objet.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="goFindApp.objet.home.notFound">No Objets found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Objet;
