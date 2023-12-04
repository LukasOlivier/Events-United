"use-strict";

function get(uri, successHandler, failureHandler) {
    const api = loadFromStorage("api");
    const request = new Request(api + uri, {
        method: "GET",
    });
    call(request, successHandler, failureHandler);
}

function post(uri, body, successHandler, failureHandler) {
    const api = loadFromStorage("api");
    const request = new Request(api + uri, {
        method: "POST",
        headers: {
        "Content-type": "application/json;",
        },
        body: JSON.stringify(body),
    });

    call(request, successHandler, failureHandler);
}

function put(uri, body, successHandler, failureHandler) {
    const api = loadFromStorage("api");
    const request = new Request(api + uri, {
        method: "PUT",
        headers: {
        "Content-type": "application/json;",
        },
        body: JSON.stringify(body),
    });

    call(request, successHandler, failureHandler);
}

function remove(uri, successHandler, failureHandler) {
    const api = loadFromStorage("api");
    const request = new Request(api + uri, {
        method: "DELETE",
    });
    call(request, successHandler, failureHandler);
}

function updateFavorites(response) {
        response.json().then((user) => {
            const favorites = user.favorites.map(favorite => parseInt(favorite.id));
            const eventId = parseInt(loadFromStorage("eventId"));
            if(favorites.includes(eventId)){
                remove(`users/${loadFromStorage("currentUser").userId}/${eventId}/interested/remove`, updateInterestedPeople);
            } else{
                post(`users/${loadFromStorage("currentUser").userId}/${eventId}/interested/add`, {}, updateInterestedPeople);
            }
        });
}

function updateFavoritesOnMainScreen(response) {
    response.json().then((user) => {
        const favorites = user.favorites.map(favorite => parseInt(favorite.id));
        const eventId = parseInt(loadFromStorage("eventId"));
        if(favorites.includes(eventId)){
            remove(`users/${loadFromStorage("currentUser").userId}/${eventId}/interested/remove`);
        } else{
            post(`users/${loadFromStorage("currentUser").userId}/${eventId}/interested/add`);
        }
    });
}

function refreshEventPage(){
    updateCurrentProfile();
    get(`events/${loadFromStorage("eventId")}`, processEventDetails);
}
function showMapProfileDetails(response) {
    response.json().then(
        (user) => {
            showUserProfileDetails(user);
        }
    );
}

function logJson(response) {
    response.json().then(console.log);
}

function logError(error) {
    response.json().then(console.log);
}

function call(request, successHandler, errorHandler) {
    fetch(request).then(successHandler).catch(errorHandler);
}

function loadEvents(response) {
    response.json().then((events) => initEvents(events));
}

function loadUserTickets(response) {
    response.json().then((tickets) => renderTickets(tickets));
}

function loadEventDetails() {
    const eventId = loadFromStorage("eventId");
    get(`events/${eventId}`, processEventDetails);
}

function updateInterestedPeople(response) {
    response.json().then(event => {
        document.querySelector("#event-interested").innerHTML = event.interested;
    });
}

function processEventDetails(response) {
    response.json().then((currentEvent) => renderEventDetails(currentEvent));
}

function storeCurrentUser(response) {
    response.json().then((user) => saveToStorage("currentUser", user));
}

function loadProfile(response) {
    response
      .json()
      .then((user) => {
        saveToStorage("currentUser", user);
        return user;
      })
      .then((user) => {
        insertUserInfo(user);
        return user;
      })
      .then((user) => insertInterests(user))
      .then(addEventHandlers());
}

function createEventSuccess(response) {
    const statusCode = returnStatusCode(response);
    response.json().then(fillInResponseText(statusCode));
}

function loadUsers(response) {
    response.json()
            .then(users => populateMap(users));
}

function loadFriendsSuccess(response) {
    response.json()
            .then(friends => renderFriends(friends));
}

function loadFriendSuggestionsSuccess(response) {
    response.json()
            .then(user => renderFriendSuggestions(user));
}

function storeCurrentEvent(response) {
    response.json().then((user) => saveToStorage("currentEvent", user));
}

function handleInterestRequest(response){
    response.json().then((event) => {
        if (document.querySelector("body").id !== "index") {
            updateInterestedPeople(event.interested);
        }
        get(`users/${loadFromStorage("randomId")}`,toggleFavoriteIconFilled);
    });
}

// Saving initial data
function initialStoreAllInterests(response) {
    response.json().then((interests) => {
        saveToStorage("allInterests", interests);
        getInitialEvents();
    });
}

function initialStoreUser(response){
    response.json().then((user) => {
        saveToStorage("currentUser", user);
        getInitialInterests();
    });
}

function pickRandomUser(response){
    response.json().then((users) => {
      const offByOneError = 1;
      const upperBound = users.length - offByOneError;
      const randomNumber = Math.floor(Math.random() * upperBound) + offByOneError;
      saveToStorage("randomId", randomNumber);
      get(`users/${loadFromStorage("randomId")}`, initialStoreUser);
    });
}
