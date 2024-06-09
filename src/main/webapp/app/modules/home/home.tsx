import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">
          <Translate contentKey="home.title">Welcome, on goFind!</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle">This is your homepage</Translate>
        </p>
        {account?.login ? (
          <>
            <div>
              <Alert color="success">
                <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as user {account.login}.
                </Translate>
              </Alert>
            </div>
            <div>
              {/* <Alert color="warning">
            <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

            <Link to="/login" className="alert-link">
              <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
            </Link>
            <Translate contentKey="global.messages.info.authenticated.suffix">
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
            </Translate>
          </Alert> */}

              <p className="lead">
                <p>Concrètement, goFind permet:</p>
                <ol>
                  <li>
                    <p>La gestion des objets de vente (téléphone, laptop, ..), ceci dans la mésure où vous donne la possibilité de: </p>
                    <ul>
                      <li>signaler un objet volé (téléphone, laptop, ..) ;</li>
                      <li>vérifier si un objet de revente est issu d'un vol ou pas.</li>
                    </ul>
                  </li>
                  <li>
                    <p>La gestion des itinéraires, ceci dans la mésure où vous donne la possibilité de: </p>
                    <ul>
                      <li>proposer un itinéraire et avoir des clients ;</li>
                      <li>de souscrire à un itinéraire de voyage.</li>
                    </ul>
                  </li>
                  <li>
                    <p>La gestion des locations, ceci dans la mésure où vous donne la possibilité de: </p>
                    <ul>
                      <li>mettre une ou plusieurs pièces d'une de vos maisons en location ;</li>
                      <li>louer une ou plusieurs pièces d'une maison en location</li>
                    </ul>
                  </li>
                </ol>
              </p>
            </div>
          </>
        ) : (
          <div>
            {/* <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert> */}

            <p className="lead">
              <p>Concrètement, goFind permet:</p>
              <ol>
                <li>
                  <p>La gestion des objets de vente (téléphone, laptop, ..), ceci dans la mésure où vous donne la possibilité de: </p>
                  <ul>
                    <li>signaler un objet volé (téléphone, laptop, ..) ;</li>
                    <li>vérifier si un objet de revente est issu d'un vol ou pas.</li>
                  </ul>
                </li>
                <li>
                  <p>La gestion des itinéraires, ceci dans la mésure où vous donne la possibilité de: </p>
                  <ul>
                    <li>proposer un itinéraire et avoir des clients ;</li>
                    <li>de souscrire à un itinéraire de voyage.</li>
                  </ul>
                </li>
                <li>
                  <p>La gestion des locations, ceci dans la mésure où vous donne la possibilité de: </p>
                  <ul>
                    <li>mettre une ou plusieurs pièces d'une de vos maisons en location ;</li>
                    <li>louer une ou plusieurs pièces d'une maison en location</li>
                  </ul>
                </li>
              </ol>
            </p>

            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
          </div>
        )}
        {/* <p>
          <Translate contentKey="home.question">If you have any question on JHipster:</Translate>
        </p> */}

        {/* <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.homepage">JHipster homepage</Translate>
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.stackoverflow">JHipster on Stack Overflow</Translate>
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.bugtracker">JHipster bug tracker</Translate>
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.chat">JHipster public chat room</Translate>
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              <Translate contentKey="home.link.follow">follow @jhipster on Twitter</Translate>
            </a>
          </li>
        </ul> */}

        <p>
          <Translate contentKey="home.like">If you like JHipster, do not forget to give us a star on</Translate>{' '}
          <a href="https://github.com/KpihX/goFInd.git" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;
