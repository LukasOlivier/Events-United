"use strict";

document.addEventListener("DOMContentLoaded", addFriend);

function addFriend(e){
    e.preventDefault();
    const mockFriendId = 1;

    if (isNotMe(mockFriendId) && isNotFriend(mockFriendId)) {
        const currentUser = loadFromStorage("currentUser");
        currentUser.friends.push(mockFriendId);
        post(`users/${loadFromStorage("currentUser").userId}/${mockFriendId}/friends/add`,{}, fillInformation);
    }
}

function fillInformation(response)  {
    if (returnStatusCode(response) === 200) {
        const $textAddedTitle = document.querySelector("#main-text");
        $textAddedTitle.innerHTML = "Friend added :)";
    } else if (returnStatusCode(response) === 500) {
        const $textAddedTitle = document.querySelector("#main-text");
        $textAddedTitle.innerHTML = "Friend not added :(";
    }
}
