"use strict";

function renderFriends(friends) {
    const $template = document.querySelector("#friend-template").content.firstElementChild.cloneNode(true);
    const $container = document.querySelector("#friends ul");
    clearTemplateContainer($container);
    const exceptions = ["userId", "birthDate", "height", "gender", "description", "interests", "friends", "tickets", "age", "favorites"];
    if(friends.length){
        document.querySelector("#friends .no-items").classList.add("hidden");
    }
    friends.forEach(friend => {
        $template.id = `liOfUser${friend.userId}`;
        $template.querySelector("img").src = `images/profile-picture-${friend.gender}.png`;
        createFriendTemplate($template, $container, friend, exceptions);
    });
    document.querySelectorAll(".remove-friend").forEach(button => button.addEventListener("click", removeFriend));
    document.querySelectorAll(".send-message").forEach(button => button.addEventListener("click", messageFriend));
}

function createFriendTemplate($template, $container, friend, exceptions) {
    const $interestsContainer = $template.querySelector(".interests");
    createTemplateFromObject($template, ".", friend, exceptions);
    loadFriendInterests($interestsContainer, friend.interests);
    $template.querySelectorAll("button").forEach(button => {
        button.setAttribute("userId", friend.userId);
    });
    $container.insertAdjacentHTML("beforeend", $template.outerHTML);
}

function loadFriendInterests($container, interests) {
    $container.innerHTML = "";
    if (interests.length === 0) {
        $container.innerHTML = `<li class="no-interests"><p>No interests.</p></li>`;
    }else{
        interests.forEach((interest) => {
            $container.innerHTML += `<li>${interest}</li>`;
        });
    }
}

function renderFriendSuggestions(users) {
    const $template = document.querySelector("#suggestions-template").content.firstElementChild.cloneNode(true);
    const $container = document.querySelector("#suggestions ul");
    clearTemplateContainer($container);
    const exceptions = ["userId", "birthDate", "height", "gender", "description", "interests", "friends", "tickets", "age", "favorites"];
    users.forEach(user => {
        if (hasCommonInterests(user) && isNotMe(user) && isNotFriend(user.userId)) {
            document.querySelector("#suggestions .no-items").classList.add("hidden");
            $template.id = `liOfUser${user.userId}`;
            $template.querySelector("img").src = `images/profile-picture-${user.gender}.png`;
            createFriendTemplate($template, $container, user, exceptions);
        }
    });
    document.querySelectorAll(".remove-suggestion").forEach(button => button.addEventListener("click", removeSuggestion));
    document.querySelectorAll(".send-friend-request").forEach(button => button.addEventListener("click", addFriend));
}


function hasCommonInterests(user) {
    let hasCommon = false;
    const myInterests = loadFromStorage("currentUser").interests;
    myInterests.forEach((interest) => {
        if (user.interests.includes(interest)) {
            hasCommon = true;
        }
    });
    return hasCommon;
}

function removeSuggestion(e){
    const friendId = e.currentTarget.getAttribute("userId");
    document.querySelector(`#liOfUser${friendId}`).remove();
}
function addFriend(e){
    const friendId = e.currentTarget.getAttribute("userId");
    post(`users/${loadFromStorage("currentUser").userId}/${friendId}/friends/add`,updateCurrentProfile);
    document.querySelector(`#liOfUser${friendId}`).remove();
}

function removeFriend(e){
    const friendId = e.currentTarget.getAttribute("userId");
    remove(`users/${loadFromStorage("currentUser").userId}/${friendId}/friends/remove`, updateCurrentProfile);

    document.querySelector(`#liOfUser${friendId}`).remove();
 }
