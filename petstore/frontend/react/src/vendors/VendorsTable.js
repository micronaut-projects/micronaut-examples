import {array} from 'prop-types';
import React from 'react';
import VendorCard from './VendorCard';

const VendorsTable = ({vendors}) => (
  <div>
    {vendors.map((vendor, index) => (
      <VendorCard key={index} vendor={vendor} />
    ))}
  </div>
);

VendorsTable.propTypes = {
  vendors: array.isRequired
};

export default VendorsTable;
