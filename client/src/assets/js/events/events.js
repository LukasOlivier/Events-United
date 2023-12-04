"use strict";

function renderEvents(events) {
    const $container = document.querySelector("#event-container");
    clearTemplateContainer(document.querySelector("#event-container"));
    events = filterEvents(events);
    events.forEach(event => {
        createEventTemplate(event, $container);
    });
    addEventListenersForEvents();
}

function addEventListenersForEvents() {
    document.querySelectorAll(".favorite").forEach(elem => {
        elem?.addEventListener("click", favoriteEvent);
    });
    document.querySelectorAll(".view-more").forEach(elem => {
        elem?.addEventListener("click", handleViewMore);
    });
}


function adjustRangeValue(e) {
 document.querySelector(`label[for=${e.currentTarget.id}] span`).innerHTML = e.currentTarget.value;
}

function filterEvents(events) {
    let filteredEvents = events;
    const selectedOrderBy = getValueFromSelectField(document.querySelector("#order-by"));
    if (document.querySelector("#interests").value !== "all") {
        filteredEvents = events.filter(event => event.interest.toLowerCase() === document.querySelector("#interests").value);
    }
    if (document.querySelector("#from").value !== "") {
        filteredEvents = filteredEvents.filter(event => event.startDate >= document.querySelector("#from").value);
    }
    if (document.querySelector("#until").value !== "") {
        filteredEvents = filteredEvents.filter(event => event.endDate <= document.querySelector("#until").value);
    }
    if (selectedOrderBy === "date") {
        filteredEvents = filteredEvents.sort((a, b) => a.startDate < b.startDate);
    }
    if (selectedOrderBy === "price") {
        filteredEvents = filteredEvents.sort((a, b) => a.ticketPrice - b.ticketPrice);
    }
    if (document.querySelector("#descending").checked) {
        filteredEvents.reverse();
    }
    filteredEvents = filteredEvents.filter(event => event.ticketPrice <= parseInt(document.querySelector("#max-price").value));
    return filteredEvents;
}

function createEventTemplate(event, $container) {
    const favorites = loadFromStorage("currentUser").favorites.map(favorite => favorite.id);
    const exceptions = ["endDate", "id", "attendees", "interested"];
    const $template = document.querySelector("#event-template").content.firstElementChild.cloneNode(true);
    $template.querySelector(".event-image").src = `images/${event.interest.toLowerCase()}.jpg`;
    if(favorites.includes(parseInt(event.id))) {
        $template.querySelector(".favorite").classList.add("filled");
    }
    $template.querySelectorAll("p").forEach($p => $p.setAttribute("eventId", event.id));
    createTemplateFromObject($template, ".event-", event, exceptions);
    $container.insertAdjacentHTML("beforeend", $template.outerHTML);
}

function adjustDistanceValue() {
    const sliderValue = document.querySelector("#max-distance").value;
    document.querySelector("#distance-input-value").innerHTML = sliderValue;
    renderEvents();
}

function handleViewMore(e) {
    const eventId = e.currentTarget.getAttribute("eventId");
    window.location.href = "inspect-event.html";
    saveToStorage("eventId", eventId);
}

const { showNotificationPrompt, askNotificationPermission, closePopUp } = (function () {
    const $modal = document.querySelector("#modal");
    const show = function(){
        Notification.requestPermission().then(function (status) {
            if (status) {
                $modal.close();
            }
        });
        document.querySelector("#modal").innerHTML = "Please click 'Allow'";
    };
    const ask = function(){
        if(Notification.permission !== "granted"){
            $modal.showModal();
        }
    };
    const close = function(){
        $modal.close();
    };
    return { showNotificationPrompt: show, askNotificationPermission: ask, closePopUp: close };
})();
