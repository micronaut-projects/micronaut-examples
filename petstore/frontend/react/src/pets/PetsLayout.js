/* eslint-disable jsx-a11y/click-events-have-key-events */
/* eslint-disable jsx-a11y/no-static-element-interactions */
import {array, shape, string} from 'prop-types';
import React, {useState} from 'react';
import {Route} from 'react-router-dom';
import PetsGrid from './PetsGrid';

function petsForTab(index, pets) {
  const type = index === 1 ? 'CAT' : index === 2 ? 'DOG' : null;
  return type ? pets.filter(p => p.type === type) : pets;
}

function PetsLayout({header, match, pets}) {
  const [tab, setTab] = useState(1);

  const render = () => (
    <div>
      <div className="jumbotron jumbotron-fluid">
        <div className="container">
          <h1 className="display-4">{header || 'Pets'}</h1>
        </div>
      </div>

      <ul className="nav nav-tabs">
        <li className="nav-item">
          <span
            className={`nav-link ${tab === 1 ? 'active' : ''}`}
            onClick={() => setTab(1)}
          >
            Cats
          </span>
        </li>
        <li className="nav-item">
          <span
            className={`nav-link ${tab === 2 ? 'active' : ''}`}
            onClick={() => setTab(2)}
          >
            Dogs
          </span>
        </li>
      </ul>

      <PetsGrid pets={petsForTab(tab, pets)} />
    </div>
  );

  return <Route exact path={match.url} render={render} />;
}

PetsLayout.propTypes = {
  header: string,
  match: shape({
    url: string
  }),
  pets: array
};

export default PetsLayout;
