import * as types from '../actions/actionTypes';
import initialState from './initialState';

export default function playerReducer(state = initialState.players, action ){

    switch(action.type){
        case types.LOAD_PLAYERS_SUCCESS:
            return action.players;
        case types.PLAYER_PRESENCE_UPDATED:
            return [
                ...state.filter(player => player.id !==action.player.id),
                Object.assign({}, action.player)
            ];
        case types.PLAYER_ADDED:
            return [
                ...state,
                Object.assign({}, action.player)
            ];
        default:
            return state;
    }

}