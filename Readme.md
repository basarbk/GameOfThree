## Game of Three

Rules of the game

* There are two player in the game

* Player A starts the game against Player B

* A random value is generated as a starting value (e.g : `56`)

* Opponent player receives this value. 

* Player adds `+1`, `0`, or `-1` to make the value divisible by three (for 56, the choice would be `+1`)

* After player selection, the received value is added to the user selection, and divided three. ( (`56`+`1`) / `3` = `19` ) . And the result is sent to opponent.

* This is continue until the result is `1`. Player whose action reaches to 1, wins the game.

* Player may start the game with opponent to be played automatically.

## Implementation Details

A web application is implemented for this game

Backend is implemented with Spring Boot. Spring Data and Websocket dependencies are used. It is also using H2 database. Even the data is persisted to a file, if you restart the application, the content will be wiped out. This behavior can be changed by changing the `spring.jpa.hibernate.ddl-auto` parameter to `create` or `update` in `application.properties` under `src/main/resources`.
 Backend codes are located under `src` folder.

Frontend is implemented with [React](https://facebook.github.io/react/). It is created with facebook's starter kit [Create react app](https://github.com/facebookincubator/create-react-app) . Used [Redux](https://github.com/reactjs/redux) for state management, [axios](https://github.com/mzabriskie/axios) for api calls and [stompjs](https://github.com/jmesnil/stomp-websocket) for websocket communication. For UI components I used [Semantic-UI](https://react.semantic-ui.com). Frontend codes are under `frontend` folder.

## Build & Run

This project requires `Java 8` and [maven](http://maven.apache.org/) for building it.

Frontend javascript codes are build with npm scripts. Maven is taking care of it with [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin)

>Frontend inclusive build would take longer time. If you don't care about npm installation and build steps, switch to the branch `frontend-prebuild`.

Execute following command to start build process
```
mvn package
```

jar file to be located under `target` folder

application can be started with following command
```
java -jar target\game-of-three-0.0.1-SNAPSHOT.jar
```

## GamePlay

Open http://localhost:8080 in your browser.

Game will automatically generate a random user for you.
This user is related to your active session. So you'll be using that user as long as your session remains alive.

If you would like to generate another user and play against it, you need to either open different browser or same browser with incognito/private mode.

On homepage, you'll see user list. You can click to a user and start a game with a mode you like. Available modes are:

* **Manual Game** - Players will do actions manually

* **Opponent Automatic Game** - Opponent's actions will be taken automatically by the game

* **Full Auto** - Both parties will be played by game


Your games will be listed below your username card. You can select any of them and continue from where you left off.

You can change your game play mode during game.

You can also change your username. This would refresh fields based on your new user.
All the history of that user will be retrieved and if exists, you can continue to play unfinished games.

## What is missing?

I wanted this app to be ready to use so I decided to go with automatic user generation whenever a new session starts. I didn't want to force user to signup first and play later. So I didn't use security.

UI is unstyled. Just used the Semantic-UI components as it is.

Backend rest controllers are limited to frontend requirements. I didn't implemented whole CRUD functions for entities.

There is no performance related tunings in backend. No caching on controllers or there is no configuration for static file gzipping etc.

## Additional
If you would like to run the application in development mode you'll need to install `npm` which is coming with [nodejs](https://nodejs.org/en/).

To load frontend dependencies
```
npm install
```

run the spring boot with following command
```
mvn spring-boot:run -Pdev
```

then run the frontend server
```
npm start
```

This will open http://localhost:9876 in your browser.

With this mode, frontend will be served by webpack dev server, and all the `/api` requests triggered by UI actions will be proxied to spring boot (which will running at http://localhost:8080)