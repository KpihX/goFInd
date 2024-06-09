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
  const updateUtilisateurs = useAppSelector(state => state.utilisateur.updateSuccess);
  const updateSuccess = useAppSelector(state => state.objet.updateSuccess);
  const dispatch = useAppDispatch();

  const handleClick = () => {
    // console.info('You clicked the Chip.');
  };

  React.useEffect(() => {
    setFound(etat === 'RETROUVE');
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
    setFound(false);
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
      // console.log("* utilisateurs:", utilisateurs);
      // console.log("* account:", account);
      // console.log("** found:", found);
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
  }, []);

  return (
    <>
      {(account.id?.toString() === proprietaire.loginId?.toString() ||
        (signalant.loginId?.toString() === account.id?.toString() && found) ||
        !found) && (
        <Card sx={{ maxWidth: 345 }}>
          <CardHeader
            // avatar={
            //   <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
            //     R
            //   </Avatar>
            // }
            // action={
            //   <IconButton aria-label="settings">
            //     <MoreVertIcon />
            //   </IconButton>
            // }
            title={libelle}
            subheader={`id:${identifiant}`}
          />
          <CardMedia component="img" height="194" image={image} alt="libelle" />
          <CardContent>
            <p>{proprietaire ? <Link to={`/utilisateur/${proprietaire.id}`}>{proprietaire.id}</Link> : ''}</p>
            <Typography variant="body2" color="text.secondary">
              {description}
            </Typography>
          </CardContent>
          {account.id === proprietaire.loginId ? (
            <>
              {found && (
                <Stack direction="row" spacing={1}>
                  <Chip
                    label="Votre objet a été retrouvé! Veuillez consulter vos mails pour plus d'infos!"
                    variant="outlined"
                    color="success"
                    onClick={handleClick}
                    onDelete={handleDelete}
                  />
                </Stack>
              )}
              <div className="flex flex-row pl-3 justify-center">
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
            </>
          ) : (
            <div className="btn-group flex-btn-group-container">
              {etat === 'VOLE' ? (
                <Button color="warning" size="sm" data-cy="entityReportButton" onClick={handleReport}>
                  <FontAwesomeIcon icon="ban" />{' '}
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.report">Report as Stolen</Translate>
                  </span>
                </Button>
              ) : (
                <Button color="success" size="sm" data-cy="entityUnReportButton" onClick={handleUnreport}>
                  <FontAwesomeIcon icon="times-circle" />{' '}
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.unreport"> Unreport </Translate>
                  </span>
                </Button>
              )}
            </div>
          )}
          {/* <CardActions disableSpacing>
        <IconButton aria-label="add to favorites">
          <FavoriteIcon />
        </IconButton>
        <IconButton aria-label="share">
          <ShareIcon />
        </IconButton>
        <ExpandMore
          expand={expanded}
          onClick={handleExpandClick}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <ExpandMoreIcon />
        </ExpandMore>
      </CardActions> */}
          {/* <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <Typography paragraph>Method:</Typography>
          <Typography paragraph>
            Heat 1/2 cup of the broth in a pot until simmering, add saffron and set
            aside for 10 minutes.
          </Typography>
          <Typography paragraph>
            Heat oil in a (14- to 16-inch) paella pan or a large, deep skillet over
            medium-high heat. Add chicken, shrimp and chorizo, and cook, stirring
            occasionally until lightly browned, 6 to 8 minutes. Transfer shrimp to a
            large plate and set aside, leaving chicken and chorizo in the pan. Add
            pimentón, bay leaves, garlic, tomatoes, onion, salt and pepper, and cook,
            stirring often until thickened and fragrant, about 10 minutes. Add
            saffron broth and remaining 4 1/2 cups chicken broth; bring to a boil.
          </Typography>
          <Typography paragraph>
            Add rice and stir very gently to distribute. Top with artichokes and
            peppers, and cook without stirring, until most of the liquid is absorbed,
            15 to 18 minutes. Reduce heat to medium-low, add reserved shrimp and
            mussels, tucking them down into the rice, and cook again without
            stirring, until mussels have opened and rice is just tender, 5 to 7
            minutes more. (Discard any mussels that don&apos;t open.)
          </Typography>
          <Typography>
            Set aside off of the heat to let rest for 10 minutes, and then serve.
          </Typography>
        </CardContent>
      </Collapse> */}
        </Card>
      )}
    </>
  );
}

export default Object;
