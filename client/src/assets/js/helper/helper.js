"use strict";

function createTemplateFromObject($element, queryPrefix, object, exceptions) {
    for (const [key, value] of Object.entries(object)) {
        if (!exceptions.includes(key)) {
            const selector = `${queryPrefix}${key}`.toLocaleLowerCase();
            $element.querySelector(selector).innerHTML = value;
        }
    }
}

function clearTemplateContainer($container) {
    $container.innerHTML = "";
}

function getValueFromSelectField(selectField) {
    return selectField.options[selectField.selectedIndex].value;
}

function fillInInterests($select) {
    const interests = loadFromStorage("allInterests");
    interests.forEach((interest) => {
      $select.insertAdjacentHTML(
        "beforeend",
        `<option value=${interest}>${interest}</option>`
      );
    });
}

function messageFriend() {
    alert("Not in POC scope"); //NOSONAR: alert used for development: feature not implemented.
  }

function navigateToWindow(pageName) {
    window.location.href = `${pageName}.html`;
}

function getIconElement(e){
   return e.currentTarget.parentElement.querySelector("span");
}

function toggleIconFilled(e){
    getIconElement(e).classList.toggle("filled");
}

function returnStatusCode(response) {
    return response.status;
}

function isNotMe(user) {
    return user.userId !== loadFromStorage("currentUser").userId;
}

function isNotFriend(userId) {
    let isNotFriend = true;
    const userFriends = loadFromStorage("currentUser").friends;
    userFriends.forEach((friend) => {
        if (userId === friend.userId) {
            isNotFriend = false;
        }
    });
    return isNotFriend;
}

function favoriteEvent(e) {
    e.preventDefault();
    const favorites = loadFromStorage("currentUser").favorites.map(favorite => parseInt(favorite.id));
    const $clickedElement = e.currentTarget;
    const eventId = $clickedElement.getAttribute("eventId");

    if ($clickedElement.getAttribute("clicked") === "true") {
        return;
    }
    handlePageFavorite($clickedElement,eventId,favorites);
}

function handlePageFavorite($clickedElement,eventId,favorites){
    $clickedElement.setAttribute("clicked","true");
    if (favorites.includes(parseInt(eventId))){
        remove(`users/${loadFromStorage("currentUser").userId}/${eventId}/interested/remove`, handleInterestRequest);
    } else{
        post(`users/${loadFromStorage("currentUser").userId}/${eventId}/interested/add`, {}, handleInterestRequest);
    }
}

function toggleFavoriteIconFilled(response){
    response.json().then((user) => {
        saveToStorage("currentUser", user);
        const $icon = document.querySelector('[clicked="true"]');
        $icon.setAttribute("clicked","false");
        $icon.classList.toggle("filled");
    });
}
