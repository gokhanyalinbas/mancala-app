# mancala-app
 Kalah Game


**Build & Deploy**

-**Frontend**
(React Native)

Prerequisite:
Have Node.js and Docker installed on your computer.

* **with docker**

- [x] First you have to install docker and node.js on your PC.
- [x] Open command promt or terminal and go to directory HOME:/mancalagame-ui. Dockerfile is here.
- [x] To dockerize your  react  application run this command **`docker build -t mancala-ui .`**  
- [x] After succesfull built you can run this docker image in a seperate container with this command  **`docker run --name containername -d -p 3000:3000 mancala-ui `**  but in the backend side we have docker compose file and we can run this image with this file.If you don't want to run seperate container, don't run this command.
- [x] check docker running containers  **`docker ps`** 
- [x] check logs of  image **`docker logs mancala-ui`** 
- [x] Frontend link :  http://localhost:3000/login



* **without docker**

- [x] Open command promt or terminal and go to directory HOME:/mancalagame-ui
- [x] To open application run this command **` npm start `** 
- [x] After built you can open applcation on your browser. 
- [x] Frontend link :  http://localhost:3000/login

If you have any problem with frontend deployment, you can use my heroku app to access UI :) Applicaiton can be on idle state, please try two or three times.
https://mancalagame-gokhan.herokuapp.com


-**Mongo DB**
- [x] I have used mongo image from docker hub. If you want to use local mongo db, just change datasourceUrl in applicaiton.properties at springboot application.
- [x] pull mongo image from docker hub **`docker pull mongo:latest`**
- [x] run mongo image **`docker run -d -p 27017:27017 --name mancalamongodb mongo:latest`**
- [x] login to mongo terminal to verify records **`docker exec -it mancalamongodb bash`**
- type mongo and enter
- show dbs
- use Mancala
- show collections
- db.games.find().pretty()


-**Backend**

* **without docker**
- [x] If you run springboot application on local, please change mongodb.url in application.properties.
- [x] Open mancalagame folder in your spring IDE (IntellIJ,Eclipse, STS ..) or command prompt and run **`clean`** - **`install`** command.
- [x] After build you can run .war file.
- [x] Application link : 'http://localhost:8085'
  
* **with docker**
- [x] First we have to run **`clean`** - **`install`** command and be sure about that you have .war file under /target folder.
- [x] dockerize spring boot application **`docker build -t mancala-game .`**
- [x] check docker running containers  **`docker ps`** 
- [x] If you have container for this image kill running container:
```
docker rm <containerId>
```

#### docker-compose.yml
```yaml
version: "3"
services:
  mancalamongodb:
    image: mongo:latest
    container_name: "mancalamongodb"
    ports:
      - 27017:27017
  mancala-game:
    image: mancala-game
    container_name: mancala-game
    ports:
      - 8085:8085
    links:
      - mancalamongodb
  mancala-ui:
    image: mancala-ui
    container_name: mancala-ui-container
    ports:
      - 3000:3000
    tty: true
```
- [x] navigate to resources folder:
/src/main/resources and run  **`docker compose up -d`**
- [x] Be sure about all containers are running succesfully
 ```
[+] Running 3/0
 - Container mancalamongodb        Running                                                                                                                                         0.0s
 - Container mancala-ui-container  Running                                                                                                                                         0.0s
 - Container mancala-game          Running   
```
![Containers](../master/screenshot/containerlog.png)

**Links**
- [x] Frontend   :  http://localhost:3000/login
- [x] Backend    :  http://localhost:8085
- [x] Mongo DB   :  mongodb://localhost:27017
- [x] Swagger UI :  http://localhost:8085/swagger-ui.html


**How To Play**

- [x] Open frontend URL on your browser. 
- [x] Login with user : admin and password: admin
- [x] ![Login Page](../master/screenshot/login.png) 
- [x] If you get error please check browser console logs or springboot-applogs.
- [x] After login you will see the ![welcome page](../master/screenshot/welcomepage.png) page . Please click  **game** button.
- [x] You will access  ![game page](../master/screenshot/gameinit.png) . 
- To create new game : Please click  **Create New Game** button
- To Resume game : Please enter your gameID and click **Load** button.
- To Autoplay : If you don't want to clict for PlayerA and PlayerB. Just click **AutoPlay** button and this button can play for you :) The autoplay logic is very simple. Determines the nextTurn and pick own  pit that stone count is greater than 0.
- [x] You can see ![error](../master/screenshot/turnerror.png) and ![winner](../master/screenshot/winner.png) here.


**Docker **

- [x] ![Container](../master/screenshot/containers.png) and ![images](../master/screenshot/images.png)

**Swagger UI**

- [x] Open swagger link and firstly get a JWT token from  ![login controller](../master/screenshot/swaggerlogin.png)
- [x] To use JWT token  ![enter your JWT token here](../master/screenshot/auth_token.png) and test the other methods.

If you have any problem with docker, let it go :) just deploy your local PC and run the applciation.

Good Luck :) 

