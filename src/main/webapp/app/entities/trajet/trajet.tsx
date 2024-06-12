import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, updateEntity, reset } from './trajet.reducer';

export const Trajet = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(location, ITEMS_PER_PAGE, 'id'), location.search),
  );
  const [sorting, setSorting] = useState(false);

  const trajetList = useAppSelector(state => state.trajet.entities);
  const loading = useAppSelector(state => state.trajet.loading);
  const links = useAppSelector(state => state.trajet.links);
  const updateSuccess = useAppSelector(state => state.trajet.updateSuccess);

  const [engagedTrajets, setEngagedTrajets] = useState({});

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
    setPaginationState({ ...paginationState, activePage: 1 });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (!loading) {
      console.log('*trajetList: ', trajetList);
    }
  }, [loading]);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      setPaginationState({ ...paginationState, activePage: paginationState.activePage + 1 });
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
    setPaginationState({ ...paginationState, activePage: 1, order: paginationState.order === ASC ? DESC : ASC, sort: p });
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

  const handleEngage = trajet => {
    const updatedTrajet = { ...trajet, places: trajet.places - 1 };
    dispatch(updateEntity(updatedTrajet));
    setEngagedTrajets({ ...engagedTrajets, [trajet.id]: true });
  };

  const handleRetract = trajet => {
    const updatedTrajet = { ...trajet, places: trajet.places + 1 };
    dispatch(updateEntity(updatedTrajet));
    setEngagedTrajets({ ...engagedTrajets, [trajet.id]: false });
  };

  return (
    <div>
      <h2 id="trajet-heading" data-cy="TrajetHeading">
        Trajets
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/trajet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" /> &nbsp; Create new Trajet
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={trajetList ? trajetList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {trajetList && trajetList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('depart')}>
                    Depart <FontAwesomeIcon icon={getSortIconByFieldName('depart')} />
                  </th>
                  <th className="hand" onClick={sort('arrivee')}>
                    Arrivee <FontAwesomeIcon icon={getSortIconByFieldName('arrivee')} />
                  </th>
                  <th className="hand" onClick={sort('dateHeureDepart')}>
                    Date Heure Depart <FontAwesomeIcon icon={getSortIconByFieldName('dateHeureDepart')} />
                  </th>
                  <th className="hand" onClick={sort('places')}>
                    Places <FontAwesomeIcon icon={getSortIconByFieldName('places')} />
                  </th>
                  <th className="hand" onClick={sort('prix')}>
                    Prix <FontAwesomeIcon icon={getSortIconByFieldName('prix')} />
                  </th>
                  <th>Proprietaire</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {trajetList.map((trajet, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/trajet/${trajet.id}`} color="link" size="sm">
                        {trajet.id}
                      </Button>
                    </td>
                    <td>{trajet.depart}</td>
                    <td>{trajet.arrivee}</td>
                    <td>
                      {trajet.dateHeureDepart ? <TextFormat type="date" value={trajet.dateHeureDepart} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{trajet.places}</td>
                    <td>{trajet.prix}</td>
                    <td>
                      {trajet.proprietaire ? <Link to={`/utilisateur/${trajet.proprietaire.id}`}>{trajet.proprietaire.id}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/trajet/${trajet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`/trajet/${trajet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/trajet/${trajet.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                        {engagedTrajets[trajet.id] ? (
                          <Button color="warning" size="sm" onClick={() => handleRetract(trajet)}>
                            Se rétracter
                          </Button>
                        ) : (
                          <Button color="success" size="sm" onClick={() => handleEngage(trajet)}>
                            S'engager
                          </Button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Trajets found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Trajet;
