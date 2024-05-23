import utilisateur from 'app/entities/utilisateur/utilisateur.reducer';
import objet from 'app/entities/objet/objet.reducer';
import trajet from 'app/entities/trajet/trajet.reducer';
import maison from 'app/entities/maison/maison.reducer';
import piece from 'app/entities/piece/piece.reducer';
import location from 'app/entities/location/location.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  utilisateur,
  objet,
  trajet,
  maison,
  piece,
  location,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
