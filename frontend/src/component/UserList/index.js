import React, {Component} from 'react'
import { Segment, Header, List, Popup, Button } from 'semantic-ui-react';

class UserList extends Component {

    render() {
        return (
            <Segment >
                <Header as='h3'>{this.props.title}</Header>
                <List selection divided verticalAlign='bottom'>
                    {this.props.players.map(player => (
                        <Popup
                            key={player.id}
                            position="top left"
                            on="click"
                            hideOnScroll
                            trigger={
                                <List.Item key={player.id}>
                                    <List.Icon name="circle" color={player.presence === "OFFLINE" ? 'red' : 'green'}>
                                    </List.Icon>
                                    <List.Content>
                                        <List.Header>{player.name}</List.Header>
                                    </List.Content>

                                </List.Item>
                            }
                            flowing
                            hoverable>
                            <Button color='green' content='Start Manual Game' onClick={() => this.props.gameStarter(player, false, false)} />
                            <Button color='green' content='Start Opponent Automatic Game' onClick={() => this.props.gameStarter(player, true)} />
                            <Button color='green' content='Start Full Auto' onClick={() => this.props.gameStarter(player, true, true)} />
                        </Popup>
                    ))}
                </List>
            </Segment>
        )
    }
}

export default UserList;