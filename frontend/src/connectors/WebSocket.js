import stomp from 'stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;
let connected = false;

let subscriptions = [];
let subs = {}

function conn(name){
    stompClient.connect({"user-name":name}, frame => {
        connected=true;
        console.log("connected to ws.. subscribing to topics")
        subscriptions.map(s => subscribe(s.topic, s.callback));
        subscriptions = [];        
    })
}

export function connect(name){
    const sock = new SockJS("/ws");
    stompClient= stomp.over(sock);
    connected=false;
    conn(name);

}

export function subscribe(topic, callback){
    console.log("subscribe to "+topic)
    if(connected){
        subs[topic] = stompClient.subscribe(topic, callback);
    } else {
        subscriptions.push({topic:topic,callback:callback})
        console.log("not connected to ws");
    }
}
 
export function unsubscribe(){
    for (const key of Object.keys(subs)) {
        if(subs[key]){
            subs[key].unsubscribe();
            subs[key]=null;
        } else {
            delete subs[key];
        }
    }
}