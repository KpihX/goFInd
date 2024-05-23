import React, { useState, useEffect } from 'react';
import { translate } from 'react-jhipster';
import { TypeObjet } from 'app/shared/model/enumerations/type-objet.model';
import { Button as Button2 } from '@mui/material';
import Stack from '@mui/material/Stack';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { InputAdornment } from '@mui/material';
import TextField from '@mui/material/TextField';
import { Object as Objet } from './object';

import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from 'app/entities/objet/objet.reducer';

export const Objects = () => {
  const [search, setSearch] = useState('');
  const typeObjetValues = Object.keys(TypeObjet);

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
    <div className="flex flex-col">
      <Stack
        direction="row"
        spacing={2}
        sx={{
          justifyContent: 'center',
          paddingY: '1rem',
        }}
      >
        {typeObjetValues.map(typeObjet => (
          <Button2 key={typeObjet}>{translate('goFindApp.TypeObjet.' + typeObjet)}</Button2>
        ))}
      </Stack>
      <div className="flex flex-row">
        <TextField
          variant="outlined"
          sx={{ ml: 1 }}
          placeholder="Rechercher"
          value={search}
          onChange={e => setSearch(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <Button
                  variant="contained"
                  // onClick={() => console.log('search', search)}
                >
                  <FontAwesomeIcon icon="search" />
                </Button>
              </InputAdornment>
            ),
          }}
        />
        <h2 id="objet-heading" data-cy="ObjetHeading">
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
      </div>

      <InfiniteScroll
        dataLength={objetList ? objetList.length : 0}
        next={handleLoadMore}
        hasMore={paginationState.activePage - 1 < links.next}
        loader={<div className="loader">Loading ...</div>}
      >
        {objetList && objetList.length > 0 ? (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, minmax(0, 1fr))' }}>
            {objetList.map((objet, i) => (
              <Objet
                key={`entity-${i}`}
                id={objet.id}
                libelle={objet.libelle}
                identifiant={objet.identifiant}
                image={objet.image}
                proprietaire={objet.proprietaire}
                description={objet.description}
              />
              // <div key={`entity-${i}`} data-cy="entityTable">
              //   <div>
              //     <Button tag={Link} to={`/objet/${objet.id}`} color="link" size="sm">
              //       {objet.id}
              //     </Button>
              //   </div>
              //   <p>{objet.libelle}</p>
              //   <p>{objet.description}</p>
              //   <div>
              //     <Translate contentKey={`goFindApp.TypeObjet.${objet.type}`} />
              //   </div>
              //   <p>{objet.image}</p>
              //   <p>{objet.identifiant}</p>
              //   <div>
              //     <Translate contentKey={`goFindApp.EtatObjet.${objet.etat}`} />
              //   </div>
              //   <p>{objet.proprietaire ? <Link to={`/utilisateur/${objet.proprietaire.id}`}>{objet.proprietaire.id}</Link> : ''}</p>
              //   <div className="text-end">
              //     <div className="btn-group flex-btn-group-container">
              //       <Button tag={Link} to={`/objet/${objet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
              //         <FontAwesomeIcon icon="eye" />{' '}
              //         <span className="d-none d-md-inline">
              //           <Translate contentKey="entity.action.view">View</Translate>
              //         </span>
              //       </Button>
              //       <Button tag={Link} to={`/objet/${objet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
              //         <FontAwesomeIcon icon="pencil-alt" />{' '}
              //         <span className="d-none d-md-inline">
              //           <Translate contentKey="entity.action.edit">Edit</Translate>
              //         </span>
              //       </Button>
              //       <Button
              //         onClick={() => (window.location.href = `/objet/${objet.id}/delete`)}
              //         color="danger"
              //         size="sm"
              //         data-cy="entityDeleteButton"
              //       >
              //         <FontAwesomeIcon icon="trash" />{' '}
              //         <span className="d-none d-md-inline">
              //           <Translate contentKey="entity.action.delete">Delete</Translate>
              //         </span>
              //       </Button>
              //     </div>
              //   </div>
              // </div>
            ))}
          </div>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="goFindApp.objet.home.notFound">No Objets found</Translate>
            </div>
          )
        )}
      </InfiniteScroll>
    </div>
  );
};

export default Objects;
