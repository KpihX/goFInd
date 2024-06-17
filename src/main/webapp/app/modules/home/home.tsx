import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMobileAlt, faRoute, faHome} from '@fortawesome/free-solid-svg-icons';
import { useAppSelector } from 'app/config/store';

const features = [
  {
    icon: faMobileAlt,
    title: 'Gestion des objets de vente',
    description: 'Signalez des objets volés et vérifiez si un objet de revente est issu d\'un vol.',
    image: '/content/images/feature1.jpeg', // Chemin vers l'image de votre choix
    link: '/objects',
  },
  {
    icon: faRoute,
    title: 'Gestion des itinéraires',
    description: 'Proposez des itinéraires et obtenez des clients, ou souscrivez à un itinéraire de voyage.',
    image: '/content/images/feature2.jpeg', // Chemin vers l'image de votre choix
    link: '/itinaries',
  },
  {
    icon: faHome,
    title: 'Gestion des locations',
    description: 'Mettez en location une ou plusieurs pièces de vos maisons, ou louez des pièces disponibles.',
    image: '/content/images/feature3.jpeg', // Chemin vers l'image de votre choix
    link: '/houses',
  },
];
const features2 = [
  {
    icon: faMobileAlt,
    title: 'Gestion des objets de vente',
    description: 'Signalez des objets volés et vérifiez si un objet de revente est issu d\'un vol.',
    image: '/content/images/feature1.jpeg', // Chemin vers l'image de votre choix
    link: '/account/register',
  },
  {
    icon: faRoute,
    title: 'Gestion des itinéraires',
    description: 'Proposez des itinéraires et obtenez des clients, ou souscrivez à un itinéraire de voyage.',
    image: '/content/images/feature2.jpeg', // Chemin vers l'image de votre choix
    link: '/account/register',
  },
  {
    icon: faHome,
    title: 'Gestion des locations',
    description: 'Mettez en location une ou plusieurs pièces de vos maisons, ou louez des pièces disponibles.',
    image: '/content/images/feature3.jpeg', // Chemin vers l'image de votre choix
    link: '/account/register',
  },
];

const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row className="home-container">
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
      

      <div className="col-md-90 text-center my-15">
<h1 className="display-4">
  <FontAwesomeIcon icon={faHome} className="mr-2" />
  <Translate contentKey="home.title">   Bienvenue chez goFind!</Translate>
</h1>
</div>

        <p className="lead">
          <Translate contentKey="home.subtitle">Ceci est votre page d'accueil</Translate>
        </p>
        {account?.login ? (
          <>
            <div>
              <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  Vous êtes connecté en tant qu'utilisateur {account.login}.
                </Translate>
              </Alert>
            </div>
            <div className="features-container">
              {features.map((feature, index) => (
                <Link to={feature.link} key={index} className="feature-card">
                  <div className="feature-icon">
                    <FontAwesomeIcon icon={feature.icon} className="icon" />
                  </div>
                  <div className="feature-content">
                    <img src={feature.image} alt={feature.title} className="feature-image" />
                    <h3 className="feature-title">{feature.title}</h3>
                    <p className="feature-description">{feature.description}</p>
                  </div>
                </Link>
              ))}
            </div>
          </>
        ) : (
          <div>
                     <>
         
            <div className="features-container">
              {features2.map((feature, index) => (
                <Link to={feature.link} key={index} className="feature-card">
                  <div className="feature-icon">
                    <FontAwesomeIcon icon={feature.icon} className="icon" />
                  </div>
                  <div className="feature-content">
                    <img src={feature.image} alt={feature.title} className="feature-image" />
                    <h3 className="feature-title">{feature.title}</h3>
                    <p className="feature-description">{feature.description}</p>
                  </div>
                </Link>
              ))}
            </div>
          </>
            <Alert color="warning" className="register-alert">
              <Translate contentKey="global.messages.info.register.noaccount">Vous n'avez pas encore de compte?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Créer un nouveau compte</Translate>
              </Link>
            </Alert>
          </div>
        )}
        <p>
          <Translate contentKey="home.like">Si vous aimez goFind, n'oubliez pas de nous donner une étoile sur</Translate>{' '}
          <a href="https://github.com/KpihX/goFInd.git" target="_blank" rel="noopener noreferrer" className="gitHub-link">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;
