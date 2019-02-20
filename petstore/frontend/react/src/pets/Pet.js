import {shape, string} from 'prop-types';
import React from 'react';
import {Link} from 'react-router-dom';
import Comments from '../comments/Comments';
import config from '../config';
import {useFetchState} from '../fetch-util';
import Mail from '../mail/Mail';

function Pet({match}) {
  const [pet] = useFetchState(`/pets/${match.params.slug}`);

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
