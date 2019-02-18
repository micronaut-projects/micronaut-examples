import React from 'react';
import {bool, func, shape, string} from 'prop-types';

const getButton = (expand, close, expanded) => {
  const level = expanded ? 'danger' : 'success';
  const text = expanded ? '-' : '+';
  const onClick = expanded ? close : expand;
  return (
    <button className={`btn btn-${level} comment-btn`} onClick={onClick}>
      {text}
    </button>
  );
};

const Comment = ({comment, isThread, expand, close, expanded}) => (
  <div className="card-body">
    <div className="row">
      <div className="col-sm-10">
        <h6 className="card-title">
          <b>{comment.poster}</b> said:
        </h6>
        <p className="card-text">{comment.content}</p>
      </div>
      <div className="col-sm-2">
        {isThread ? getButton(expand, close, expanded) : null}
      </div>
    </div>
  </div>
);

Comment.propTypes = {
  close: func,
  comment: shape({
    content: string,
    poster: string
  }),
  expand: func,
  expanded: bool,
  isThread: bool
};

export default Comment;
