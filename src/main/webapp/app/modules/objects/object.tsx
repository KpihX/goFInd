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
import { green } from '@mui/material/colors';
import FavoriteIcon from '@mui/icons-material/Favorite';
import ShareIcon from '@mui/icons-material/Share';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import { Button } from 'reactstrap';
import { Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { toast } from 'react-toastify';
import { getEntities, updateEntity } from 'app/entities/objet/objet.reducer';
import { Link } from 'react-router-dom';

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

  const saveEntity = ({ etatNew, signalantNew }) => {
    const entity = {
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

    const report = found ? 'report' : 'unreport';
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
      saveEntity({
        etatNew: 'VOLE',
        signalantNew: null,
      });
    } else {
      const signalantNew = utilisateurs.find(it => it.loginId.toString() === account.id.toString());
      saveEntity({
        etatNew: 'RETROUVE',
        signalantNew,
      });
    }
  }, [found, updateUtilisateurs]);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  return (
    <>
      {(account.id?.toString() === proprietaire.loginId?.toString() ||
        (signalant.loginId?.toString() === account.id?.toString() && found) ||
        !found) && (
        <Card sx={{ maxWidth: 345, backgroundColor: green[50], borderRadius: '15px' }}>
          <CardHeader
            title={libelle}
            subheader={`id:${identifiant}`}
            avatar={<Avatar sx={{ bgcolor: green[500] }}>{libelle.charAt(0)}</Avatar>}
          />
          <CardMedia component="img" height="400" image={image} alt="libelle" sx={{ borderRadius: '0 0 15px 15px' }} />
          <CardContent>
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
                <Button tag={Link} to={`/objet/${id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                  <FontAwesomeIcon icon="pencil-alt" />{' '}
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.edit">Edit</Translate>
                  </span>
                </Button>
                <Button onClick={() => (window.location.href = `/objet/${id}/delete`)} color="danger" size="sm" data-cy="entityDeleteButton">
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
        </Card>
      )}
    </>
  );
}

export default Object;
