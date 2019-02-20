import {shape, string} from 'prop-types';
import React from 'react';
import {Link} from 'react-router-dom';
import config from '../config';

const getStyle = pet => ({
  backgroundImage: `url(${config.SERVER_URL}/images/${pet.image})`
});

const PetCell = ({pet}) => (
  <div className="col-sm pet-card" style={getStyle(pet)}>
    <Link className="pet-link" to={`/pets/${pet.slug}`}>
      <div className="pet-header">
        <h4>{pet.name}</h4>
      </div>
    </Link>
  </div>
);

PetCell.propTypes = {
  pet: shape({
    image: string,
    name: string,
    slug: string
  })
};

export default PetCell;
