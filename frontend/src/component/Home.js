import React from 'react'
import { Container, Grid, Card, Dimmer, Loader} from 'semantic-ui-react'
import UserList from './UserList';
import GameList from './Games/GameList';
import HeaderArea from './Header';
import GameArea from './Games/GameArea';
import UserChangeDialog from './UserChangeDialog';

const ProfileCard = ({user, changeUser}) => (
  <Card>
    <Card.Content>
      <Card.Header>{user.name}</Card.Header>
      <Card.Meta>{user.type === 'TEMPORARY' ? "This user is automatically created": "Created by user"}</Card.Meta>
    </Card.Content>
    <UserChangeDialog changeUser={changeUser}/>
  </Card>
)

const Home = ({currentUser, players, gameStarter, games, loading, changeUser}) => (
  <div>
    <Dimmer active={loading}>
      <Loader size='large'>Loading</Loader>
    </Dimmer>
    <Container style={{ padding: '1em 0em' }}>
      <HeaderArea />
      <Grid >
        <Grid.Column width={4}>
          <ProfileCard user={currentUser} changeUser={changeUser} />
          <GameList title="Games" games={games} />
        </Grid.Column>

        <Grid.Column width={8}>
          <GameArea />
        </Grid.Column>
        <Grid.Column width={4}>
          <UserList title="Players" players={players} gameStarter={gameStarter} />
        </Grid.Column>


      </Grid>
    </Container>

  </div>
)

export default Home;
