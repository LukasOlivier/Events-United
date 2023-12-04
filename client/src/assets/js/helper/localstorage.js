"use strict";

function saveToStorage(key, value) {
    if (localStorage) {
        return localStorage.setItem(key,JSON.stringify(value));
    }else {
        return null;
    }
}

function loadFromStorage(key) {
    if (localStorage) {
        return JSON.parse(localStorage.getItem(key));
    }else {
        return null;
    }
}

function deleteFromStorage(key){
    if (localStorage) {
        localStorage.removeItem(key);
    }
    return null;
}
