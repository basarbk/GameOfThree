import React, { Component } from 'react';
import { Modal, Button, Input, Label } from 'semantic-ui-react';

class UserChangeDialog extends Component {

    constructor(props){
        super(props);
        this.state={
            newname: "",
            open:false,
            error:false
        }

        this.inputChange = this.inputChange.bind(this);
        this.showModal = this.showModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.changeName = this.changeName.bind(this);
    }

    inputChange(event){
        let error = true;
        const value = event.target.value;
        if(value.length>0){
            error = false;
        }
        this.setState({newname:value, error:error});
    }

    showModal(){
        this.setState({open:true});
    }

    closeModal(){
        this.setState({open:false});
    }
    
    changeName(){
        if(this.state.newname.length===0){
            this.setState({error:true})
        } else {
            this.closeModal();
            this.props.changeUser(this.state.newname);
        }
    }


    render() {
        return (
            <Modal dimmer={'blurring'} size={'mini'} open={this.state.open} trigger={<Button color={"green"} onClick={this.showModal}>Change your user</Button>}>
                <Modal.Header>Change your user</Modal.Header>
                <Modal.Content>
                    <Input focus placeholder='Enter your user name..' error={this.state.error} value={this.state.newnmae} onChange={this.inputChange}/>
                    {this.state.error && <Label basic color='red' pointing>Please enter a value</Label>}
                </Modal.Content>
                <Modal.Actions>
                    <Button negative content="Cancel" onClick={this.closeModal} />
                    <Button positive icon='checkmark' labelPosition='right' content="Change" onClick={this.changeName} />
                </Modal.Actions>
            </Modal>
        );
    }
}

export default UserChangeDialog;