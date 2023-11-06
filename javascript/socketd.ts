interface Consumer<T> {
    (t: T): void
}

interface Entity {
    metaString: string
    data: BinaryData
}

class Message {
    constructor(sid: string, entity: Entity){
        this.sid = sid;
        this.entity = entity;
    }
    sid: string
    entity?: Entity
}

class Frame {
    flag: number
    message: Message
    constructor(flag:number, message: Message){
        this.flag = flag;
        this.message = message;
    }
}

class ClientConfig {
    readonly url: string
    schema: string
    constructor(url:string){
        this.url = url;
    }
}

interface Channel {
    config: ClientConfig

    send(frame): void
}

interface Session {
    channel: Channel

    send(topic: string, entity: Entity)

    sendAndRequest(topic: string, entity: Entity): Entity

    sendAndSubscribe(topic: string, entity: Entity, consumer: Consumer<Entity>)
}


interface ClientConnector {
    config: ClientConfig

    connect(): Channel
}

class ClientChannel implements Channel{
    real: Channel
    connector: ClientConnector
    config: ClientConfig;
    constructor(channel: Channel, connector: ClientConnector){
        this.real = channel;
        this.connector = connector;
    }

    send(frame): void {
    }
}

class Client {
    config: ClientConfig

    constructor(url:string) {
    }

    listen(listener): Client{

    }

    open(): Session{

    }
}

/**
 * let connentor = new ClientConnector(this.config);
 *  return new Session(new ClientChannel(connentor.connect(), connentor));
 * */


var SocketD={
    createClient(url) : Client {
        let config = new ClientConfig(url);
        return new Client(url);
    }
}

let session = SocketD.createClient("tcp://xxx.xxx.x")
    .config({replyTimeout: 12})
    .listen({
        onOpen: function (session){

        },
        onMessage: function (session, message){

        },
        onClose: function (session){

        },
        onError: function (session, error){

        }
    })
    .open();

session.send("/demo", {data: ""});

let entity = session.sendAndRequest("/demo", {data: ""});
session.sendAndSubscribe("/demo",{data:""},  entity=>{

});