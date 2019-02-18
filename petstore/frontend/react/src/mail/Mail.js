/* eslint-disable react/no-unescaped-entities */
import {shape, string} from 'prop-types';
import React, {useCallback, useEffect, useState} from 'react';
import config from '../config';
import Alert from '../display/Alert';

const checkHealth = async setEnabled => {
  const url = `${config.SERVER_URL}/mail/health`;
  try {
    const res = await fetch(url);
    const json = await res.json();
    setEnabled(json.status === 'UP');
  } catch (e) {
    console.warn(e);
  }
};

function Mail({pet}) {
  const [enabled, setEnabled] = useState(false);
  const [email, setEmail] = useState('');
  const [level, setLevel] = useState('');
  const [message, setMessage] = useState('');

  if (!enabled) return null;

  useEffect(() => {
    checkHealth(setEnabled);
  }, []);

  const changeEmail = useCallback(event => setEmail(event.target.value), []);

  const displayError = useCallback(() => {
    setLevel('warning');
    setMessage('Enter a valid email address');
  }, []);

  const submitEmail = useCallback(
    async event => {
      event.preventDefault();

      try {
        const url = `${config.SERVER_URL}/mail/send`;
        const res = await fetch(url, {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({email, slug: pet.slug})
        });
        if (res.status === 200) {
          setEmail('');
          setLevel('success');
          setMessage('Email has been sent');
        } else {
          setLevel('warning');
          setMessage('Could not send email');
        }
      } catch (e) {
        console.warn(e);
      }
    },
    [email, pet]
  );

  const valid =
    email.length > 0 && email.match(/^([\w.%+-]+)@([\w-]+\.)+([\w]{2,})$/i);

  return (
    <div>
      <div className="jumbotron">
        <h4>Request Info about {pet.name}</h4>
        <form
          className="form-group"
          onSubmit={valid ? submitEmail : displayError}
        >
          <label htmlFor="inputEmail">Email address</label>
          <input
            type="email"
            className="form-control"
            name="email"
            id="inputEmail"
            placeholder="Enter email"
            onChange={changeEmail}
            value={email}
          />
          <small id="emailHelp" className="form-text text-muted">
            We'll never share your email with anyone else.
          </small>
          <br />
          <input
            type="submit"
            className={`btn btn-primary ${valid ? '' : 'disabled'}`}
            value="Send me info"
          />
        </form>
      </div>
      <Alert message={message} level={level} />
    </div>
  );
}

Mail.propTypes = {
  pet: shape({
    name: string
  })
};

export default Mail;
