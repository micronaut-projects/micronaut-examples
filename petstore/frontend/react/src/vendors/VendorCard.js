import React from 'react'
import config from "../config";
import {Link} from "react-router-dom";
import {shape, array, string}  from 'prop-types'

const VendorCard = ({vendor}) => <div className="card vendor-card">
  <Link to={`/pets/vendor/${vendor.name}`}>
    <img className="card-img-top"
         src={`${config.SERVER_URL}/images/${vendor.pets ? (vendor.pets.length > 0 ? vendor.pets[0].image : 'missing.png') : 'missing.png'}`}
         alt={vendor.name}/>
  </Link>
  <div className="card-body">
    <h5 className="card-title">{vendor.name}</h5>
    <p className="card-text">Pets: {vendor.pets ? vendor.pets.length : 0}</p>
    <Link to={`/pets/vendor/${vendor.name}`} className="btn btn-primary">See all Pets</Link>
  </div>
</div>

VendorCard.propTypes = {
  vendor: shape({
    name: string,
    pets: array
  })
}

export default VendorCard