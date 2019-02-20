import React, {useEffect, useRef, useState} from 'react';
import FeaturedPet from './FeaturedPet';
import Offer from './Offer';
import config from '../config';
import Alert from '../display/Alert';

export default function Home() {
  const [error, setError] = useState();
  const [offer, setOffer] = useState();
  const source = useRef();

  useEffect(() => {
    source.current = new EventSource(`${config.SERVER_URL}/offers`);
    source.current.onmessage = e => {
      setOffer(JSON.parse(e.data));
      setError(null);
    };
    source.current.onerror = () => {
      setError('Could not load offers');
      setOffer(null);
    };
    return () => source.current.close();
  }, []);

  return (
    <div>
      <Offer offer={offer} />
      <Alert message={error} level="warning" />

      <h2>Check out our Popular Pets!</h2>
      <div className="row">
        <div className="col-md-6">
          <FeaturedPet />
        </div>
        <div className="col-md-6">
          <FeaturedPet />
        </div>
      </div>
    </div>
  );
}
