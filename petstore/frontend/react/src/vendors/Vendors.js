import React, {useEffect, useState} from 'react';
import VendorsTable from './VendorsTable';
import {getJson} from '../fetch-util';

async function loadVendors(setVendors) {
  try {
    const vendors = await getJson('/vendors');
    setVendors(vendors);
  } catch (e) {
    console.warn(e);
  }
}

export default function Vendors() {
  const [vendors, setVendors] = useState([]);

  useEffect(() => {
    loadVendors(setVendors);
  }, []);

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
