import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
<<<<<<< HEAD
import IconButton, { IconButtonProps } from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import { red } from '@mui/material/colors';
import { Link } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';
import Piece from 'app/entities/piece/piece';
import PieceDetail from 'app/entities/piece/piece-detail';
=======
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
import { updateEntity } from 'app/entities/objet/objet.reducer';
import { toast } from 'react-toastify';
import { getEntities } from 'app/entities/utilisateur/utilisateur.reducer';

import './houses.css';
>>>>>>> origin

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

<<<<<<< HEAD
export function House({ id, adresse, description, image, proprietaire, signalant, piece, location}) {
  const [expanded, setExpanded] = React.useState(false);
  const [open, setOpen] = React.useState(false);
  const account = useAppSelector(state => state.authentication.account);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleRent = () => {
    toast.success('Vous avez loué cette maison!');
    handleClose();
  };
  const pieceList = useAppSelector(state => state.piece.entities);
  const locationList = useAppSelector(state => state.location.entities);
  return (
    <>
      <Card sx={{ maxWidth: 345 }}>
        <CardHeader title={adresse} />
        <CardMedia component="img" height="400" image={image} alt="libelle" />
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            {description}
          </Typography>
        </CardContent>
        {account.id === proprietaire.loginId ? (
          <div className="flex flex-row pl-3 justify-center">
            <Button component={Link} to={`/maison/${id}/edit`} color="primary" size="small" data-cy="entityEditButton">
              <FontAwesomeIcon icon="pencil-alt" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.edit">Edit</Translate>
              </span>
            </Button>
            <Button onClick={() => (window.location.href = `/maison/${id}/delete`)} color="error" size="small" data-cy="entityDeleteButton">
              <FontAwesomeIcon icon="trash" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.delete">Delete</Translate>
              </span>
            </Button>
          </div>
        ) : (
          <div className="btn-group flex-btn-group-container">
            <Button onClick={handleClickOpen} color="info" size="small">
              <FontAwesomeIcon icon={faPlus} />{' '}
              <span className="d-none d-md-inline">Plus d'informations</span>
            </Button>
          </div>
        )}
      </Card>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Plus d'informations</DialogTitle>
        <DialogContent>
          <Typography variant="h6">Description:</Typography>
          <Typography variant="body2" color="text.secondary">
            {description}
          </Typography>
          <Typography variant="h6">Prix de location:</Typography>
          <Typography variant="body2" color="text.secondary">
          {locationList.map((location) => (
            location.maison.id === id ? (
              location.prix
            ) : null
          ))}
          </Typography>
          <Typography variant="h6">Photos des pièces:</Typography>
        {pieceList.map((piece) => (
          piece.maison.id === id ? (
            <CardMedia component="img" height="400" image={piece.image} alt={piece.libelle} key={piece.id} />
          ) : null
        ))}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleRent} color="primary">
            Louer cette maison
          </Button>
          <Button onClick={handleClose} color="secondary">
            Fermer
          </Button>
        </DialogActions>
      </Dialog>
=======
export function House({ id, adresse, description, image, proprietaire, pieces }) {
  const [expanded, setExpanded] = React.useState(false);
  const account = useAppSelector(state => state.authentication.account);
  const [rent, setRent] = React.useState(false);
  const rentInit = false;
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const objetEntity = useAppSelector(state => state.objet.entity);
  const [updateUtilisateurs, setUpdateUtilisateurs] = React.useState(false);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);
  const dispatch = useAppDispatch();

  React.useEffect(() => {
    setRent(!pieces.some(piece => piece.etat === 'ATTENTE'));
    dispatch(getEntities({}));
    setUpdateUtilisateurs(true);
  }, []);

  // eslint-disable-next-line complexity

  //   const handleDelete = () => {
  //     toast.success('Votre maison est en location. Dirigez vous vers la page de locations pour en savoir plus!');
  //   };

  const handleRent = () => {
    // setRent(true);
    // toast.success(`La location a été confirmée. Voici le numéro de téléphone du propriétaire en cas de besoin: ${proprietaire.telephone}!`);
  };

  //   React.useEffect(() => {
  //     if (updateSuccess) {

  //     }
  //   }, [updateSuccess]);

  //   const handleUnreport = () => {
  //     setRent(false);
  //   };

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  React.useEffect(() => {
    // console.log("* libelle", libelle);
    // console.log("- proprio", proprietaire.loginId);
    // console.log("- account", account.id);
    // console.log("- rent", rent);
    // console.log("- signalant", signalant);
  }, []);

  return (
    <>
      {(account.id?.toString() === proprietaire.loginId?.toString() ||
        // (signalant.loginId?.toString() === account.id?.toString() && rent) ||
        !rent) && (
        <div key={id} className="house-card">
          <img src={image} alt={description} className="house-image" />
          <div className="house-info">
            <h2>{description}</h2>
            <p>{adresse}:&nbsp;</p>
            {account.id === proprietaire.loginId ? (
              <div style={{ marginRight: '0.5rem', marginBottom: '0.5rem' }}>
                {/* {rent && (
                  <Stack direction="row" spacing={1}>
                    <Chip
                      label="Votre maison est en location! Consulter vos mails!"
                      variant="outlined"
                      color="success"
                      onClick={handleClick}
                      onDelete={handleDelete}
                    />
                  </Stack>
                )} */}
                <div style={{ display: 'flex', justifyContent: 'center' }} className="flex flex-row pl-3 justify-center">
                  {/* <Button tag={Link} to={`/objet/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
                <FontAwesomeIcon icon="eye" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.view">View</Translate>
                </span>
              </Button> */}
                  <Button tag={Link} to={`/maison/${id}/edit/0`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                  <Button
                    onClick={() => (window.location.href = `/maison/${id}/delete`)}
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
              </div>
            ) : (
              <div style={{ display: 'flex', justifyContent: 'center' }} className="absolutemt-2 space-x-2 space-y-2">
                {/* {rent !== true ? ( */}
                <Button
                  tag={Link}
                  to={`/maison/${id}/edit/${utilisateurs.find(it => it.loginId.toString() === account.id.toString())?.id}`}
                  color="success"
                  size="sm"
                  data-cy="entityReportButton"
                  onClick={handleRent}
                >
                  <FontAwesomeIcon icon="pencil-alt" />{' '}
                  <span style={{ display: 'inline' }}>
                    Louer des pièces
                    {/* <Translate contentKey="entity.action.report">Louer</Translate> */}
                  </span>
                </Button>
                {/* ) : ( */}
                {/* <Button color="success" size="sm" data-cy="entityUnReportButton" onClick={handleUnreport}> */}
                {/* <FontAwesomeIcon icon="times-circle" />{' '} */}
                {/* <span style={{ display: 'inline' }}> */}
                {/* Se retracter */}
                {/* <Translate contentKey="entity.action.unreport"> Unreport </Translate> */}
                {/* </span> */}
                {/* </Button> */}
                {/* )} */}
              </div>
            )}
          </div>
        </div>
      )}
>>>>>>> origin
    </>
  );
}

export default House;
