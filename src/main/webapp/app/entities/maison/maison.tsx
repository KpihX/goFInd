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

import { getEntities, reset } from './maison.reducer';

export const Maison = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const maisonList = useAppSelector(state => state.maison.entities);
  const loading = useAppSelector(state => state.maison.loading);
  const links = useAppSelector(state => state.maison.links);
  const updateSuccess = useAppSelector(state => state.maison.updateSuccess);

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
      <h2 id="maison-heading" data-cy="MaisonHeading">
        <Translate contentKey="goFindApp.maison.home.title">Maisons</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="goFindApp.maison.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/maison/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="goFindApp.maison.home.createLabel">Create new Maison</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={maisonList ? maisonList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {maisonList && maisonList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="goFindApp.maison.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('libelle')}>
                    <Translate contentKey="goFindApp.maison.libelle">Libelle</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('libelle')} />
                  </th>
                  <th className="hand" onClick={sort('description')}>
                    <Translate contentKey="goFindApp.maison.description">Description</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                  </th>
                  <th className="hand" onClick={sort('type')}>
                    <Translate contentKey="goFindApp.maison.type">Type</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                  </th>
                  <th className="hand" onClick={sort('image')}>
                    <Translate contentKey="goFindApp.maison.image">Image</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                  </th>
                  <th className="hand" onClick={sort('identifiant')}>
                    <Translate contentKey="goFindApp.maison.identifiant">Identifiant</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('identifiant')} />
                  </th>
                  <th className="hand" onClick={sort('etat')}>
                    <Translate contentKey="goFindApp.maison.etat">Etat</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('etat')} />
                  </th>
                  <th>
                    <Translate contentKey="goFindApp.maison.proprietaire">Proprietaire</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="goFindApp.maison.signalant">Signalant</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {maisonList.map((maison, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/maison/${maison.id}`} color="link" size="sm">
                        {maison.id}
                      </Button>
                    </td>
                    <td>{maison.libelle}</td>
                    <td>{maison.description}</td>
                    <td>
                      <Translate contentKey={`goFindApp.TypeMaison.${maison.type}`} />
                    </td>
                    <td>{maison.image}</td>
                    <td>{maison.identifiant}</td>
                    <td>
                      <Translate contentKey={`goFindApp.EtatMaison.${maison.etat}`} />
                    </td>
                    <td>
                      {maison.proprietaire ? <Link to={`/utilisateur/${maison.proprietaire.id}`}>{maison.proprietaire.id}</Link> : ''}
                    </td>
                    <td>{maison.signalant ? <Link to={`/utilisateur/${maison.signalant.id}`}>{maison.signalant.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/maison/${maison.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/maison/${maison.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/maison/${maison.id}/delete`)}
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
                <Translate contentKey="goFindApp.maison.home.notFound">No Maisons found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Maison;
