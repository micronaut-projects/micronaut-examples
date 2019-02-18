import React, {Component} from 'react';
import {string} from 'prop-types';
import AddComment from './AddComment';
import Thread from './Thread';
import config from '../config';

class Comments extends Component {
  state = {
    enabled: false,
    reply: {
      content: '',
      poster: ''
    },
    threads: []
  };

  async componentDidMount() {
    try {
      const url = `${config.SERVER_URL}/comment/health`;
      const res = await fetch(url);
      const json = await res.json();
      const isEnabled = json.status === 'UP';
      this.setState({enabled: isEnabled});
      if (isEnabled) this.fetchThreads();
    } catch (e) {
      console.warn(e);
    }
  }

  addReply = async (e, id) => {
    e.preventDefault();
    const {topic, reply} = this.state;
    const url = `${config.SERVER_URL}/comment/${topic}/${id}`;
    try {
      const res = await fetch(url, {
        method: 'POST',
        body: JSON.stringify(reply),
        headers: {'Content-Type': 'application/json'}
      });
      if (res.status === 201) {
        this.expandThread(id);
      } else {
        console.warn('Could not post reply');
      }
    } catch (e) {
      console.warn(e);
    }
  };

  clearReply = () => this.setState({reply: {poster: '', content: ''}});

  closeThread = id => {
    const threads = this.state.threads.map(t => {
      if (t.id === id) {
        t.expanded = false;
        t.replies = [];
      }
      return t;
    });
    this.setState({threads});
  };

  createThread = async e => {
    e.preventDefault();

    const {topic} = this.props;
    const thread = this.state.reply;

    const url = `${config.SERVER_URL}/comment/${topic}`;
    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(thread)
      });
      if (res.status === 201) {
        this.fetchThreads();
      } else {
        console.warn('Could not post thread');
      }
    } catch (e) {
      console.warn(e);
    }
  };

  expandForm = e => {
    e.preventDefault();
    let threads = this.state.threads.map(t => {
      t.replies = [];
      t.expanded = false;
      return t;
    });
    this.setState({threads});
    this.clearReply();
  };

  expandThread = async id => {
    const {topic} = this.props;

    const url = `${config.SERVER_URL}/comment/${topic}/${id}`;
    try {
      const res = await fetch(url);
      const json = await res.json();

      const {threads} = this.state;
      const newThreads = threads.map(t => {
        t.replies = t.id === id ? json.replies : [];
        t.expanded = t.id === id;
        return t;
      });

      this.setState({threads: newThreads});
      this.clearReply();
    } catch (e) {
      console.warn(e);
    }
  };

  fetchThreads = async () => {
    const {topic} = this.props;
    const url = `${config.SERVER_URL}/comment/${topic}`;
    try {
      const res = await fetch(url);
      const threads = await res.json();
      this.setState({threads});
      this.clearReply();
    } catch (e) {
      console.warn(e);
    }
  };

  threadIsExpanded = () => !this.state.threads.some(t => t.expanded);

  updateReply = e => {
    let {reply} = this.state;
    const {name, value} = e.target;
    const newReply = {...reply, [name]: value};
    this.setState({reply: newReply});
  };

  render() {
    const {threads, reply, enabled} = this.state;

    return enabled ? (
      <div>
        <h2>Join the discussion!</h2>

        {threads.length > 0
          ? threads.map(t => (
              <Thread
                thread={t}
                key={t.id}
                expand={() => this.expandThread(t.id)}
                close={() => this.closeThread(t.id)}
                reply={reply}
                submitReply={e => this.addReply(e, t.id)}
                updateReply={this.updateReply}
              />
            ))
          : null}

        <div className="card" style={{clear: 'both', marginTop: '20px'}}>
          <div className="card-header">
            <b>Start a new discussion!</b>
          </div>
          <AddComment
            submit={this.createThread}
            update={this.updateReply}
            comment={reply}
            expand={this.expandForm}
            expanded={this.threadIsExpanded()}
          />
        </div>
      </div>
    ) : null;
  }
}

Comments.propTypes = {
  topic: string.isRequired
};

export default Comments;
