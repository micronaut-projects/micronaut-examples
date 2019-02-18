import {shape, string} from 'prop-types';
import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import Comments from '../comments/Comments';
import config from '../config';
import Mail from '../mail/Mail';

async function loadPet(slug, setPet) {
  const url = `${config.SERVER_URL}/pets/${slug}`;
  try {
    const res = await fetch(url);
    const pet = await res.json();
    setPet(pet);
  } catch (e) {
    console.warn(e);
  }
}

function Pet({match}) {
  const [pet, setPet] = useState();

  useEffect(() => {
    loadPet(match.params.slug, setPet);
  }, []);

  if (!pet) return <span>Loading...</span>;

  return (
    <div className="row">
      <div className="col-md-6">
        <h1>{pet.name}</h1>
        <h4>Vendor: {pet.vendor}</h4>
        <p>
          <Link to={`/pets/vendor/${pet.vendor}`} className="btn btn-primary">
            More Pets from {pet.vendor}
          </Link>
        </p>

        <Mail pet={pet} />
        <Comments topic={pet.slug} />
      </div>
      <div className="col-md-6">
        <img
          style={{maxWidth: '70%'}}
          src={`${config.SERVER_URL}/images/${pet.image}`}
          alt={pet.name}
        />
      </div>
    </div>
  );
}

Pet.propTypes = {
  match: shape({
    params: shape({
      slug: string
    })
  })
};

export default Pet;
