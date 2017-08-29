import axios from 'axios';

export function checkPrincipal(){
    return axios.get("/api/players/my-player");
}

export function changeMyPlayer(newname){
    return axios.put("/api/players/my-player/"+newname,{});
}

export function loadOpponents(id){
    return axios.get("/api/players/"+id+"/opponents");
}

export function createGame(body){
    return axios.post("/api/games",body);
}

export function getPlayersGames(id){
    return axios.get("/api/players/"+id+"/games");
}

export function sendActionForGame(gameid, body){
    return axios.post("/api/games/"+gameid+"/actions",body);
}

export function toggleGamePlayMode(playerid, gameid){
    return axios.put("/api/players/"+playerid+"/games/"+gameid+"/automatic", {});
}