import {array} from 'prop-types';
import React from 'react';
import PetsRow from './PetsRow';

function PetsGrid({pets}) {
  const groupByThree = (array, pet, index) => {
    const rowIndex = Math.floor(index / 3);
    if (!array[rowIndex]) array[rowIndex] = [];
    array[rowIndex].push(pet);
    return array;
  };

  const rows = pets.reduce(groupByThree, []);

  return (
    <div>
      {rows.map((row, index) => (
        <PetsRow key={index} pets={row} />
      ))}
    </div>
  );
}

PetsGrid.propTypes = {
  pets: array
};

export default PetsGrid;
