import React, { Component } from 'react'
import AuthenticationService from '../api/AuthenticationService'

class LoginComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            username: '',
            password: '',
            hasLoginFailed: false,
            showSuccessMessage: false
        }
        this.handleChange = this.handleChange.bind(this)
        this.loginClicked = this.loginClicked.bind(this)
    }

    handleChange(event) {
        this.setState(
            {
                [event.target.name]
                    : event.target.value
            }
        )
    }


    loginClicked() {
        AuthenticationService
            .executeJwtAuthenticationService(this.state.username, this.state.password)
            .then((response) => {
                AuthenticationService.registerSuccessfulLoginForJwt(this.state.username, response.data.token)
                this.props.history.push(`/welcome/${this.state.username}`)
                window.location.reload()
            }).catch(() => {
                this.setState({ showSuccessMessage: false })
                this.setState({ hasLoginFailed: true })
            })

    }

    render() {
        return (
      
        <div id="loginform">
        <FormHeader title="Login" />
        {this.state.hasLoginFailed && <div className="alert alert-warning">Wrong username or password!</div>}
                    {this.state.showSuccessMessage && <div>Login Successfull</div>}
        <div class="row">
       <label>Username</label>
        <input placeholder="Enter your username" type="text" name="username" value={this.state.username} onChange={this.handleChange}/>
        </div> 
        <div class="row">
       <label>Password</label>
        <input placeholder="Enter your password" type="password" name="password" value={this.state.password} onChange={this.handleChange}/>
        </div>         
        <div id="button" class="row">
        <button onClick={this.loginClicked}>Login</button>
         </div>
        
      </div>
        
        )
    }
}




const FormHeader = props => (
    <h2 id="headerTitle">{props.title}</h2>
);


export default LoginComponent