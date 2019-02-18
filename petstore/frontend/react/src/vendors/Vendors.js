import React, {useEffect, useState} from 'react';
import VendorsTable from './VendorsTable';
import config from '../config';

async function loadVendors(setVendors) {
  try {
    const res = await fetch(`${config.SERVER_URL}/vendors`);
    const vendors = await res.json();
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
