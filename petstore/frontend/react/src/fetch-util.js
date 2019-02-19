import config from './config';

const options = {};

export async function getJson(urlSuffix) {
  const url = config.SERVER_URL + urlSuffix;
  const res = await fetch(url, options);
  return res.json();
}

export async function getText(urlSuffix) {
  const url = config.SERVER_URL + urlSuffix;
  const res = await fetch(url, options);
  return res.text();
}

export async function postJson(urlSuffix, obj) {
  const body = JSON.stringify(obj);
  const headers = {'Content-Type': 'application/json'};
  const url = config.SERVER_URL + urlSuffix;
  const res = await fetch(url, {...options, method: 'POST', headers, body});
  return res;
}
