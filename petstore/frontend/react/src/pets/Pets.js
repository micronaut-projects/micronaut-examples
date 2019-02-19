import {shape, string} from 'prop-types';
import React from 'react';
import PetsLayout from './PetsLayout';
import {useFetchState} from '../fetch-util';

function Pets({match}) {
  const [pets] = useFetchState('/pets', []);
  return <PetsLayout pets={pets} match={match} />;
}

Pets.propTypes = {
  match: shape({
    params: shape({
      slug: string
    })
  })
};

export default Pets;
