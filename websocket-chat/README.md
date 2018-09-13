## Micronaut WebSocket Demo Application.

### Steps

Run the application

```
./gradlew run
```

Open 2 browser windows with different URIs:

```
http://localhost:8080/#/stuff/fred
http://localhost:8080/#/stuff/bob
```

The first part of the URI (in the above case `stuff`) is the topic being discussed, last part is the username of the user.

Send messages!
