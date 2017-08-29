import * as types from '../actions/actionTypes';
import initialState from './initialState';

export default function gameReducer(state = initialState.games, action ){

    switch(action.type){
        case types.LOAD_GAME_SUCCESS:
            return Object.assign({}, state, {all:action.games});
        case types.SET_CURRENT_GAME:
            return Object.assign({}, state, {current:action.gameid});
        case types.CREATED_NEW_GAME:
            return Object.assign({}, state, {all:[...state.all, action.game]});
        case types.GAME_ACTION_TAKEN:
            return Object.assign({}, state, {
                all : state.all.map( game => {
                    if(game.id === action.gameid) {
                        let gameState = game.state;
                        if(action.action.result === 1 && gameState === "STARTED"){
                            gameState = "COMPLETED";
                        }
                        return Object.assign({}, game, {state:gameState, actions:[...game.actions, action.action]});
                    } else {
                        return game;
                    }
                } )
            });
        case types.GAME_STATE_CHANGED:
            return Object.assign({}, state, {
                all : state.all.map( game => {
                    if(game.id === action.gameid) {
                        return Object.assign({}, game, {state:action.state});
                    } else {
                        return game;
                    }
                } )
            });
        case types.GAME_PLAYER_MODE_CHANGED:
            return Object.assign({}, state, {
                all : state.all.map( game => {
                    if(game.id === action.gameid) {
                        return Object.assign({}, game, {starterAutomatic:action.starter, opponentAutomatic:action.opponent});
                    } else {
                        return game;
                    }
                } )
            });
        default:
            return state;
    }

}