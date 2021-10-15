import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { GameInfoPage} from "../pages/game-info.page";

export class WebSocketAPI {
  webSocketEndPoint: string = 'http://localhost:8080/api/websocket';
  topic: string = "/socket/notify";
  stompClient: any;
  game: GameInfoPage;
  constructor(appComponent: GameInfoPage){
    this.game = appComponent;
  }
  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function () {
      _this.stompClient.subscribe(_this.topic, function (sdkEvent: any) {
        _this.onMessageReceived(sdkEvent);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  };

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error: string) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

  /**
   * Send message to sever via web socket
   * @param {*} message
   */
  _send(message: string | number) {
    console.log("ID " + message + "calling api via web socket");
    this.stompClient.send("/app/websocket", {}, JSON.stringify(message));
  }

  onMessageReceived(message: String) {
    console.log("New message recieved!");
    this.game.handleMessage();
  }
}