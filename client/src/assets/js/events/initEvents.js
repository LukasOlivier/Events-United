"use-strict";
getInitialEvents();

function initEvents(events) {
    renderEvents(events);
    document.querySelector("#from").addEventListener("change", () => renderEvents(events));
    document.querySelector("#until").addEventListener("change", () => renderEvents(events));
    document.querySelector("#interests").addEventListener("change", () => renderEvents(events));
    document.querySelector("#max-distance").addEventListener("change", () => renderEvents(events));
    document.querySelector("#max-price").addEventListener("change", () => renderEvents(events));
    document.querySelector("#order-by").addEventListener("change", () => renderEvents(events));
    document.querySelectorAll("input[type=radio]").forEach(radioButton => radioButton.addEventListener("click", () => renderEvents(events)));
    document.querySelector("#logo").addEventListener("click", () => window.location.href = "index.html");
    document.querySelectorAll("input[type=range]").forEach(input => input.addEventListener("input", adjustRangeValue));
    document.querySelector("#create-event").addEventListener("click",() => navigateToWindow("create-event"));
    document.querySelector("#yes").addEventListener("click",showNotificationPrompt);
    document.querySelector("#no").addEventListener("click", closePopUp);
    fillInInterests(document.querySelector("#interests"));
    askNotificationPermission();
}
