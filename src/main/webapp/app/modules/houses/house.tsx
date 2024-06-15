import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
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

export function House({ id, adresse, description, image, proprietaire, signalant}) {
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
          FCFA
          </Typography>
          <Typography variant="h6">Photos des pièces:</Typography>
          <Piece/>
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
    </>
  );
}

export default House;
