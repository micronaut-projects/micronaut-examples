import {shape, string} from 'prop-types';
import React from 'react';
import PetsLayout from './PetsLayout';
import {useFetchState} from '../fetch-util';

function VendorPets({match}) {
  const {vendor} = match.params;
  const [pets] = useFetchState(`/pets/vendor/${vendor}`, []);

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
