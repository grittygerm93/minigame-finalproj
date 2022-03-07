export class tokens {
  access_token: string;
  refresh_token: string;
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
