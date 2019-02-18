import {shape, array, string} from 'prop-types';
import React from 'react';
import {Link} from 'react-router-dom';
import config from '../config';

function VendorCard({vendor}) {
  const imgSrc =
    vendor.pets && vendor.pets.length > 0
      ? vendor.pets[0].image
      : 'missing.png';
  const imgUrl = `${config.SERVER_URL}/images/${imgSrc}`;
  const linkTo = `/pets/vendor/${vendor.name}`;

  return (
    <div className="card vendor-card">
      <Link to={linkTo}>
        <img alt={vendor.name} className="card-img-top" src={imgUrl} />
      </Link>
      <div className="card-body">
        <h5 className="card-title">{vendor.name}</h5>
        <p className="card-text">
          Pets: {vendor.pets ? vendor.pets.length : 0}
        </p>
        <Link to={linkTo} className="btn btn-primary">
          See all Pets
        </Link>
      </div>
    </div>
  );
}

VendorCard.propTypes = {
  vendor: shape({
    name: string,
    pets: array
  })
};

export default VendorCard;
