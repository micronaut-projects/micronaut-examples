import React from 'react';
import Comment from './Comment';
import AddComment from './AddComment';
import {array, bool, func, object, shape} from 'prop-types';

const Thread = ({thread, expand, close, reply, submitReply, updateReply}) => (
  <div>
    <div className="card comment-card">
      <Comment
        comment={thread}
        expand={expand}
        close={close}
        expanded={thread.expanded}
        isThread={true}
      />

      {thread.expanded ? (
        <div className="card replies-card">
          {thread.replies ? (
            <div className="card-title comment-title">
              <i>
                {thread.replies.length}{' '}
                {thread.replies.length > 1 ? 'Replies' : 'Reply'}
              </i>
            </div>
          ) : (
            <div className="card-title comment-title">
              <i>Be the first to reply!</i>
            </div>
          )}
          <ul className="list-group list-group-flush">
            {thread.replies &&
              thread.replies.map(r => (
                <li className="list-group-item reply-item" key={r.id}>
                  <Comment comment={r} />
                </li>
              ))}

            <li className="list-group-item comment-form-item">
              <AddComment
                submit={submitReply}
                comment={reply}
                update={updateReply}
                reply={true}
                expanded={true}
              />
            </li>
          </ul>
        </div>
      ) : null}
    </div>
  </div>
);

Thread.propTypes = {
  close: func.isRequired,
  expand: func.isRequired,
  reply: object.isRequired,
  submitReply: func.isRequired,
  thread: shape({
    expanded: bool,
    replies: array
  }).isRequired,
  updateReply: func.isRequired
};

export default Thread;
