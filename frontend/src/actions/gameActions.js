import * as types from './actionTypes.js';

export function loadGamesSuccess(games){
    return {type: types.LOAD_GAME_SUCCESS, games};
}

export function setCurrentGame(gameid){
    return {type: types.SET_CURRENT_GAME, gameid};
}

export function newGameCreated(game){
    return {type: types.CREATED_NEW_GAME, game};
}

export function newActionIsTaken(gameid, action) {
    return {type: types.GAME_ACTION_TAKEN, gameid, action};
}

export function gameStateChanged(gameid, state){
    return {type: types.GAME_STATE_CHANGED, gameid, state};
}

export function gamePlayerModeChanged(gameid, starter, opponent){
    return {type: types.GAME_PLAYER_MODE_CHANGED, gameid, starter, opponent }
}