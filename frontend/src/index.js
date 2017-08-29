import React from 'react';
import ReactDOM from 'react-dom';
import { createStore, applyMiddleware} from 'redux';
import { combineReducers } from 'redux';
import userReducer from './reducers/userReducer';
import playerReducer from './reducers/playerReducer';
import gameReducer from './reducers/gameReducer';
import { Provider } from 'react-redux';
import {createLogger} from 'redux-logger';
import './style/index.css';
import App from './container/App';
import registerServiceWorker from './registerServiceWorker';
import '../../node_modules/toastr/build/toastr.min.css';

const reducers = combineReducers(
    {
        userReducer,
        playerReducer,
        gameReducer
    }
);

const store = createStore(
    reducers,
    applyMiddleware(createLogger())
  );

ReactDOM.render(
    <Provider store={store}>
        <App />
    </Provider>
    , document.getElementById('root'));
registerServiceWorker();
