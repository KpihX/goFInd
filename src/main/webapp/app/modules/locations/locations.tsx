import React, { useState, useEffect } from 'react';
<<<<<<< HEAD
import { translate } from 'react-jhipster';
import { GenTypeObjet } from 'app/shared/model/enumerations/type-objet.model';
import { Button as Button2 } from '@mui/material';
import Stack from '@mui/material/Stack';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { InputAdornment } from '@mui/material';
import TextField from '@mui/material/TextField';
import Location from './location';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';

import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
=======
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
>>>>>>> origin
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
<<<<<<< HEAD

import { getEntities, reset } from 'app/entities/location/location.reducer';

export const Locations = () => {
  const [currentSearch, setCurrentSearch] = useState('');
  const [search, setSearch] = useState('');
 
  const [typeSearch, setTypeSearch] = React.useState('libelle'); // ou 'identifiant

  const handleChange = (event: SelectChangeEvent) => {
    setTypeSearch(event.target.value);
  };

=======

import { getEntities, reset } from 'app/entities/location/location.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { Stack } from '@mui/material';
import { Button as Button2 } from '@mui/material';
import { getEntities as getMaisons } from 'app/entities/maison/maison.reducer';
import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';

export const Location = () => {
>>>>>>> origin
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const locationList = useAppSelector(state => state.location.entities);
  const loading = useAppSelector(state => state.location.loading);
  const links = useAppSelector(state => state.location.links);
  const updateSuccess = useAppSelector(state => state.location.updateSuccess);

<<<<<<< HEAD
=======
  const [status, setStatus] = useState('prop'); // or "loc"
  const maisonList = useAppSelector(state => state.maison.entities);
  const account = useAppSelector(state => state.authentication.account);
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);

>>>>>>> origin
  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
<<<<<<< HEAD
        search: currentSearch,
        searchType: typeSearch,
      }),
    );
=======
      }),
    );
    dispatch(getMaisons({}));
    dispatch(getUtilisateurs({}));
>>>>>>> origin
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
<<<<<<< HEAD
=======
    dispatch(getMaisons({}));
    dispatch(getUtilisateurs({}));
>>>>>>> origin
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
<<<<<<< HEAD
=======
    console.log('* locations', locationList);
  }, [locationList]);

  useEffect(() => {
>>>>>>> origin
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
<<<<<<< HEAD
  }, [paginationState.activePage, search, typeSearch]);
=======
  }, [paginationState.activePage]);
>>>>>>> origin

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

<<<<<<< HEAD
  React.useEffect(() => {
    if (currentSearch === '') {
      setSearch('');
    }
  }, [currentSearch]);

  return (
    <div className="flex flex-col">
=======
  return (
    <div>
>>>>>>> origin
      <Stack
        direction="row"
        spacing={2}
        sx={{
          justifyContent: 'center',
          paddingY: '1rem',
        }}
      >
<<<<<<< HEAD
        
      </Stack>
      <div className="flex flex-row">
        <TextField
          variant="outlined"
          sx={{ ml: 1 }}
          placeholder="Rechercher une location ..."
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
            <MenuItem value="libelle">Libelle</MenuItem>
            <MenuItem value="identifiant">Identifiant</MenuItem>
          </Select>
        </FormControl>
      </div>
      <div>
        <h2 id="objet-heading" data-cy="ObjetHeading">
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="goFindApp.objet.home.refreshListLabel">Refresh List</Translate>
            </Button>
            <Link to="/location/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              Créer une nouvelle location
            </Link>
          </div>
        </h2>
      </div>

      <InfiniteScroll
        dataLength={locationList ? locationList.length : 0}
        next={handleLoadMore}
        hasMore={paginationState.activePage - 1 < links.next}
        loader={<div className="loader">Loading ...</div>}
      >
        {locationList && locationList.length > 0 ? (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, minmax(0, 1fr))' }}>
            {locationList.map(
              (location, i) =>
                
                  <Location
                    key={`entity-${i}`}
                    id={location.prix}
                    prix={location.prix}
                    maison={location.maison}
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
            )}
          </div>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              Pas de locations trouvées
            </div>
          )
        )}
      </InfiniteScroll>
=======
        <Button2
          key="prop"
          variant={status === 'prop' ? 'contained' : 'outlined'}
          onClick={() => {
            setStatus('prop');
          }}
        >
          Bayeur ?
        </Button2>
        <Button2
          key="prop"
          variant={status === 'loc' ? 'contained' : 'outlined'}
          onClick={() => {
            setStatus('loc');
          }}
        >
          Locataire ?
        </Button2>
      </Stack>
      <h2 id="location-heading" data-cy="LocationHeading">
        <Translate contentKey="goFindApp.location.home.title">Locations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="goFindApp.location.home.refreshListLabel">Refresh List</Translate>
          </Button>
          {/* <Link to="/location/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="goFindApp.location.home.createLabel">Create new Location</Translate>
          </Link> */}
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={locationList ? locationList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {locationList && locationList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="goFindApp.location.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('prix')}>
                    <Translate contentKey="goFindApp.location.prix">Prix</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('prix')} />
                  </th>
                  <th className="hand" onClick={sort('dateHeureDebut')}>
                    Date et heure de début
                    {/* <Translate contentKey="goFindApp.trajet.dateHeureDepart">Date Heure Depart</Translate>{' '} */}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dateHeureDebut')} />
                  </th>
                  <th className="hand" onClick={sort('dateHeureFin')}>
                    Date et heure de fin
                    {/* <Translate contentKey="goFindApp.trajet.dateHeureDepart">Date Heure Depart</Translate>{' '} */}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dateHeureFin')} />
                  </th>
                  <th>
                    <Translate contentKey="goFindApp.location.maison">Maison</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {locationList.map((location, i) => (
                  <>
                    {/* <>{console.log("* maisonListV", status === "prop" && maisonList.find(maison => maison.id === location.maisonId)?.proprietaire.loginId) === account?.id}</>
                  <>{console.log("* maisonList", status === "prop"}</> */}
                    {((status === 'prop' &&
                      maisonList.find(maison => maison.id === location.maisonId)?.proprietaire.loginId === account?.id) ||
                      (status === 'loc' && location.locataireId === utilisateurs.find(user => user?.loginId === account?.id).id)) && (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>
                          <Button tag={Link} to={`/location/${location.id}`} color="link" size="sm">
                            {location.id}
                          </Button>
                        </td>
                        <td>{location.prix} FCFA</td>
                        <td>
                          {location.dateHeureDebut ? (
                            <TextFormat type="date" value={location.dateHeureDebut} format={APP_DATE_FORMAT} />
                          ) : null}
                        </td>
                        <td>
                          {location.dateHeureFin ? <TextFormat type="date" value={location.dateHeureFin} format={APP_DATE_FORMAT} /> : null}
                        </td>
                        <td>{location.maison ? <Link to={`/maison/${location.maison.id}`}>{location.maison.id}</Link> : ''}</td>
                        <td className="text-end">
                          <div className="btn-group flex-btn-group-container">
                            <Button tag={Link} to={`/location/${location.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                              <FontAwesomeIcon icon="eye" />{' '}
                              <span className="d-none d-md-inline">
                                Détails
                                {/* <Translate contentKey="entity.action.view">View</Translate> */}
                              </span>
                            </Button>
                            <Button
                              onClick={() => (window.location.href = `/location/${location.id}/delete`)}
                              color="danger"
                              size="sm"
                              data-cy="entityDeleteButton"
                            >
                              <FontAwesomeIcon icon="trash" />{' '}
                              <span className="d-none d-md-inline">
                                Annuler
                                {/* <Translate contentKey="entity.action.delete">Delete</Translate> */}
                              </span>
                            </Button>
                          </div>
                        </td>
                      </tr>
                    )}
                  </>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="goFindApp.location.home.notFound">No Locations found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
>>>>>>> origin
    </div>
  );
};

export default Location;
