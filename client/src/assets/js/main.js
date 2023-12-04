"use strict";

document.addEventListener("DOMContentLoaded", initConfig);

function initConfig() {
    if (loadFromStorage("api") === null) {
        loadConfig();
    } else {
        updateCurrentProfile();
    }
}

function getRandomUser(){
  get(`users`, pickRandomUser);
}

function loadConfig() {
  fetch("config.json")
    .then((resp) => resp.json())
    .then((config) => {
      const api = `${config.host ? config.host + "/" : ""}${
        config.group ? config.group + "/" : ""
      }api/`;
      const eb = `${config.host ? config.host + "/" : ""}${
        config.group ? config.group + "/" : ""
      }events/`;
      saveToStorage("api", api);
      saveToStorage("eb",eb);
      getRandomUser();
      initConfig();
    });
}

// Getting initial data
function getInitialInterests(){
  get("interests", initialStoreAllInterests);
}

function getInitialEvents(){
  get("events", loadEvents);
}

function updateCurrentProfile() {
  get(`users/${loadFromStorage("randomId")}`, storeCurrentUser);
}
