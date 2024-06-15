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
import { updateEntity } from 'app/entities/objet/objet.reducer';
import { toast } from 'react-toastify';
import { getEntities } from 'app/entities/utilisateur/utilisateur.reducer';

import './object.css';

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

export function Object({ id, libelle, description, identifiant, image, etat, type, proprietaire, signalant }) {
  const [expanded, setExpanded] = React.useState(false);
  const account = useAppSelector(state => state.authentication.account);
  const [found, setFound]: [boolean, any] = React.useState(etat === 'RETROUVE');
  const foundInit = etat === 'RETROUVE';
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const objetEntity = useAppSelector(state => state.objet.entity);
  const [updateUtilisateurs, setUpdateUtilisateurs] = React.useState(false);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);
  const dispatch = useAppDispatch();

  const handleClick = () => {
    toast.success('Vous pouvez supprimer votre objet en toute sécurité après être entré en possession!');
  };

  React.useEffect(() => {
    setFound(etat === 'RETROUVE');
    dispatch(getEntities({}));
    setUpdateUtilisateurs(true);
  }, []);

  // eslint-disable-next-line complexity
  const saveEntity = ({ etatNew, signalantNew }) => {
    // const prop = utilisateurs.find(it => it.id.toString() === values.proprietaire?.toString());

    // console.log("* values", {...values});

    const entity = {
      // ...objetEntity,
      id,
      libelle,
      description,
      identifiant,
      image,
      etat: etatNew,
      type,
      proprietaire,
      proprietaireId: proprietaire.id,
      signalant: signalantNew,
      signalantId: signalantNew ? signalantNew.id : null,
    };

    // console.log("* entity:", entity)

    // console.log("* found:", found);
    const report = found ? 'report' : 'unreport';
    // console.log("* report:", report);
    dispatch(updateEntity({ entity, report }));
  };

  const handleDelete = () => {
    toast.success('Vous pouvez supprimer votre objet en toute sécurité après être entré en possession!');
  };

  const handleReport = () => {
    setFound(true);
  };

  React.useEffect(() => {
    if (updateSuccess && found !== foundInit) {
      if (found) {
        toast.success(
          `Merci de votre collaboration. L'alerte a bien été envoyé au propriétaire. Voici son numéro de téléphone si vous tenez à prendre les devants: ${proprietaire.telephone}!`,
        );
      } else {
        toast.success("L'alerte a été annulée!");
      }
    }
  }, [updateSuccess, found]);

  const handleUnreport = () => {
    setFound(false);
  };

  React.useEffect(() => {
    if (found === foundInit) {
      return;
    }
    if (!found) {
      // console.log("** found:", found);
      saveEntity({
        etatNew: 'VOLE',
        signalantNew: null,
      });
    } else {
      const signalantNew = utilisateurs.find(it => it.loginId.toString() === account.id.toString());
      // console.log('* utilisateurs:', utilisateurs);
      // console.log('* account:', account);
      // console.log('** found:', found);
      saveEntity({
        etatNew: 'RETROUVE',
        signalantNew,
      });
    }
  }, [found, updateUtilisateurs]);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  React.useEffect(() => {
    // console.log("* libelle", libelle);
    // console.log("- proprio", proprietaire.loginId);
    // console.log("- account", account.id);
    // console.log("- found", found);
    // console.log("- signalant", signalant);
  }, []);

  return (
    <>
      {(account.id?.toString() === proprietaire.loginId?.toString() ||
        (signalant.loginId?.toString() === account.id?.toString() && found) ||
        !found) && (
        <div key={id} className="house-card">
          <img src={image} alt={libelle} className="house-image" />
          <div className="house-info">
            <h2>{libelle}</h2>
            <p>ID:&nbsp;{identifiant}</p>
            <p>{description}</p>
            {account.id === proprietaire.loginId ? (
              <div style={{ marginRight: '0.5rem', marginBottom: '0.5rem' }}>
                {found && (
                  <Stack direction="row" spacing={1}>
                    <Chip
                      label="Votre objet a été retrouvé! Consulter vos mails!"
                      variant="outlined"
                      color="success"
                      onClick={handleClick}
                      onDelete={handleDelete}
                    />
                  </Stack>
                )}
                <div style={{ display: 'flex', justifyContent: 'center' }} className="flex flex-row pl-3 justify-center">
                  {/* <Button tag={Link} to={`/objet/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
                <FontAwesomeIcon icon="eye" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.view">View</Translate>
                </span>
              </Button> */}
                  <Button tag={Link} to={`/objet/${id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                  <Button
                    onClick={() => (window.location.href = `/objet/${id}/delete`)}
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
                {etat === 'VOLE' ? (
                  <Button color="warning" size="sm" data-cy="entityReportButton" onClick={handleReport}>
                    <FontAwesomeIcon icon="ban" />{' '}
                    <span style={{ display: 'inline' }}>
                      <Translate contentKey="entity.action.report">Report as Stolen</Translate>
                    </span>
                  </Button>
                ) : (
                  <Button color="success" size="sm" data-cy="entityUnReportButton" onClick={handleUnreport}>
                    <FontAwesomeIcon icon="times-circle" />{' '}
                    <span style={{ display: 'inline' }}>
                      <Translate contentKey="entity.action.unreport"> Unreport </Translate>
                    </span>
                  </Button>
                )}
              </div>
            )}
          </div>
        </div>
        // <Card sx={{ maxWidth: 345 }}>
        //   <CardHeader
        //     // avatar={
        //     //   <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
        //     //     R
        //     //   </Avatar>
        //     // }
        //     // action={
        //     //   <IconButton aria-label="settings">
        //     //     <MoreVertIcon />
        //     //   </IconButton>
        //     // }
        //     title={libelle}
        //     subheader={`id:${identifiant}`}
        //   />
        //   <CardMedia component="img" height="400" image={image} alt="libelle" />
        //   <CardContent>
        //     {/* <p>{proprietaire ? <Link to={`/utilisateur/${proprietaire.id}`}>{proprietaire.login}</Link> : ''}</p> */}
        //     <Typography variant="body2" color="text.secondary">
        //       {description}
        //     </Typography>
        //   </CardContent>
        //   {account.id === proprietaire.loginId ? (
        //     <>
        //       {found && (
        //         <Stack direction="row" spacing={1}>
        //           <Chip
        //             label="Votre objet a été retrouvé! Consulter vos mails!"
        //             variant="outlined"
        //             color="success"
        //             onClick={handleClick}
        //             onDelete={handleDelete}
        //           />
        //         </Stack>
        //       )}
        //       <div style={{ display: 'flex', justifyContent: 'center' }} className="flex flex-row pl-3 justify-center">
        //         {/* <Button tag={Link} to={`/objet/${id}`} color="info" size="sm" data-cy="entityDetailsButton">
        //         <FontAwesomeIcon icon="eye" />{' '}
        //         <span className="d-none d-md-inline">
        //           <Translate contentKey="entity.action.view">View</Translate>
        //         </span>
        //       </Button> */}
        //         <Button tag={Link} to={`/objet/${id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
        //           <FontAwesomeIcon icon="pencil-alt" />{' '}
        //           <span className="d-none d-md-inline">
        //             <Translate contentKey="entity.action.edit">Edit</Translate>
        //           </span>
        //         </Button>
        //         <Button
        //           onClick={() => (window.location.href = `/objet/${id}/delete`)}
        //           color="danger"
        //           size="sm"
        //           data-cy="entityDeleteButton"
        //         >
        //           <FontAwesomeIcon icon="trash" />{' '}
        //           <span className="d-none d-md-inline">
        //             <Translate contentKey="entity.action.delete">Delete</Translate>
        //           </span>
        //         </Button>
        //       </div>
        //     </>
        //   ) : (
        //     <div style={{ display: 'flex', justifyContent: 'center' }}>
        //       {etat === 'VOLE' ? (
        //         <Button color="warning" size="sm" data-cy="entityReportButton" onClick={handleReport}>
        //           <FontAwesomeIcon icon="ban" />{' '}
        //           <span style={{ display: 'inline' }}>
        //             <Translate contentKey="entity.action.report">Report as Stolen</Translate>
        //           </span>
        //         </Button>
        //       ) : (
        //         <Button color="success" size="sm" data-cy="entityUnReportButton" onClick={handleUnreport}>
        //           <FontAwesomeIcon icon="times-circle" />{' '}
        //           <span style={{ display: 'inline' }}>
        //             <Translate contentKey="entity.action.unreport"> Unreport </Translate>
        //           </span>
        //         </Button>
        //       )}
        //     </div>
        //   )}
      )}
    </>
  );
}

export default Object;
