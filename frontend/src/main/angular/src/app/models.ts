export class tokens {
  access_token: string;
  refresh_token: string;
}

export class link {
  link: string
}

export class link1 {
  accessToken: string
}

export class chatmessage {

  constructor(message: string, user: string) {
    this.message = message;
    this.user = user;
  }

  message: string;
  user: string;
}

export class simpleperson {
  name: string;
  email: string;
  photoURL: string;
}


//NOT USED
export class gamestats {
  numPlayers: number;
  playersStats: Map<string, playerstats>;
}

export class playerstats {
  rotation: number;
  x: number;
  y: number;
  playerId: string;
}

