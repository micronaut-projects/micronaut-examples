import React from 'react';
import VendorsTable from './VendorsTable';
import {useFetchState} from '../fetch-util';

export default function Vendors() {
  const [vendors] = useFetchState('/vendors', []);

  return (
    <div>
      <div className="jumbotron jumbotron-fluid">
        <div className="container">
          <h1 className="display-4">Vendors</h1>
        </div>
      </div>
      <VendorsTable vendors={vendors} />
    </div>
  );
}
