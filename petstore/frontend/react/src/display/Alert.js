import React from 'react';
import {string} from 'prop-types';

function Alert({message, level}) {
  if (!message) return null;

  const className = `alert alert-${level ||
    'info'} alert-dismissible fade show`;
  return (
    <div className={className} role="alert">
      {message}
      <button className="close" data-dismiss="alert" aria-label="Close">
        <span aria-hidden="true">×</span>
      </button>
    </div>
  );
}

Alert.propTypes = {
  level: string,
  message: string
};

export default Alert;
