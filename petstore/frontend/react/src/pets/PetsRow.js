import {array} from 'prop-types';
import React from 'react';
import PetCell from './PetCell';

const PetsRow = ({pets}) => (
  <div className="row">
    {pets.map((pet, index) => (
      <PetCell key={index} pet={pet} />
    ))}
  </div>
);

PetsRow.propTypes = {pets: array};

export default PetsRow;
