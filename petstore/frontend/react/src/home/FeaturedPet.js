import React from 'react';
import {Link} from 'react-router-dom';
import config from '../config';
import {useFetchState} from '../fetch-util';

export default function FeaturedPet() {
  const [pet] = useFetchState('/pets/random');

  if (!pet) return null;

  return (
    <div className="card featured-card">
      <Link to={`/pets/${pet.slug}`}>
        <img
          className="card-img-top"
          src={`${config.SERVER_URL}/images/${pet.image}`}
          alt={pet.name}
        />
      </Link>
      <div className="card-body">
        <h5 className="card-title">{pet.name}</h5>
        <Link to={`/pets/${pet.slug}`} className="btn btn-primary">
          More Info
        </Link>
      </div>
    </div>
  );
}
