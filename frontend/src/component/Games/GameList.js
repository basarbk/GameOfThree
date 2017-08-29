import React, { Component } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import { Segment, Header, List, Container } from 'semantic-ui-react';
import * as gameActions from '../../actions/gameActions';

class GameList extends Component {

    constructor(props){
        super(props);
        this.selectGame = this.selectGame.bind(this);
        this.getOpponent = this.getOpponent.bind(this);
        this.getNextAction = this.getNextAction.bind(this);
    }

    selectGame(gameid){
        this.props.actions.setCurrentGame(gameid);
    }

    getOpponent(game){
        if(game.starter.name === this.props.user.name){
            return game.opponent.name;
        } else {
            return game.starter.name;
        }
    }

    getNextAction(game){
        if(game.state !=="STARTED"){
            return <Container style={{ color: "gray" }}>
                Game completed - {game.actions[game.actions.length-1].owner.name === this.props.user.name ? "YOU WON !!" : game.actions[game.actions.length-1].owner.name + " won"}
            </Container>
        } else {
            if(game.actions[game.actions.length-1].owner.name === this.props.user.name){
                return <Container style={{ color: "gray" }}>
                Waiting for {game.actions[game.actions.length-1].owner.name}
            </Container>
            } else {
                return <Container style={{ color: "red" }}>
                Your turn
            </Container>
            }
        }
    }


    

    render() {
        return (
            <Segment >
                <Header as='h3'>{this.props.title}</Header>
                <List selection divided verticalAlign='bottom'>
                    {this.props.games.map(game => (
                        <List.Item key={game.id} onClick={() => this.selectGame(game.id)}>
                            <List.Icon name="circle" color={game.state === "STARTED" ? 'green' : 'red'}>
                            </List.Icon>
                            <List.Content>
                                <List.Header>{"Game " + game.id + " vs " + this.getOpponent(game)}</List.Header>
                                {this.getNextAction(game)}
                            </List.Content>

                        </List.Item>
                    ))}
                </List>
            </Segment>
        );
    }
}

function mapStateToProps(state, ownProps){
    return {
        user: state.userReducer.user,
        games: state.gameReducer.all
    };
  }
  
  function mapDispatchtoProps(dispatch){
    return {
        actions : bindActionCreators(gameActions, dispatch)
    };
  }

  export default connect(mapStateToProps, mapDispatchtoProps)(GameList);