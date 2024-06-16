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

export function Room({ id, libelle, image, etat, proprietaire }) {
  const [expanded, setExpanded] = React.useState(false);
  const account = useAppSelector(state => state.authentication.account);
  const [rent, setRent]: [boolean, any] = React.useState(false);
  const rentInit = false;
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const objetEntity = useAppSelector(state => state.objet.entity);
  const [updateUtilisateurs, setUpdateUtilisateurs] = React.useState(false);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);
  const dispatch = useAppDispatch();

  const handleClick = () => {
    toast.success('Votre maison est en location. Dirigez vous vers la page de locations pour en savoir plus!');
  };

  React.useEffect(() => {
    setRent(false);
    dispatch(getEntities({}));
    setUpdateUtilisateurs(true);
  }, []);

  // eslint-disable-next-line complexity
  const saveEntity = () => {
    // const prop = utilisateurs.find(it => it.id.toString() === values.proprietaire?.toString());

    // console.log("* values", {...values});

    // const entity = {
    //   // ...objetEntity,

    // };

    // console.log("* entity:", entity)

    // console.log("* rent:", rent);
    const rentStatus = rent ? 'report' : 'unreport';
    // console.log("* report:", report);
    // dispatch(updateEntity({ entity, rentStatus }));
  };

  const handleDelete = () => {
    toast.success('Votre maison est en location. Dirigez vous vers la page de locations pour en savoir plus!');
  };

  const handleRent = () => {
    setRent(true);
  };

  React.useEffect(() => {
    if (updateSuccess && rent !== rentInit) {
      if (rent) {
        toast.success(
          `La location a été confirmée. Voici le numéro de téléphone du propriétaire en cas de besoin: ${proprietaire.telephone}!`,
        );
      } else {
        toast.success('La location a été annulée!');
      }
    }
  }, [updateSuccess, rent]);

  const handleUnreport = () => {
    setRent(false);
  };

  React.useEffect(() => {
    if (rent === rentInit) {
      return;
    }
    if (!rent) {
      // console.log("** rent:", rent);
      //   saveEntity();
    } else {
      const signalantNew = utilisateurs.find(it => it.loginId.toString() === account.id.toString());
      // console.log('* utilisateurs:', utilisateurs);
      // console.log('* account:', account);
      // console.log('** rent:', rent);
      //   saveEntity();
    }
  }, [rent, updateUtilisateurs]);

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
      {/* {(account.id?.toString() === proprietaire.loginId?.toString() ||
        (signalant.loginId?.toString() === account.id?.toString() && rent) || */}
      {!rent && (
        <div key={id} className="house-card">
          <img src={image} alt={libelle} className="house-image" />
          <div className="house-info">
            <h2>{libelle}</h2>
            {/* {account.id === proprietaire.loginId ? ( */}
            {true ? (
              <div style={{ marginRight: '0.5rem', marginBottom: '0.5rem' }}>
                {rent && (
                  <Stack direction="row" spacing={1}>
                    <Chip
                      label="Votre maison est en location! Consulter vos mails!"
                      variant="outlined"
                      color="success"
                      onClick={handleClick}
                      onDelete={handleDelete}
                    />
                  </Stack>
                )}
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
              </div>
            ) : (
              <div style={{ display: 'flex', justifyContent: 'center' }} className="absolutemt-2 space-x-2 space-y-2">
                {rent !== true ? (
                  <Button color="warning" size="sm" data-cy="entityReportButton" onClick={handleRent}>
                    <FontAwesomeIcon icon="ban" />{' '}
                    <span style={{ display: 'inline' }}>
                      Louer
                      {/* <Translate contentKey="entity.action.report">Louer</Translate> */}
                    </span>
                  </Button>
                ) : (
                  <Button color="success" size="sm" data-cy="entityUnReportButton" onClick={handleUnreport}>
                    <FontAwesomeIcon icon="times-circle" />{' '}
                    <span style={{ display: 'inline' }}>
                      Se retracter
                      {/* <Translate contentKey="entity.action.unreport"> Unreport </Translate> */}
                    </span>
                  </Button>
                )}
              </div>
            )}
          </div>
        </div>
      )}
    </>
  );
}

export default Room;