"use strict";

const overlays = {
    main:  document.querySelector("main"),
    form:  document.querySelector(".form"),
    overview: document.querySelector(".overview"),
    paymentSuccessful: document.querySelector(".payment-successful")
};

loadEventDetails();
addEventListeners();


function addEventListeners() {
    document.querySelector("#go-to-checkout").addEventListener("click", () => openOverlay(overlays.overview));
    document.querySelector("#go-back-overview").addEventListener("click", () => openOverlay(overlays.form));
    document.querySelectorAll(".go-back-to-event").forEach($button => $button.addEventListener("click", () => openOverlay(overlays.main)));
    document.querySelector("#buy-button").addEventListener("click",openOverLayIfNotYetBought);
    document.querySelector("#favorite-event").addEventListener("click", favoriteEvent);
    document.querySelector("#going-event").addEventListener("click", toggleIconFilled);
    document.querySelector(".buyer").innerHTML = `${loadFromStorage("currentUser").firstName}  ${loadFromStorage("currentUser").lastName}`;

    document.querySelector(".total-price span").innerHTML = loadFromStorage("currentEvent").ticketPrice;

    document.querySelector("#confirm-ticket").addEventListener("click", buyTicket);

    document.querySelector("#check-tickets").addEventListener("click", switchToTicketsPage);
}

function openOverLayIfNotYetBought(e) {
    if (!e.currentTarget.classList.contains("already-bought")) {
        openOverlay(overlays.form);
    }
}


function switchToTicketsPage() {
    window.location.href = "tickets.html";
}

function renderEventDetails(currentEvent) {
    const $favoriteButton = document.querySelector("#favorite-event");
    saveToStorage("currentEvent", currentEvent);
    createTemplateFromObject(document.querySelector("body"),"#event-",currentEvent,["id","endDate"]);
    document.querySelector("#event-image").src = `images/${currentEvent.interest.toLowerCase()}.jpg`;
    $favoriteButton.setAttribute("eventId",currentEvent.id);
    const favorites = loadFromStorage("currentUser").favorites.map(favorite => parseInt(favorite.id));
    if (favorites.includes(currentEvent.id)) {
        $favoriteButton.classList.add("filled");
    }
    const tickets = loadFromStorage("currentUser").tickets.map(ticket => parseInt(ticket.event.id));
    if (tickets.includes(currentEvent.id)) {
        const $button = document.querySelector("#buy-button");
        $button.classList.add("already-bought");
        $button.classList.remove("hoverable");
    }
}

function openOverlay($overlayToOpen) {
    Object.entries(overlays).forEach(([key,overlay]) => {
        overlay.classList.add("hidden");
     });
    $overlayToOpen.classList.remove("hidden");
}

function updateInterestedPeople(count){
    document.querySelector("#event-interested").innerHTML = count;
}


function buyTicket() {
    const userId = loadFromStorage("currentUser").userId;
    const eventId = loadFromStorage("eventId");
    post(`users/${userId}/${eventId}/ticket/buy`, {}, refreshEventPage);
    openOverlay(overlays.paymentSuccessful);
}
