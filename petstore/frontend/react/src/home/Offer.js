import {number, object, shape, string} from 'prop-types';
import React from 'react';
import {Link} from 'react-router-dom';
import config from '../config';
import Price from '../display/Price';
import banner from '../images/banner.png';

const Offer = ({offer}) =>
  offer && offer.pet ? (
    <div id="offers">
      <div
        className="jumbotron jumbotron-fluid offer-jumbotron"
        style={{
          backgroundImage: `url(${config.SERVER_URL}/images/${offer.pet.image})`
        }}
      >
        <div className="container">
          <Link
            style={{textDecoration: 'none', color: 'white'}}
            to={`/pets/${offer.pet.slug}`}
          >
            <h1 className="display-4">{offer.pet.name}</h1>
            <p className="lead">{offer.description}</p>
            <h3>
              <Price price={offer.price} currency={offer.currency} />
            </h3>
            <p>
              <small>
                {offer.pet.vendor} | {offer.pet.type}
              </small>
            </p>
          </Link>
        </div>
      </div>
    </div>
  ) : (
    <div
      className="jumbotron jumbotron-fluid offer-jumbotron-fallback"
      style={{backgroundImage: `url(${banner})`}}
    >
      <div className="container" />
    </div>
  );

Offer.propTypes = {
  offer: shape({
    currency: string,
    description: string,
    pet: object,
    price: number
  })
};

export default Offer;
