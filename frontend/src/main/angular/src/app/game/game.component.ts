import {Component, OnInit} from '@angular/core';
import * as Phaser from 'phaser';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {v4 as uuidv4} from 'uuid';
import {AuthService} from "../auth.service";
import {gamestats, playerstats} from "../models";
import GameObject = Phaser.GameObjects.GameObject;

const WIDTH = 800;
const HEIGHT = 600;

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {

  phaserGame: Phaser.Game;
  config: Phaser.Types.Core.GameConfig;

  constructor() {
    this.config = {
      type: Phaser.AUTO,
      height: HEIGHT, width: WIDTH,
      scene: [MainScene, GameScene],
      parent: 'gameContainer',
      physics: {
        default: 'arcade',
        // arcade: {gravity: {y: 100}}
      }
    };
  }

  ngOnInit(): void {
    this.phaserGame = new Phaser.Game(this.config);
  }

}

class MainScene extends Phaser.Scene {

  constructor() {
    super({key: 'main'});
  }

  preload() {
    this.load.image('gradient', '../../assets/gradient.png');
    this.load.image('button', '../../assets/green_button02.png');
    this.load.image('button_pressed', '../../assets/green_button03.png');
  }

  create() {
    this.add.image(0, 0, 'gradient');
    const settingsButton = this.add.image(400, 300,
      'button').setInteractive();
    const settingsButtonText = this.add.text(0, 0, 'start game', {
      color:
        '#000', fontSize: '28px'
    });
    Phaser.Display.Align.In.Center(settingsButtonText,
      settingsButton);
    settingsButton.on('pointerdown', () => {
      settingsButton.setTexture('button_pressed');
    }).on('pointerup', () => {
      settingsButton.setTexture('button');
      this.scene.launch('game');
      this.scene.stop();
    });
  }
}

class GameScene extends Phaser.Scene {
  //game variables
  private cursorKeys: Phaser.Types.Input.Keyboard.CursorKeys;
  private image: Phaser.Physics.Arcade.Sprite;

  // state = {players: "", numPlayers: ""};
  joined: boolean;
  // man: any;
  otherPlayers;

  //websocket variables
  webSocketEndPoint: string = 'http://localhost:8080/gamews';
  headers = {'access-token': 'Bearer ' + sessionStorage.getItem('access-token')};
  stompClient: any;

  constructor() {
    super({key: 'game'});
  }

  preload() {
    this.load.image('man', '../../assets/character.png');
  }

  create() {
    let obj: { [k: string]: any } = {};
    obj.userId = uuidv4()
    // let obj = this;
    this.otherPlayers = this.physics.add.group();
    this.initSocket();
    this.stompClient.connect(this.headers, (frame) => {
      //create userid;
      this.stompClient.send("/gamews/gameid", {}, obj.userId);
      //to re-add everyone when a new player joins that is NOT you.
      this.stompClient.subscribe("/topic/setState", (event) => {
        // const {players, numPlayers } = event;
        // this.physics.resume();
        const gs = JSON.parse(event.body);
        const players = gs.playersStats;
        const numPlayers = gs.numPlayers;
        console.log(numPlayers);
        console.log(players);
        Object.keys(players).forEach(function (id) {
          if (players[id].playerId === obj.userId) {
            console.log('adding myself');
            // obj.addPlayer(players[id], obj);
            // obj.joined = true;
            obj.image = obj.physics.add
              .sprite(players[id].x, players[id].y, "man").setOrigin(0.5, 0.5)
          } else {
            console.log('adding others');
            // obj.addOtherPlayers(players[id], obj);
            let player = obj.add.sprite(players[id].x, players[id].y, "man").setOrigin(0.5, 0.5).setTint(0x0000ff);

            // const otherPlayer = {
            //   player: player, playerId: players[id].playerId
            // }

            obj.otherPlayers.add(player);
          }
        });
      });
      //for adding yourself when you join the game
/*      this.stompClient.subscribe("/topic/currentPlayer", (event) => {
        const gs = JSON.parse(event.body);
        const players = gs.playersStats;
        const numPlayers = gs.numPlayers;
        console.log(numPlayers);
        console.log(players);
        Object.keys(players).forEach(function (id) {
          if (players[id].playerId === obj.userId) {
            console.log('adding myself');
            obj.image = obj.physics.add
              .sprite(players[id].x, players[id].y, "man").setOrigin(0.5, 0.5)
          }
        });
      });*/
      this.stompClient.subscribe("/topic/disconnect", (event) => {
        const playerId = event.body;
        this.otherPlayers.getChildren().forEach(function (otherPlayer) {
          if (playerId === otherPlayer.playerId) {
            otherPlayer.destroy();
          }
        });
      });

      /*    this.stompClient.connect(this.headers, (frame) => {
            this.stompClient.subscribe("/topic/currentPlayers", (event) => {
              const { players, numPlayers } = event;
              this.state.numPlayers = numPlayers;
              Object.keys(players).forEach(function (id) {
                if (players[id].playerId === this.socket.id) {
                  scene.addPlayer(scene, players[id]);
                } else {
                  scene.addOtherPlayers(scene, players[id]);
                }
            });
          });*/

      this.add.text(250, 40, 'startgame', {
        fontSize: '56px', color: '#ffffff'
      });
      this.renderBackButton();
      // this.image = this.physics.add.sprite(WIDTH / 2, HEIGHT / 2, 'man');
      // this.cursorKeys = this.input.keyboard.createCursorKeys();
    })
  }

  /*update() {
    const scene = this;
    // Every frame, we create a new velocity for the sprite based on what keys the player is holding down.
    const velocity = new Phaser.Math.Vector2(0, 0);
    const speed = 200;

    if (this.cursorKeys.left.isDown) {
      velocity.x -= 1;
    }
    if (this.cursorKeys.right.isDown) {
      velocity.x += 1;
    }
    if (this.cursorKeys.up.isDown) {
      velocity.y -= 1;
    }
    if (this.cursorKeys.down.isDown) {
      velocity.y += 1;
    }

    // We normalize the velocity so that the player is always moving at the same speed, regardless of direction.
    const normalizedVelocity = velocity.normalize();
    scene.image.setVelocity(normalizedVelocity.x * speed, normalizedVelocity.y * speed);

    //emit position to other players
    /!*    const x = this.man.x;
        const y = this.man.y;
        if (
          this.man.oldPosition &&
          (x !== this.man.oldPosition.x ||
            y !== this.man.oldPosition.y)
        ) {
          // this.moving = true;
          this.stompClient.send("/gamews/gameid", {}, this.userId);
          this.stompClient.send("/gamews/playerMovement", {},
            { x: this.man.x, y: this.man.y}
            );
          this.man.oldPosition = {
            x: this.man.x,
            y: this.man.y,
            rotation: this.man.rotation,
          };
        }*!/

  }*/

  renderBackButton() {
    const backButton = this.add.image(700, 570,
      'button').setInteractive();
    const backButtonText = this.add.text(0, 0, 'back', {
      color:
        '#000', fontSize: '28px'
    });
    Phaser.Display.Align.In.Center(backButtonText,
      backButton);
    backButton.on('pointerdown', () => {
      backButton.setTexture('button_pressed');
    }).on('pointerup', () => {
      backButton.setTexture('button');
      this.scene.launch('main');
      this.scene.stop();
    });
  }

  initSocket() {
    console.log("initialize websocket connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
  }


  /*
    addPlayer(playerInfo, scene: this) {
      console.log('adding myself1');
      console.log(playerInfo.x);
      scene.joined = true;
      scene.image = this.physics.add
        .sprite(WIDTH/2, HEIGHT/2, "man")
        // .setOrigin(0.5, 0.5)
        // .setSize(30, 40)
        // .setOffset(0, 24);
    }

    //likely to have issues here..
    addOtherPlayers(playerInfo, scene: this) {
      console.log('adding others1');
      console.log(playerInfo.x);
      let player = scene.add.sprite(
        playerInfo.x + 40,
        playerInfo.y + 40,
        "man"
      );
      const otherPlayer = {
        player: player, playerId: playerInfo.playerId
      }
      this.otherPlayers.add(otherPlayer);
    }*/


}
