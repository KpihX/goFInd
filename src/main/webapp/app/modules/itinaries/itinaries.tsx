import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button as Button2, InputAdornment, TextField } from '@mui/material';
import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, reset } from 'app/entities/trajet/trajet.reducer';

const Itinaries = () => {
    const [search, setSearch] = useState('');
    const dispatch = useAppDispatch();
    const pageLocation = useLocation();

    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(
            getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'),
            pageLocation.search
        )
    );
    const [sorting, setSorting] = useState(false);
    const trajetList = useAppSelector(state => state.trajet.entities);
    const loading = useAppSelector(state => state.trajet.loading);
    const updateSuccess = useAppSelector(state => state.trajet.updateSuccess);

    const getAllEntities = () => {
        dispatch(
            getEntities({
                page: paginationState.activePage - 1,
                size: paginationState.itemsPerPage,
                sort: `${paginationState.sort},${paginationState.order}`,
            })
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
        if (window.pageYOffset > 0) {
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

    const getSortIconByFieldName = (fieldName: string) => {
        const sortFieldName = paginationState.sort;
        const order = paginationState.order;
        if (sortFieldName !== fieldName) {
            return faSort;
        } else {
            return order === ASC ? faSortUp : faSortDown;
        }
    };

    const tableStyle = {
        backgroundColor: '#f0f8f5', // Utilisation d'une nuance de vert clair pour le fond de la table
        padding: '20px',
        borderRadius: '8px',
        marginBottom: '20px',
    };

    const buttonContainerStyle = {
        display: 'flex',
        justifyContent: 'flex-end',
        marginBottom: '10px',
    };

    const buttonStyle = {
        marginRight: '10px',
        marginBottom: '10px',
        border: 'none',
        padding: '10px 20px',
        borderRadius: '5px',
        cursor: 'pointer',
    };

    const viewButtonStyle = {
        ...buttonStyle,
        backgroundColor: '#5cb85c', // Vert pour le bouton View
        color: 'white',
    };

    const editButtonStyle = {
        ...buttonStyle,
        backgroundColor: '#5bc0de', // Vert pour le bouton Edit
        color: 'white',
    };

    const deleteButtonStyle = {
        ...buttonStyle,
        backgroundColor: '#d9534f', // Vert pour le bouton Delete
        color: 'white',
    };

  const handleSyncList = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    // Correction du problème : ajoute le type du paramètre dans la déclaration de la fonction
    event.preventDefault();
  };
  
    return (
        <div style={{ backgroundColor: '#e0f2f1', minHeight: '100vh', padding: '20px' }}>
            <TextField
                variant="outlined"
                sx={{ ml: 1 }}
                placeholder="Rechercher"
                value={search}
                onChange={e => setSearch(e.target.value)}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <Button2 variant="contained" style={{ backgroundColor: '#4ac29a' }}>
                                <FontAwesomeIcon icon="search" />
                            </Button2>
                        </InputAdornment>
                    ),
                }}
            />
            <div style={buttonContainerStyle}>
                <Button
                    style={{ ...viewButtonStyle }}
                    onClick={handleSyncList}
                    disabled={loading}
                >
                    <FontAwesomeIcon icon="sync" spin={loading} />{' '}
                    <Translate contentKey="goFindApp.trajet.home.refreshListLabel">Refresh List</Translate>
                </Button>
                <Link
                    to="/trajet/new"
                    className="btn btn-primary jh-create-entity"
                    id="jh-create-entity"
                    style={{ ...buttonStyle, backgroundColor: '#5cb85c' }}
                >
                    <FontAwesomeIcon icon="plus" />
                    <Translate contentKey="goFindApp.trajet.home.createLabel">Create new Trajet</Translate>
                </Link>
            </div>
            <div style={tableStyle}>
                <h2 id="trajet-heading" data-cy="TrajetHeading">
                    <Table responsive>
                        <thead>
                            <tr>
                                <th className="hand" onClick={sort('id')}>
                                    <Translate contentKey="goFindApp.trajet.id">ID</Translate>{' '}
                                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                                </th>
                                <th className="hand" onClick={sort('depart')}>
                                    <Translate contentKey="goFindApp.trajet.depart">Depart</Translate>{' '}
                                    <FontAwesomeIcon icon={getSortIconByFieldName('depart')} />
                                </th>
                                <th className="hand" onClick={sort('arrivee')}>
                                    <Translate contentKey="goFindApp.trajet.arrivee">Arrivee</Translate>{' '}
                                    <FontAwesomeIcon icon={getSortIconByFieldName('arrivee')} />
                                </th>
                                <th className="hand" onClick={sort('dateHeureDepart')}>
                                    <Translate contentKey="goFindApp.trajet.dateHeureDepart">
                                        Date Heure Depart
                                    </Translate>{' '}
                                    <FontAwesomeIcon icon={getSortIconByFieldName('dateHeureDepart')} />
                                </th>
                                <th className="hand" onClick={sort('places')}>
                                    <Translate contentKey="goFindApp.trajet.places">Places</Translate>{' '}
                                    <FontAwesomeIcon icon={getSortIconByFieldName('places')} />
                                </th>
                                <th className="hand" onClick={sort('prix')}>
                                    <Translate contentKey="goFindApp.trajet.prix">Prix</Translate>{' '}
                                    <FontAwesomeIcon icon={getSortIconByFieldName('prix')} />
                                </th>
                                <th>
                                    <Translate contentKey="goFindApp.trajet.proprietaire">Proprietaire</Translate>{' '}
                                    <FontAwesomeIcon icon="sort" />
                                </th>
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
                                        {trajet.dateHeureDepart ? (
                                            <TextFormat
                                                type="date"
                                                value={trajet.dateHeureDepart}
                                                format={APP_DATE_FORMAT}
                                            />
                                        ) : null}
                                    </td>
                                    <td>{trajet.places}</td>
                                    <td>{trajet.prix}</td>
                                    <td>
                                        {trajet.proprietaire ? (
                                            <Link to={`/utilisateur/${trajet.proprietaire.id}`}>
                                                {trajet.proprietaire.id}
                                            </Link>
                                        ) : (
                                            ''
                                        )}
                                    </td>
                                    <td className="text-end">
                                        <div className="btn-group flex-btn-group-container">
                                            <Button
                                                tag={Link}
                                                to={`/trajet/${trajet.id}`}
                                                color="info"
                                                size="sm"
                                                data-cy="entityDetailsButton"
                                                style={{ ...buttonStyle, backgroundColor: '#5cb85c', color: 'white' }}
                                            >
                                                <FontAwesomeIcon icon="eye" />{' '}
                                                <span className="d-none d-md-inline">
                                                    <Translate contentKey="entity.action.view">View</Translate>
                                                </span>
                                                </Button>
                                                <Button
                                                    tag={Link}
                                                    to={`/trajet/${trajet.id}/edit`}
                                                    color="primary"
                                                    size="sm"
                                                    data-cy="entityEditButton"
                                                    style={{ ...buttonStyle, backgroundColor: '#5bc0de', color: 'white' }}
                                                >
                                                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                                                    <span className="d-none d-md-inline">
                                                        <Translate contentKey="entity.action.edit">Edit</Translate>
                                                    </span>
                                                </Button>
                                                <Button
                                                    onClick={() =>
                                                        (window.location.href = `/trajet/${trajet.id}/delete`)
                                                    }
                                                    color="danger"
                                                    size="sm"
                                                    data-cy="entityDeleteButton"
                                                    style={{ ...buttonStyle, backgroundColor: '#d9534f', color: 'white' }}
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
                    </h2>
                    {!loading && trajetList.length === 0 && (
                        <div className="alert alert-warning">
                            <Translate contentKey="goFindApp.trajet.home.notFound">No Trajets found</Translate>
                        </div>
                    )}
                </div>
            </div>
    );
};

export default Itinaries;

