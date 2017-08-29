import React, { Component } from 'react';
import Home from '../component/Home';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as playerActions from '../actions/playerActions';
import * as gameActions from '../actions/gameActions';
import * as api from '../connectors/APICaller';
import * as stompSocket from '../connectors/WebSocket';
import toastr from 'toastr';

class App extends Component {

  constructor(props){
    super(props);
    this.wsNewPlayerListener = this.wsNewPlayerListener.bind(this);
    this.wsPresenceListener = this.wsPresenceListener.bind(this);
    this.wsConnect = this.wsConnect.bind(this);
    this.createGame = this.createGame.bind(this);
    this.wsPrivateListener = this.wsPrivateListener.bind(this);
    this.getMyGames = this.getMyGames.bind(this);
    this.changeUser = this.changeUser.bind(this);
    this.state = {
      user: {name:"",type:"TEMPORARY"},
      players : [],
      games:[],
      currentGame:{},
      loading:true
    }
  }

  componentWillReceiveProps(nextProps){
    this.setState({user: nextProps.user, players:nextProps.players, games:nextProps.games.all, currentGame:nextProps.games.current}, this.afterStateUpdated(nextProps));

  }

  afterStateUpdated(nextProps) {
    if (nextProps.user != null && Object.keys(nextProps.user).length > 0 && nextProps.user.id !== this.state.user.id) {
      this.getMyGames(nextProps.user.id);
      this.loadOpponents(nextProps.user.id);
      this.wsConnect(nextProps.user.name);
    }
  }

  componentWillMount() {
    this.retrieveMyUser();
  }

  retrieveMyUser(){
    this.setState({loading:true})
    api.checkPrincipal()
    .then(resp => {
        this.props.actions.loadUserSuccess({user: resp.data});
      })
    .catch(err => {
      this.setState({loading:false})
      toastr.error("Failed retrieving user info from backend. Make sure backend is running");

    });
  }

  getMyGames(id){
    api.getPlayersGames(id)
    .then(resp => this.props.gameActs.loadGamesSuccess(resp.data))
    .catch(err=>{
      if(err.response.status === 404){
        this.props.gameActs.loadGamesSuccess([])
      } else {
        console.log(err);
      }
    });
  }

  loadOpponents(id){
    api.loadOpponents(id)
    .then(resp => this.props.actions.loadPlayersSuccess(resp.data))
    .catch(err => console.log(err));
    this.setState({loading:false})
  }

  wsConnect(name){
    stompSocket.connect(name);
    stompSocket.subscribe("/topic/new-player", this.wsNewPlayerListener);
    stompSocket.subscribe("/topic/player/"+name, this.wsPrivateListener);
    stompSocket.subscribe("/topic/presence", this.wsPresenceListener);
  }

  wsNewPlayerListener(response){
    this.props.actions.playerPresenceChanged(JSON.parse(response.body));
  }
  
  wsPresenceListener(response){
    this.props.actions.playerPresenceChanged(JSON.parse(response.body));
  }

  wsPrivateListener(response){
    const obj = JSON.parse(response.body);
    if(obj.type === "game-start") {
      this.props.gameActs.newGameCreated(obj.body);
    } else if ( obj.type === "game-completed") {
      this.props.gameActs.gameStateChanged(obj.body.id, obj.body.state);
    } else if (obj.type === "action") {
      this.props.gameActs.newActionIsTaken(obj.body.gameid, obj.body.action);
    } else {
      console.log("undefined ws message");
    }
  }

  changeUser(newname){
    this.setState({loading:true})
    stompSocket.unsubscribe();
    console.log("changing user to:: "+newname);
    api.changeMyPlayer(newname).then(resp => this.retrieveMyUser()).catch(err => console.log(err));
    console.log("changing completed.. follow up rest activities");
  }

  createGame(player, remoteAuto, fullAuto){
    console.log("starting game to player ",player);
    const content = {starter:this.state.user, opponent:player, starterAutomatic:fullAuto, opponentAutomatic:remoteAuto};
    api.createGame(content)
      .then(resp => this.props.gameActs.newGameCreated(resp.data) )
      .catch(err => console.log(err));

  }

  render() {
      return (
          <div>
              <Home 
                currentUser={this.state.user} 
                players={this.state.players} 
                gameStarter={this.createGame}
                games={this.state.games}
                loading={this.state.loading}
                changeUser={this.changeUser}
                />
          </div>
      );
  }
}


function mapStateToProps(state, ownProps){
  return {
      user: state.userReducer.user,
      players: state.playerReducer,
      games: state.gameReducer
  };
}

function mapDispatchtoProps(dispatch){
  return {
      actions: bindActionCreators(playerActions, dispatch),
      gameActs : bindActionCreators(gameActions, dispatch)
  };
}

export default connect(mapStateToProps, mapDispatchtoProps)(App);