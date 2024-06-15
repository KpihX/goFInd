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
import { updateEntity } from 'app/entities/location/location.reducer';
import { toast } from 'react-toastify';
import { getEntities } from 'app/entities/utilisateur/utilisateur.reducer';

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

export function Location({ id, prix, maison }) {
  const [expanded, setExpanded] = React.useState(false);
  const account = useAppSelector(state => state.authentication.account);

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const locationEntity = useAppSelector(state => state.location.entity);
  const [updateUtilisateurs, setUpdateUtilisateurs] = React.useState(false);
  const updateSuccess = useAppSelector(state => state.location.updateSuccess);
  const dispatch = useAppDispatch();

  const handleClick = () => {
    toast.success('Vous pouvez supprimer votre objet en toute sécurité après être entré en possession!');
  };

//   React.useEffect(() => {
//     setFound(etat === 'RETROUVE');
//     dispatch(getEntities({}));
//     setUpdateUtilisateurs(true);
//   }, []);

  // eslint-disable-next-line complexity
  
    // const prop = utilisateurs.find(it => it.id.toString() === values.proprietaire?.toString());

    // console.log("* values", {...values});

    const entity = {
      // ...objetEntity,
      id,
      prix,
      maison
    };

    // console.log("* entity:", entity)

   

  const handleDelete = () => {
    toast.success('Vous pouvez supprimer votre objet en toute sécurité après être entré en possession!');
  };

  
//   React.useEffect(() => {
//     if (updateSuccess && found !== foundInit) {
//       if (found) {
//         toast.success(
//           `Merci de votre collaboration. L'alerte a bien été envoyé au propriétaire. Voici son numéro de téléphone si vous tenez à prendre les devants: ${proprietaire.telephone}!`,
//         );
//       } else {
//         toast.success("L'alerte a été annulée!");
//       }
//     }
//   }, [updateSuccess, found]);

 

//   React.useEffect(() => {
//     if (found === foundInit) {
//       return;
//     }
//     if (!found) {
//       // console.log("** found:", found);
//       saveEntity({
//         etatNew: 'VOLE',
//         signalantNew: null,
//       });
//     } else {
//       const signalantNew = utilisateurs.find(it => it.loginId.toString() === account.id.toString());
//       console.log('* utilisateurs:', utilisateurs);
//       console.log('* account:', account);
//       console.log('** found:', found);
//       saveEntity({
//         etatNew: 'RETROUVE',
//         signalantNew,
//       });
//     }
//   }, [found, updateUtilisateurs]);

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
            title={id}
            subheader={`id:${maison}`}
          />
         
          <CardContent>
            {/* <p>{proprietaire ? <Link to={`/utilisateur/${proprietaire.id}`}>{proprietaire.login}</Link> : ''}</p> */}
            <Typography variant="body2" color="text.secondary">
              {prix}
            </Typography>
          </CardContent>
          
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
      
    </>
  );
}

export default Location;
