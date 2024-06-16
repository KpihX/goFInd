import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Avatar from '@mui/material/Avatar';
import IconButton, { IconButtonProps } from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import { red } from '@mui/material/colors';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShareIcon from '@mui/icons-material/Share';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import MoreVertIcon from '@mui/icons-material/MoreVert';

import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
// import { updateEntity } from 'app/entities/piece/objet.reducer';
import { toast } from 'react-toastify';
import { getEntities } from 'app/entities/utilisateur/utilisateur.reducer';

import './houses.css';
import { Checkbox } from '@mui/material';

interface ExpandMoreProps extends IconButtonProps {
  expand: boolean;
}

const ExpandMore = styled((props: ExpandMoreProps) => {
  const { expand, ...other } = props;
  return <IconButton {...other} />;
})(({ theme, expand }) => ({
  transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
  marginLeft: 'auto',
  transition: theme.transitions.create('transform', {
    duration: theme.transitions.duration.shortest,
  }),
}));

export function Room({
  id,
  libelle,
  image,
  etat,
  proprietaire,
  locataireId,
  selectedPieces,
  setSelectedPieces,
  prix,
  pieces,
  total,
  setTotal,
  locationId,
}) {
  const account = useAppSelector(state => state.authentication.account);
  // const [rent, setRent] = React.useState(false);
  // const rentInit = false;
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const objetEntity = useAppSelector(state => state.objet.entity);
  const [updateUtilisateurs, setUpdateUtilisateurs] = React.useState(false);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);
  const dispatch = useAppDispatch();
  const [begin, setBegin] = React.useState(true);

  const [checked, setChecked] = React.useState(false);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setChecked(event.target.checked);
  };

  React.useEffect(() => {
    if (begin) {
      return;
    }
    if (checked) {
      setSelectedPieces([...selectedPieces, pieces.filter(piece => piece.id === id)[0]]);
      setTotal(total + prix);
    } else {
      setSelectedPieces(selectedPieces.filter(piece => piece.id !== id));
      setTotal(total - prix);
    }
  }, [checked, begin]);

  React.useEffect(() => {
    console.log('* selectedPieces: ', selectedPieces);
  }, [selectedPieces]);

  React.useEffect(() => {
    // setRent(false);
    dispatch(getEntities({}));
    setUpdateUtilisateurs(true);
  }, []);

  React.useEffect(() => {
    // console.log("* libelle", libelle);
    // console.log("- proprio", proprietaire);
    // console.log("- account", account.id);
    // console.log("- rent", rent);
    // console.log("- signalant", signalant);
  }, []);

  return (
    <>
      {(account.id?.toString() === proprietaire.loginId?.toString() || etat === 'ATTENTE') && (
        <div key={id} className="house-card">
          {locataireId !== '0' && (
            <Checkbox
              checked={checked}
              onChange={event => {
                handleChange(event);
                setBegin(false);
              }}
              inputProps={{ 'aria-label': 'controlled' }}
            />
          )}
          <img src={image} alt={libelle} className="house-image" />
          <div className="house-info">
            <h2>{libelle}</h2>
            <h2>{prix}&nbsp;FCFA / mois</h2>
            {/* {account.id === proprietaire.loginId ? ( */}
            {locataireId === '0' ? (
              <div style={{ marginRight: '0.5rem', marginBottom: '0.5rem' }}>
                <Stack direction="row" spacing={1}>
                  <Chip label={etat} variant="outlined" color="success" />
                </Stack>

                {etat !== 'LOUE' ? (
                  <div style={{ display: 'flex', justifyContent: 'center' }} className="flex flex-row pl-3 justify-center">
                    {/* <Button tag={Link} to={`/piece/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
                <FontAwesomeIcon icon="eye" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.view">View</Translate>
                </span>
              </Button> */}
                    <Button tag={Link} to={`/piece/${id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                      <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.edit">Edit</Translate>
                      </span>
                    </Button>
                    <Button
                      onClick={() => (window.location.href = `/piece/${id}/delete`)}
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
                ) : (
                  <Button tag={Link} to={`/location/${locationId}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      Détails de la location
                      {/* <Translate contentKey="entity.action.view">View</Translate> */}
                    </span>
                  </Button>
                )}
              </div>
            ) : // <div style={{ display: 'flex', justifyContent: 'center' }} className="absolutemt-2 space-x-2 space-y-2">
            //   {true ? (
            //     <Button color="warning" size="sm" data-cy="entityReportButton" onClick={handleRent}>
            //       <FontAwesomeIcon icon="ban" />{' '}
            //       <span style={{ display: 'inline' }}>
            //         Louer
            //         {/* <Translate contentKey="entity.action.report">Louer</Translate> */}
            //       </span>
            //     </Button>
            //   ) : (
            //     <Button color="success" size="sm" data-cy="entityUnReportButton" onClick={handleUnreport}>
            //       <FontAwesomeIcon icon="times-circle" />{' '}
            //       <span style={{ display: 'inline' }}>
            //         Se retracter
            //         {/* <Translate contentKey="entity.action.unreport"> Unreport </Translate> */}
            //       </span>
            //     </Button>
            //   )}
            // </div>
            null}
          </div>
        </div>
      )}
    </>
  );
}

export default Room;
