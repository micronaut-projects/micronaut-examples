import React from 'react';
import aboutImg from '../images/about.png';

const About = () => (
  <div className="text-center">
    <h2>Powered By</h2>
    <img src={aboutImg} alt="micronaut" className="mx-auto d-block about" />
  </div>
);

export default About;
