import React, { useState, useEffect } from 'react';
import { translate } from 'react-jhipster';

import { Button as Button2 } from '@mui/material';
import Stack from '@mui/material/Stack';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { InputAdornment } from '@mui/material';
import TextField from '@mui/material/TextField';
import House from './house';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';

import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from 'app/entities/maison/maison.reducer';

export const Houses = () => {
  const [currentSearch, setCurrentSearch] = useState('');
  const [search, setSearch] = useState('');

  const [typeSearch, setTypeSearch] = React.useState('libelle'); // ou 'identifiant

  const handleChange = (event: SelectChangeEvent) => {
    setTypeSearch(event.target.value);
  };

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
        search: currentSearch,
        searchType: typeSearch,
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
  }, [paginationState.activePage, search, typeSearch]);

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

  React.useEffect(() => {
    if (currentSearch === '') {
      setSearch('');
    }
  }, [currentSearch]);

  return (
    <div className="flex flex-col">
      <Stack
        direction="row"
        spacing={2}
        sx={{
          justifyContent: 'center',
          paddingY: '1rem',
        }}
      ></Stack>
      <div className="flex flex-row">
        <TextField
          variant="outlined"
          sx={{ ml: 1 }}
          placeholder="Rechercher un maison ..."
          value={currentSearch}
          onChange={e => setCurrentSearch(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <Button2 variant="contained" onClick={() => setSearch(currentSearch)}>
                  <FontAwesomeIcon icon="search" />
                </Button2>
              </InputAdornment>
            ),
          }}
        />
        <FormControl sx={{ m: 1, minWidth: 120 }}>
          <InputLabel id="demo-select-small-label">Type de recherche</InputLabel>
          <Select
            labelId="demo-select-small-label"
            id="demo-select-small"
            value={typeSearch}
            label="Type de recherche"
            onChange={handleChange}
          >
            <MenuItem value="libelle">Localisation</MenuItem>
            <MenuItem value="identifiant">Identifiant</MenuItem>
          </Select>
        </FormControl>
      </div>
      <div>
        <h2 id="maison-heading" data-cy="maisonHeading">
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="goFindApp.maison.home.refreshListLabel">Refresh List</Translate>
            </Button>
            <Link to="/maison/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Créer une nouvelle maison
            </Link>
          </div>
        </h2>
      </div>

      <InfiniteScroll
        dataLength={maisonList ? maisonList.length : 0}
        next={handleLoadMore}
        hasMore={paginationState.activePage - 1 < links.next}
        loader={<div className="loader">Loading ...</div>}
      >
        {maisonList && maisonList.length > 0 ? (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, minmax(0, 1fr))' }}>
            {maisonList.map(
              (maison, i) => (
                <House
                  key={`entity-${i}`}
                  id={maison.id}
                  libelle={maison.libelle}
                  identifiant={maison.identifiant}
                  image={maison.image}
                  etat={maison.etat}
                  proprietaire={maison.proprietaire}
                  signalant={maison.signalant}
                  description={maison.description}
                />
              ),
              // <div key={`entity-${i}`} data-cy="entityTable">
              //   <div>
              //     <Button tag={Link} to={`/maison/${maison.id}`} color="link" size="sm">
              //       {maison.id}
              //     </Button>
              //   </div>
              //   <p>{maison.libelle}</p>
              //   <p>{maison.description}</p>
              //   <div>
              //     <Translate contentKey={`goFindApp.Typemaison.${maison.type}`} />
              //   </div>
              //   <p>{maison.image}</p>
              //   <p>{maison.identifiant}</p>
              //   <div>
              //     <Translate contentKey={`goFindApp.Etatmaison.${maison.etat}`} />
              //   </div>
              //   <p>{maison.proprietaire ? <Link to={`/utilisateur/${maison.proprietaire.id}`}>{maison.proprietaire.id}</Link> : ''}</p>
              //   <div className="text-end">
              //     <div className="btn-group flex-btn-group-container">
              //       <Button tag={Link} to={`/maison/${maison.id}`} color="info" size="sm" data-cy="entityDetailsButton">
              //         <FontAwesomeIcon icon="eye" />{' '}
              //         <span className="d-none d-md-inline">
              //           <Translate contentKey="entity.action.view">View</Translate>
              //         </span>
              //       </Button>
              //       <Button tag={Link} to={`/maison/${maison.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
              //         <FontAwesomeIcon icon="pencil-alt" />{' '}
              //         <span className="d-none d-md-inline">
              //           <Translate contentKey="entity.action.edit">Edit</Translate>
              //         </span>
              //       </Button>
              //       <Button
              //         onClick={() => (window.location.href = `/maison/${maison.id}/delete`)}
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
            )}
          </div>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="goFindApp.maison.home.notFound">No maisons found</Translate>
            </div>
          )
        )}
      </InfiniteScroll>
    </div>
  );
};

export default Houses;
