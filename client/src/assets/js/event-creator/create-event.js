"use strict";

loadInterests();
const { sendEventRequest, closeConfirmationPopUp, openConfirmationPopUp } = (function () {
    const $modal = document.querySelector("#modal");
    const send = function(){
        const body = createEventBody();
        post("events/create", body, createEventSuccess);
    };
    const close = function(){
        $modal.close();
    };
    const open = function(e){
        $modal.showModal();
    };
    return { sendEventRequest: send, closeConfirmationPopUp: close, openConfirmationPopUp: open };
})();

document.querySelector("#create-event").addEventListener("click",handleSubmitAction);
document.querySelector("#cancel-event").addEventListener("click",handleSubmitAction);
document.querySelector("#back-to-main").addEventListener("click", () => navigateToWindow("index"));
document.querySelector("#create-again").addEventListener("click", () => navigateToWindow("create-event"));

document.querySelector("#confirm").addEventListener("click",sendEventRequest);
document.querySelector("#cancel").addEventListener("click",closeConfirmationPopUp);

function loadInterests(){
    const $select = document.querySelector("#interests");
    fillInInterests($select);
}

function fillInResponseText(statusCode){
    const $response = document.querySelector("#creation-response");
    $response.classList.remove("hidden");
    document.querySelector("form").classList.add("hidden");
    closeConfirmationPopUp();
    if (statusCode === 200){
        $response.querySelector(".response-text").innerHTML = `<h2>Your event request has been sent successfully to Events United.</h2>
        <p>We will keep you updated via e-mail. Be aware that you can't accept this event request yourself.</p>`;
    }else{
        $response.querySelector(".response-text").innerHTML = `<h2>Something went wrong while processing your request.</h2>`;
    }
}

function handleSubmitAction(e){
    e.preventDefault();
    if(e.currentTarget.id === "cancel-event"){
        window.location.href = "index.html";
    }else{
        openConfirmationPopUp();
    }
}

function createEventBody() {
    return {
        title: document.querySelector("#title").value,
        description: document.querySelector("#description").value,
        location: document.querySelector("#location").value,
        startDate: document.querySelector("#start-date").value,
        endDate: document.querySelector("#end-date").value,
        interest: getValueFromSelectField(document.querySelector("#interests")).toUpperCase(),
        ticketPrice: parseInt(document.querySelector("#ticket-price").value)
    };
}
