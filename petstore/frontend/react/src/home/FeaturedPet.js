import React, {useEffect, useState} from 'react';
import config from '../config';
import {Link} from 'react-router-dom';

const loadPet = async setPet => {
  const url = `${config.SERVER_URL}/pets/random`;
  try {
    const res = await fetch(url);
    const pet = await res.json();
    setPet(pet);
  } catch (e) {
    console.warn(e);
  }
};

export default function FeaturedPet() {
  const [pet, setPet] = useState(null);

  useEffect(() => {
    loadPet(setPet);
  }, []);

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
