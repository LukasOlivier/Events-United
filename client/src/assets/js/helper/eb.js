const CHNL_TO_CLIENT_UNICAST = "events.friend.request";

openSocket();

function openSocket() {

	const eb = new EventBus(loadFromStorage("eb"));
    const userId = loadFromStorage("randomId");

    eb.onopen = function() {
        eb.registerHandler(CHNL_TO_CLIENT_UNICAST + userId, onMessage);
	};
}

function onMessage(error, message) {
    const notification = new Notification("Friend request", { //NOSONAR: this is how the notification API works
        body : message.body,
    });
}
