document.addEventListener("DOMContentLoaded", initTickets);

function initTickets() {
  get(`users/${loadFromStorage("randomId")}/tickets`, loadUserTickets);
}

function renderTickets(tickets) {
  if (tickets.length === 0) {
    document.querySelector("#no-tickets").classList.remove("hidden");
    document.querySelector("#ticket-container").classList.add("hidden");
  } else {
    const $template = document
      .querySelector("#ticket-template")
      .content.firstElementChild.cloneNode(true);
    const $container = document.querySelector("#ticket-container");
    tickets.forEach((ticket) => {
      createTemplateFromObject($template, ".event-", ticket.event, [
        "id",
        "attendees",
        "description",
        "endDate",
        "interested",
      ]);
      $container.insertAdjacentHTML("beforeend", $template.outerHTML);
    });
  }
}
