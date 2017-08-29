import * as types from '../actions/actionTypes';
import initialState from './initialState';

export default function userReducer(state = initialState.user, action ){

    switch(action.type){
        case types.LOAD_MY_PLAYER_SUCCESS:
            return action.user;
        default:
            return state;
    }

}