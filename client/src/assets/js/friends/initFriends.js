"use strict";

const nav_menus = {
    friends: document.querySelector("#my-friends"),
    suggestions:  document.querySelector("#friend-suggestions"),
    requests: document.querySelector("#friend-requests")
};

const menus = {
    friends: document.querySelector("#friends"),
    suggestions: document.querySelector("#suggestions"),
    requests: document.querySelector("#requests")

};

loadFriends();

document.querySelector("#my-friends").addEventListener("click", loadFriends);
document.querySelector("#friend-suggestions").addEventListener("click", loadFriendSuggestions);
document.querySelectorAll("nav li").forEach(li => li.addEventListener("click", switchMenu));

function loadFriends() {
    get(`users/${loadFromStorage("randomId")}/friends`, loadFriendsSuccess);
}

function loadFriendSuggestions() {
    get(`users/${loadFromStorage("randomId")}`);
    get(`users`, loadFriendSuggestionsSuccess);
}

function switchMenu(e) {
    Object.values(nav_menus).forEach(menu => {
        menu.classList.remove("selected");
    });
    Object.values(menus).forEach(menu => {
        menu.classList.add("hidden");
    });

    switch (e.currentTarget.id){
        default:
            document.querySelector("#friends").classList.remove("hidden");
            break;
        case "friend-requests":
            document.querySelector("#requests").classList.remove("hidden");
            break;
        case "friend-suggestions":
            document.querySelector("#suggestions").classList.remove("hidden");
            break;
    }
    e.currentTarget.classList.add("selected");
}
