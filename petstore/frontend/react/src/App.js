import React from 'react';
import {BrowserRouter as Router, Route, Link} from 'react-router-dom';

import About from './about/About';
import Home from './home/Home';
import Pet from './pets/Pet';
import Pets from './pets/Pets';
import VendorPets from './pets/VendorPets';
import Vendors from './vendors/Vendors';

import logo from './images/logo.png';
import './App.css';

const App = () => (
  <Router>
    <div className="App">
      <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <Link to="/" className="navbar-brand">
          <img src={logo} className="micronaut-logo" alt="micronaut" />{' '}
          Micronaut PetStore
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link to="/" className="nav-link">
                Home
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/pets" className="nav-link">
                Pets
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/vendors" className="nav-link">
                Vendors
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/about" className="nav-link">
                About
              </Link>
            </li>
          </ul>
        </div>
      </nav>

      <div className="container">
        <Route exact path="/" component={Home} />
        <Route exact path="/pets" component={Pets} />
        <Route exact path="/pets/:slug" component={Pet} />
        <Route exact path="/pets/vendor/:vendor" component={VendorPets} />
        <Route exact path="/vendors" component={Vendors} />
        <Route exact path="/about" component={About} />
      </div>
    </div>
  </Router>
);

export default App;
