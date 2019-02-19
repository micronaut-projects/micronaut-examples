/* eslint-disable react/no-unescaped-entities */
import {shape, string} from 'prop-types';
import React, {useCallback, useEffect, useState} from 'react';
import Alert from '../display/Alert';
import {getJson, postJson} from '../fetch-util';

const checkHealth = async setEnabled => {
  try {
    const json = await getJson('/mail/health');
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
        const res = await postJson('/mail/send', {email, slug: pet.slug});
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

  if (!enabled) return null;

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
