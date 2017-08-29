import * as types from './actionTypes.js';

export function loadUserSuccess(user){
    return {type: types.LOAD_MY_PLAYER_SUCCESS, user};
}

export function loadPlayersSuccess(players){
    return {type: types.LOAD_PLAYERS_SUCCESS, players};
}

export function playerPresenceChanged(player){
    return {type: types.PLAYER_PRESENCE_UPDATED, player};
}

export function newPlayerAdded(player){
    return {type : types.PLAYER_ADDED, player};
}
