FROM node:latest

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY package.json /usr/src/app/

COPY wait-for wait-for /usr/src/app/

RUN npm install

CMD ./wait-for -t 120 storefront:8080 -- npm start