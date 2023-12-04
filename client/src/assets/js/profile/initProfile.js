"use-strict";

initProfile();

function initProfile() {
    get(`users/${loadFromStorage("randomId")}`, loadProfile);
}
