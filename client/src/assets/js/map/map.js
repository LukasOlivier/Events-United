"use strict";

let _map;
let userCoords;
const markers = [];
const $actionButton = document.querySelector("#martian-action");


init();

function init() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showMap);
  }
  document.querySelector("form").addEventListener("change", filterMartians);
  document.querySelector("#close-modal")
          .addEventListener("click", () => document.querySelector("#modal").close());
  fillInInterests(document.querySelector("#interests"));
}

function showMap(pos) {
  userCoords = [pos.coords.latitude, pos.coords.longitude];

  _map = L.map('map', {
    minZoom: 3,
    maxZoom: 0
  });

  _map.setView(userCoords, 3);

  L.tileLayer('https://cartocdn-gusc.global.ssl.fastly.net/opmbuilder/api/v1/map/named/opm-mars-basemap-v0-2/all/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(_map);

  get("/users", loadUsers);

}

function populateMap(users) {
  users.forEach( (user) => {
    if (user.userId !== loadFromStorage("currentUser").userId) {
      const randomCoords = [userCoords[0] + generateRandomCoordinateOffset(30), userCoords[1] + generateRandomCoordinateOffset(30)];
      createPersonMarker(_map, randomCoords, user.gender, user.userId, user.age, user.interests);
    } else {
      createCurrentPersonMarker(_map, userCoords, user.gender);
    }
  });
}

function createCurrentPersonMarker(map, coords, gender) {
  const currentUserIcon = L.icon({
    iconUrl: `images/currentUserMarker${gender}.png`,
    iconSize:     [60, 60],
    iconAnchor:   [20, 20],
    popupAnchor:  [5, -80]
  });
  const marker = L.marker(coords, {icon: currentUserIcon}) //NOSONAR: this is how leaflett works
                  .addTo(map);
}

function createPersonMarker(map, coords, gender, id, age, interests) {
  const currentUserIcon = L.icon({
    iconUrl: `images/marker-${gender.toLowerCase()}.png`,
    iconSize:     [60, 60],
    iconAnchor:   [20, 20],
    popupAnchor:  [5, -80]
  });
  const marker = L.marker(coords, {icon: currentUserIcon, userId: id, gender: gender, age: age, interests: interests})
                  .on("click", showProfileDetails)
                  .addTo(map);
  markers.push(marker);
}

function showProfileDetails(e) {
  get(`/users/${this.options.userId}`, showMapProfileDetails);
}

function showUserProfileDetails(user) {
  const $modal = document.querySelector("#modal");
  $modal.showModal();
  loadUserDetails(user);

}

function loadUserDetails(user) {
  const $modal = document.querySelector("#modal");

  $modal.querySelector("#name").innerHTML = `${user.firstName} ${user.lastName}`;
  $modal.querySelector("#description").innerHTML = user.description;
  $modal.querySelector("#modal-image").src = `images/profile-picture-${user.gender}.png`;
  $actionButton.addEventListener("click",handleAction);
  $actionButton.setAttribute("userId",user.userId);
  if(!isNotFriend(user.userId)){
    $actionButton.setAttribute("isFriend",true);
    $actionButton.innerHTML = "Send Message";
  }
  loadUserInterests(user.interests);
}

function loadUserInterests(interests) {
  const $interestContainer = document.querySelector("#modal-interests");
  $interestContainer.innerHTML = "";
  if (interests.length === 0) {
    $interestContainer.innerHTML = "<li>No interest.</li>";
  }
  interests.forEach((interest) => {
    const html = `<li>${interest}</li>`;
    $interestContainer.insertAdjacentHTML("beforeend", html);
  });
}

function handleAction(){
  const isFriend = $actionButton.getAttribute("isFriend");
  if (isFriend){
    messageFriend();
  }else{
    addFriend();
  }
}


function addFriend(){
  post(`users/${loadFromStorage("currentUser").userId}/${$actionButton.getAttribute("userId")}/friends/add`, {}, updateCurrentProfile);
  document.querySelector("#modal").close();
}

function filterMartians() {
  resetMarkers();
  filterByAge();
  filterByGender();
  filterByInterest();
}

function resetMarkers() {
  markers.forEach(marker => marker.addTo(_map));
}

function filterByAge() {
  const minAge = document.querySelector("#min-age").value;
  const maxAge = document.querySelector("#max-age").value;
  markers.forEach( (marker) => {
    if (marker.options.age < minAge || marker.options.age > maxAge) {
      _map.removeLayer(marker);
    }
  });
}

function filterByGender() {
  const gender = getValueFromSelectField(document.querySelector("#gender"));
  markers.forEach( (marker) => {
    if (marker.options.gender.toLowerCase() !== gender && gender !== "all") {
      _map.removeLayer(marker);
    }
  });
}

function filterByInterest() {
  const interest = getValueFromSelectField(document.querySelector("#interests"));
  markers.forEach( (marker) => {
    if (!marker.options.interests.includes(interest) && interest !== "all") {
      _map.removeLayer(marker);
    }
  });
}

function generateRandomCoordinateOffset(offsetAmount) {
  return (Math.random() * 2 - 1) * offsetAmount;
}

function generateRandomGender() {
  const genders = ["male", "female"];
  return genders[Math.floor(Math.random() * genders.length)];
}
