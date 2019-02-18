import {shape, string} from 'prop-types';
import React, {useEffect, useState} from 'react';
import PetsLayout from './PetsLayout';
import config from '../config';

async function loadPets(setPets) {
  try {
    const url = `${config.SERVER_URL}/pets`;
    const res = await fetch(url);
    const pets = await res.json();
    setPets(pets);
  } catch (e) {
    console.warn(e);
  }
}

function Pets({match}) {
  const [pets, setPets] = useState([]);

  useEffect(() => {
    loadPets(setPets);
  }, []);

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
