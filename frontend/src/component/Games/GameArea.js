import React, { Component } from 'react';
import { Segment, Label, Menu, Button} from 'semantic-ui-react'
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as gameActions from '../../actions/gameActions';
import * as api from '../../connectors/APICaller';

import toastr from 'toastr';

const ButtonExampleGroup = ({buttonClick}) => (
    <Button.Group size='large'>
      <Button color='orange' onClick={() => buttonClick("ADD_ONE")}>+1</Button>
      <Button.Or text=' ' />
      <Button onClick={() => buttonClick("ZERO")}>0</Button>
      <Button.Or text=' ' />
      <Button color='blue' onClick={() => buttonClick("MINUS_ONE")}>-1</Button>
    </Button.Group>
  )

const getActionItem = (type) => {
    switch(type){
        case "START_GAME":
         return <Label style={{float:"initial"}}>Started The Game</Label>;
        case "ADD_ONE":
            return <Label style={{float:"initial"}}>+1</Label>;
        case "MINUS_ONE":
            return <Label style={{float:"initial"}}>-1</Label>;
        default:
            return <Label style={{float:"initial"}}>add nothing</Label>;
    }
}

class GameArea extends Component {
    constructor(props){
        super(props);
        this.buttonClick = this.buttonClick.bind(this);
        this.getOpponent = this.getOpponent.bind(this);
        this.amIAutomatic = this.amIAutomatic.bind(this);
        this.togglePlayMode = this.togglePlayMode.bind(this);
        this.state={
            user: {name:"",type:"TEMPORARY"},
            myTurn : false,
            game:null
        }
    }

    componentWillReceiveProps(nextProps){
        this.setState({game:nextProps.game, user:nextProps.user});
    }

    getOpponent(game){
        if(game.starter.name === this.props.user.name){
            return game.opponent.name;
        } else {
            return game.starter.name;
        }
    }

    buttonClick(i){
        const action = {type:i, owner:this.state.user};
        api.sendActionForGame(this.state.game.id, action).then(resp => {
            console.log("doing nothing with the response.. action will be received through websocket")
            // this.props.gameActs.newActionIsTaken(this.state.game.id, resp.data);
        }
        ).catch(err => toastr.error("invalid action"));
    }

    amIAutomatic(){
        if(this.state.game.starter.name === this.props.user.name){
            return this.state.game.starterAutomatic;
        } else {
            return this.state.game.opponentAutomatic;
        }
    }

    togglePlayMode(){
        const uid = this.state.user.id;
        const gid = this.state.game.id;
        console.log("user id - "+uid+" - game: "+gid);
        api.toggleGamePlayMode(uid, gid)
            .then(resp => {
                this.props.gameActs.gamePlayerModeChanged(gid, resp.data.starterAutomatic, resp.data.opponentAutomatic);
            })
            .catch(err => toastr.error("Failed toggling game mode"))
    }

    render() {
        return (
            <Segment>
                <Label attached='top' size="large" style={{paddingBottom:"1em"}}>{this.state.game === null ? "Game" : "Game " + this.state.game.id + " vs " + this.getOpponent(this.state.game)}</Label>
                {this.state.game === null && <div>Select a user to start a game</div>}
                {this.state.game !== null &&
                <div>
                    <Menu vertical style={{width:"100%"}}>


                        {this.state.game.actions.map(action => 

                            <Menu.Item key={action.id} style={{display:""}}>
                            {action.owner.name} {getActionItem(action.type)} and the result is <Label style={{float:"initial"}}>{action.result}</Label>
                            </Menu.Item>

                        )}
                    </Menu>
                    
                    {this.state.game.state === "COMPLETED" && this.state.user.name === this.state.game.actions[this.state.game.actions.length-1].owner.name &&
                        <div>You Win!!</div>
                    }
                    {this.state.game.state === "COMPLETED" && this.state.user.name !== this.state.game.actions[this.state.game.actions.length-1].owner.name &&
                        <div>{this.state.game.actions[this.state.game.actions.length-1].owner.name +" won the game :("}</div>
                    }

                    {!this.amIAutomatic() && this.state.game.state==="STARTED" && this.state.user.name !== this.state.game.actions[this.state.game.actions.length-1].owner.name  && 
                        <div>
                            Your turn<br/>
                            <ButtonExampleGroup buttonClick={this.buttonClick}/>
                        </div>
                    }
                    
                    {this.state.game.state==="STARTED" && this.state.user.name === this.state.game.actions[this.state.game.actions.length-1].owner.name  && 
                        <div>
                        Waiting for opponent
                        </div>
                    }
                    <br/>
                    {this.state.game.state==="STARTED" && this.amIAutomatic() && 

                        <Button color='red' onClick = {this.togglePlayMode}>Click to play manual</Button>
                    }
                    {this.state.game.state==="STARTED" && !this.amIAutomatic() && 
                        <Button color='green' onClick = {this.togglePlayMode}>Click to play automatically</Button>
                    }
                    
                    </div>
                }
            </Segment>
        );
    }
}

function mapStateToProps(state, ownProps){
    let geym = null;
    if(state.gameReducer.current > -1){
        for(let game of state.gameReducer.all){
            if(game.id === state.gameReducer.current){
                geym = game;
                break;
            }
        }
    }
    return {
        user: state.userReducer.user,
        game: geym
    };
  }
  function mapDispatchtoProps(dispatch){
    return {
        gameActs : bindActionCreators(gameActions, dispatch)
    };
  }
  
export default connect(mapStateToProps, mapDispatchtoProps)(GameArea);
