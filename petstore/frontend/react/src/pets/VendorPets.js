import {shape, string} from 'prop-types';
import React, {useEffect, useState} from 'react';
import PetsLayout from './PetsLayout';
import {getJson} from '../fetch-util';

async function loadPets(vendor, setPets) {
  try {
    const pets = await getJson(`/pets/vendor/${vendor}`);
    setPets(pets);
  } catch (e) {
    console.warn(e);
  }
}

function VendorPets({match}) {
  const [pets, setPets] = useState([]);

  const {vendor} = match.params;

  useEffect(() => {
    loadPets(vendor, setPets);
  }, []);

  return (
    <PetsLayout header={`Pets from ${vendor}`} match={match} pets={pets} />
  );
}

VendorPets.propTypes = {
  match: shape({
    params: shape({
      vendor: string
    })
  })
};

export default VendorPets;
