import axios from 'axios'
import { API_URL } from '../../Constant'

class GameService {


    createGame(gameDTO) {
        console.log('executed service',gameDTO)
        let token = sessionStorage.getItem('token');
        let config={
            headers:{
            'Authorization': token
        } 
    };

      const instance= axios.create()
      instance.defaults.headers.common['Authorization'] = token
    
        console.log('token',instance.defaults.headers.common)
        return instance.post(`${API_URL}/games/create`, gameDTO);
    }
     
    playGame(pitId,gameId) {
        console.log('executed service',pitId,gameId)
        let token = sessionStorage.getItem('token');
        let config={
            headers:{
            'Authorization': token
        } 
    };

      const instance= axios.create()
      instance.defaults.headers.common['Authorization'] = token
      console.log('token',instance.defaults.headers.common)
      return instance.put(`${API_URL}/games/play/${gameId}/pit/${pitId}`);
    }
    resume(id) {
        console.log('executed service',id)
        let token = sessionStorage.getItem('token');
        let config={
            headers:{
            'Authorization': token
        }    
    };

      const instance= axios.create()
      instance.defaults.headers.common['Authorization'] = token

        console.log('executed service', config)
        return instance.get(`${API_URL}/games/resume/${id}`);
    }

}

export default new GameService()