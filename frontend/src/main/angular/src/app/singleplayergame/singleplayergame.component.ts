import {Component, Injectable, OnInit} from '@angular/core';
import * as Phaser from 'phaser';
import {GameService} from "./gameservice.service";
import {Subject, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";

const WIDTH = 800;
const HEIGHT = 600;


@Component({
  selector: 'app-singleplayergame',
  templateUrl: './singleplayergame.component.html',
  styleUrls: ['./singleplayergame.component.css']
})
export class SingleplayergameComponent implements OnInit {

  phaserGame: Phaser.Game;
  config: Phaser.Types.Core.GameConfig;
  gameSvcfield;

  constructor(private gameSvc: GameService) {
    this.config = {
      type: Phaser.AUTO,
      height: HEIGHT, width: WIDTH,
      // scene: [MainScene, GameScene],
      parent: 'gameContainer',
      physics: {
        default: 'arcade',
        arcade: {gravity: {y: 300}}
      },
      scale: {
        parent: 'mygame',
        autoCenter: Phaser.Scale.CENTER_BOTH
      }
    };
    this.gameSvcfield = gameSvc;
    this.config.scene = [new MainScene(this), new GameScene((this))]
    // this.config.scene = new MainScene(this.config, this);
    gameSvc.Sub.subscribe(score => console.log(score));
  }

  ngOnInit(): void {
    this.phaserGame = new Phaser.Game(this.config);
  }


}

class MainScene extends Phaser.Scene {

  constructor(p: SingleplayergameComponent) {
    super({key: 'main'});
  }

  preload() {
    // this.load.image('gradient', '../../assets/gradient.png');
    this.load.image('button', '../../assets/green_button02.png');
    this.load.image('button_pressed', '../../assets/green_button03.png');
  }

  create() {
    const settingsButton = this.add.image(400, 500,
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

export class GameScene extends Phaser.Scene {
  //game variables
  private cursorKeys?: Phaser.Types.Input.Keyboard.CursorKeys;
  // private image: Phaser.Physics.Arcade.Sprite;
  private platforms?: Phaser.Physics.Arcade.StaticGroup;
  private player?: Phaser.Physics.Arcade.Sprite
  private stars?: Phaser.Physics.Arcade.Group
  private bombs?: Phaser.Physics.Arcade.Group
  private score = 0;
  private scoreText: Phaser.GameObjects.Text;
  private gameOver = false;
  private context;


  constructor(p: SingleplayergameComponent) {
    super({key: 'game'});
    console.log(p.gameSvcfield.abc);
    this.context = p;
  }


  preload() {
    this.load.image('man', '../../assets/character.png');

    this.load.image('sky', '../../assets/sky.png');
    this.load.image('ground', '../../assets/platform.png');
    this.load.image('star', '../../assets/star.png');
    this.load.image('bomb', '../../assets/bomb.png');
    this.load.spritesheet('dude',
      '../../assets/dude.png',
      { frameWidth: 32, frameHeight: 48 }
    );

  }

  create() {

    this.add.image(400, 300, 'sky');
    // this.add.image(400, 300, 'star');
    this.platforms = this.physics.add.staticGroup();
    const ground = this.platforms.create(400,586, 'ground') as Phaser.Physics.Arcade.Sprite
    ground.setScale(2).refreshBody();
    this.platforms.create(600, 400, 'ground');
    this.platforms.create(50, 250, 'ground');
    this.platforms.create(750, 220, 'ground');

    this.player = this.physics.add.sprite(100, 450, 'dude');
    this.player.setBounce(0.2);
    this.player.setCollideWorldBounds(true);

    this.anims.create({
      key: 'turn',
      frames: [ { key: 'dude', frame: 4 } ],
      frameRate: 20
    });

    this.anims.create({
      key: 'left',
      frames: this.anims.generateFrameNumbers('dude', { start: 0, end: 3 }),
      frameRate: 10,
      repeat: -1
    });

    this.anims.create({
      key: 'right',
      frames: this.anims.generateFrameNumbers('dude', { start: 5, end: 8 }),
      frameRate: 10,
      repeat: -1
    });

    this.physics.add.collider(this.player, this.platforms);


    this.add.text(250, 40, 'startgame', {
      fontSize: '56px', color: '#ffffff'
    });
    this.renderBackButton();
    this.stars = this.physics.add.group({
      key: 'star',
      repeat: 11,
      setXY: { x: 12, y: 0, stepX: 70 }
    });

    this.stars.children.iterate(function (child: Phaser.Physics.Arcade.Image) {
      child.setBounceY(Phaser.Math.FloatBetween(0.4, 0.8));
    });
    this.physics.add.collider(this.stars, this.platforms);
    this.physics.add.overlap(this.player, this.stars, this.handleCollectStar, null, this);

    this.scoreText = this.add.text(16, 16, 'score: 0', { fontSize: '32px', color: '#000' });

    this.bombs = this.physics.add.group();
    this.physics.add.collider(this.bombs, this.platforms);
    this.physics.add.collider(this.bombs, this.player, this.handleHitBombs, null, this);

    // this.image = this.physics.add.sprite(WIDTH / 2, HEIGHT / 2, 'man');
    this.cursorKeys = this.input.keyboard.createCursorKeys();
  }

  private handleCollectStar(player: Phaser.GameObjects.GameObject, star: Phaser.Physics.Arcade.Image) {
    star.disableBody(true, true);
    this.score += 10;
    this.scoreText.setText('Score: ' + this.score);

    if (this.stars.countActive(true) === 0) {
      this.stars.children.iterate(function (child: Phaser.Physics.Arcade.Image) {

        child.enableBody(true, child.x, 0, true, true);

      });

      const x = (this.player.x < 400) ? Phaser.Math.Between(400, 800) : Phaser.Math.Between(0, 400);

      const bomb = this.bombs.create(x, 16, 'bomb');
      bomb.setBounce(1);
      bomb.setCollideWorldBounds(true);
      bomb.setVelocity(Phaser.Math.Between(-200, 200), 20);
    }

  }

  private handleHitBombs(player: Phaser.GameObjects.GameObject, bomb: Phaser.Physics.Arcade.Image){
    this.physics.pause();

    this.player.setTint(0xff0000);
    this.player.anims.play('turn');
    this.gameOver = true;


    this.scene.launch('main');
    this.scene.stop();
    this.context.gameSvcfield.storeScore(this.score);
  }

  // sendGameEvent(gameEvent: string) : void
  // {
  //   this.context.gameEvent.fire(gameEvent);
  // }

  update() {
    // Every frame, we create a new velocity for the sprite based on what keys the player is holding down.
    const velocity = new Phaser.Math.Vector2(0, 0);
    const speed = 160;

    if (this.cursorKeys.left.isDown) {
      velocity.x -= 1;
      this.player.anims.play('left', true);
    }
    else if (this.cursorKeys.right.isDown) {
      velocity.x += 1;
      this.player.anims.play('right', true);
    }
    else {
      velocity.x = 0;
      this.player.anims.play('turn')
    }

    if(this.cursorKeys.up.isDown && this.player.body.touching.down) {
      this.player.setVelocityY(-330);
    }

    // We normalize the velocity so that the player is always moving at the same speed, regardless of direction.
    // const normalizedVelocity = velocity.normalize();
    this.player.setVelocityX(velocity.x * speed);
    // this.player.setVelocity(normalizedVelocity.x * speed, normalizedVelocity.y * speed);


  }

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
      this.context.gameSvcfield.storeScore(this.score);
    });
  }


}
