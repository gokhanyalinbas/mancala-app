import React, { Component } from 'react'
import GameService from '../api/GameService'

class GameComponent extends Component {
  
    constructor(props) {
        super(props)
        this.state = {
            game:'',
            pit1:'',
            pit2:'',
            pit3:'',
            pit4:'',
            pit5:'',
            pit6:'',
            pit7:'',
            pit8:'',
            pit9:'',
            pit10:'',
            pit11:'',
            pit12:'',
            pit13:'',
            pit14:'',
            scoreA:'36',
            scoreB:'36',
            nextTurn:'',
            winner:'',
            gameId:'',
            resumeId:''
        }
        this.callService = this.callService.bind(this)
        this.load = this.load.bind(this)
        this.updateEventClicked=this.updateEventClicked.bind(this)
        this.handleChange = this.handleChange.bind(this)
        this.autoPlay = this.autoPlay.bind(this)
        this.getPit=this.getPit.bind(this)
        this.timeout=this.timeout.bind(this)
    }
    handleChange(event) {
        this.setState(
            {
                [event.target.name]
                    : event.target.value
            }
        )
    }
    load() {
        console.log('load',this.state.resumeId)
        GameService.resume(this.state.resumeId)
        .then(
            response => {
                console.log(response);
                this.setState({ game: response.data,
                                pit1:response.data.gameBoard.pits[0].stoneCount,
                                pit2:response.data.gameBoard.pits[1].stoneCount,
                                pit3: response.data.gameBoard.pits[2].stoneCount,
                                pit4: response.data.gameBoard.pits[3].stoneCount,
                                pit5: response.data.gameBoard.pits[4].stoneCount,
                                pit6: response.data.gameBoard.pits[5].stoneCount,
                                pit7: response.data.gameBoard.pits[6].stoneCount,
                                pit8: response.data.gameBoard.pits[7].stoneCount,
                                pit9: response.data.gameBoard.pits[8].stoneCount,
                                pit10: response.data.gameBoard.pits[9].stoneCount,
                                pit11: response.data.gameBoard.pits[10].stoneCount,
                                pit12: response.data.gameBoard.pits[11].stoneCount,
                                pit13: response.data.gameBoard.pits[12].stoneCount,
                                pit14: response.data.gameBoard.pits[13].stoneCount, 
                                scoreA: response.data.gameBoard.scoreBoard.PlayerA,
                                scoreB: response.data.gameBoard.scoreBoard.PlayerB ,
                                 nextTurn: response.data.turn,
                                winner:response.data.winner,
                                gameId:response.data.id,
                                alertmessage:''    },() => 
               
                                console.log('score',this.state.scoreA))
            }
        )
        .catch(error=>{
            console.log(error.response.data.reason)
            this.setState({ alertmessage: `${error.response.data.reason}` })
            
        })
    }
    componentWillUnmount() {
        console.log('componentWillUnmount')
    }
    updateEventClicked(id){

        let gameDto={
            pitId:id,
            gameId:this.state.gameId
        }
        GameService.playGame(gameDto)
            .then(
                response => {
                    console.log(response);
                    this.setState({ game: response.data,
                                    pit1:response.data.gameBoard.pits[0].stoneCount,
                                    pit2:response.data.gameBoard.pits[1].stoneCount,
                                    pit3: response.data.gameBoard.pits[2].stoneCount,
                                    pit4: response.data.gameBoard.pits[3].stoneCount,
                                    pit5: response.data.gameBoard.pits[4].stoneCount,
                                    pit6: response.data.gameBoard.pits[5].stoneCount,
                                    pit7: response.data.gameBoard.pits[6].stoneCount,
                                    pit8: response.data.gameBoard.pits[7].stoneCount,
                                    pit9: response.data.gameBoard.pits[8].stoneCount,
                                    pit10: response.data.gameBoard.pits[9].stoneCount,
                                    pit11: response.data.gameBoard.pits[10].stoneCount,
                                    pit12: response.data.gameBoard.pits[11].stoneCount,
                                    pit13: response.data.gameBoard.pits[12].stoneCount,
                                    pit14: response.data.gameBoard.pits[13].stoneCount, 
                                    scoreA: response.data.gameBoard.scoreBoard.PlayerA,
                                    scoreB: response.data.gameBoard.scoreBoard.PlayerB ,
                                     nextTurn: response.data.turn,
                                    winner:response.data.winner,
                                    gameId:response.data.id,
                                    alertmessage:''    },() => 
                   
                                    console.log('score',this.state.scoreA))
                }
            )
            .catch(error=>{
                console.log(error.response.data.reason)
                this.setState({ alertmessage: `${error.response.data.reason}` })
                
            })

    }
    autoPlay(){
        let pit
        if(this.state.scoreA>0 && this.state.scoreB>0){

            pit=this.getPit(this.state.nextTurn)
            this.updateEventClicked(pit)
            
        }
    }
    timeout(ms) { //pass a time in milliseconds to this function
        return new Promise(resolve => setTimeout(resolve, ms));
      }
    getPit(turn){
        console.log(turn,this.state.pit1)
        if(turn==='PlayerA'){
            if(this.state.pit1!==0)
              return 1
            else if(this.state.pit2!==0)  
              return 2
            else if(this.state.pit3!==0)  
              return 3
            else if(this.state.pit4!==0)  
              return 4
            else if(this.state.pit5!==0)  
              return 5
            else if(this.state.pit6!==0)  
              return 6
            else return -1  
        }
        else{
            if(this.state.pit8!==0)
            return 8
          else if(this.state.pit9!==0)  
            return 9
          else if(this.state.pit10!==0)  
            return 10
          else if(this.state.pit11!==0)  
            return 11
          else if(this.state.pit12!==0)  
            return 12
          else if(this.state.pit13!==0)  
            return 13
            else return -1    
        }
    }
    shouldComponentUpdate(nextProps, nextState) {
        console.log('shouldComponentUpdate')
        console.log(nextProps)
        console.log(nextState)
        return true
    }
    componentDidMount() {
        console.log('componentDidMount')
        //this.callService();
        console.log(this.state)
    }
    callService() {
        console.log('callService')
        let gameDto={
            playerA:{
                id:'1',
                name:'Player A'
            },
            playerB:{
                id:'2',
                name:'Player B'
            }
        }

        GameService.createGame(gameDto)
            .then(
                response => {
                    console.log(response);
                    this.setState({ game: response.data,
                                    pit1:response.data.gameBoard.pits[0].stoneCount,
                                    pit2:response.data.gameBoard.pits[1].stoneCount,
                                    pit3: response.data.gameBoard.pits[2].stoneCount,
                                    pit4: response.data.gameBoard.pits[3].stoneCount,
                                    pit5: response.data.gameBoard.pits[4].stoneCount,
                                    pit6: response.data.gameBoard.pits[5].stoneCount,
                                    pit7: response.data.gameBoard.pits[6].stoneCount,
                                    pit8: response.data.gameBoard.pits[7].stoneCount,
                                    pit9: response.data.gameBoard.pits[8].stoneCount,
                                    pit10: response.data.gameBoard.pits[9].stoneCount,
                                    pit11: response.data.gameBoard.pits[10].stoneCount,
                                    pit12: response.data.gameBoard.pits[11].stoneCount,
                                    pit13: response.data.gameBoard.pits[12].stoneCount,
                                    pit14: response.data.gameBoard.pits[13].stoneCount,
                                    scoreA: 36,
                                    scoreB: 36 ,
                                    nextTurn: response.data.turn,
                                    winner:response.data.winner,
                                    gameId:response.data.id,
                                    alertmessage:''  }, () => 
                    console.log('score',this.state.scoreA))
                }
            )
            .catch(error=>{
                this.setState({ alertmessage: `${error.response.data.reason}` })
                
            })
    } 

    render() {
        return (
            <>
                <h1>Mancala Game!</h1>
                {this.state.winner && <div class="alert alert-success">Game over ! The winner is {this.state.winner.name}</div>}
                {this.state.alertmessage && <div class="alert alert-danger">{this.state.alertmessage}</div>}
                <div>
                <label>
                    Resume with Game ID:
                    <input placeholder="Enter your game id" type="text" name="resumeId" value={this.state.resumeId} onChange={this.handleChange}/>
                </label>
                <button onClick={() => this.load()}>Load</button>
                </div>
                <div>
                    <label >Current game is : {this.state.gameId}</label>
                </div>
                <div>
            <button className="btn btn-info" onClick={() => this.callService()}>Create New Game  !</button>
            </div>
            <div><label></label></div>
            <div>
            <button className="btn btn-success" onClick={() => this.autoPlay()}>Auto Play !</button>
            </div>
            <div/>
                <div className="container">
                    <div>
                    <label> <b>Next Turn : {this.state.nextTurn}</b></label> 
                    </div>
                    <label>Player B : {this.state.scoreB}</label>
             <table>
                <tbody>{
                    <tr>
                    <td><button className="playerBbutton" disabled={true} onClick={() => this.updateEventClicked(14)}>{this.state.pit14}</button> </td>
                <td>
                <tr>    
                <td><button className="playerBbutton" onClick={() => this.updateEventClicked(13)}>{this.state.pit13}</button> </td>
                <td><button className="playerBbutton" onClick={() => this.updateEventClicked(12)}>{this.state.pit12}</button></td>
                <td><button className="playerBbutton" onClick={() => this.updateEventClicked(11)}>{this.state.pit11}</button>  </td>
                <td><button className="playerBbutton" onClick={() => this.updateEventClicked(10)}>{this.state.pit10}</button>  </td>
                <td><button className="playerBbutton" onClick={() => this.updateEventClicked(9)}>{this.state.pit9}</button>  </td>
                <td><button className="playerBbutton" onClick={() => this.updateEventClicked(8)}>{this.state.pit8}</button>  </td>
                 </tr>
                 <tr>    
                 <td><button className="playerAbutton" onClick={() => this.updateEventClicked(1)}>{this.state.pit1}</button>  </td>
                 <td><button className="playerAbutton" onClick={() => this.updateEventClicked(2)}>{this.state.pit2}</button>  </td>
                 <td><button className="playerAbutton" onClick={() => this.updateEventClicked(3)}>{this.state.pit3}</button>  </td>
                 <td><button className="playerAbutton" onClick={() => this.updateEventClicked(4)}>{this.state.pit4}</button>  </td>
                 <td><button className="playerAbutton" onClick={() => this.updateEventClicked(5)}>{this.state.pit5}</button>  </td>
                 <td> <button className="playerAbutton" onClick={() => this.updateEventClicked(6)}>{this.state.pit6}</button>  </td>
                 
                 <td>                
                 <button className="playerAbutton" disabled={true}onClick={() => this.updateEventClicked(7)}>{this.state.pit7}</button>   
                 <tr><label></label></tr>
                 <tr><label> </label></tr>
                 </td>
                 
                 </tr>
                 </td>
                 </tr>
                }
            </tbody>
            </table>
            <label>Player A : {this.state.scoreA}</label>
            
            </div>
            </>
        )
        
    }



}


export default GameComponent