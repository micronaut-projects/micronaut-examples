import React from 'react';
import {number, string} from 'prop-types';

const currencyMap = {USD: '$', EUR: '€', GBP: '£'};

const Price = ({currency, price}) => (
  <span className="badge badge-secondary">
    {currencyMap[currency] || ''}
    {price.toFixed(2)}
  </span>
);

Price.propTypes = {
  currency: string,
  price: number.isRequired
};

export default Price;
