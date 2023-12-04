function insertInterests() {
    loadFromStorage("allInterests").forEach(interest => {
        const $interest = createInterestElement(interest);
        if (loadFromStorage("currentUser").interests.includes(interest)) {
            document.querySelector("#interests-container").appendChild($interest);
            $interest.dataset.previousParent = "interests-container";
        } else {
            document.querySelector("#non-interests-container").appendChild($interest);
            $interest.dataset.previousParent = "non-interests-container";
        }
    });
}

function createInterestElement(interest) {
    const $interest = document.createElement('li');
    $interest.dataset.interest = interest;
    $interest.setAttribute("draggable", "true");
    $interest.classList.add("hoverable");
    $interest.innerText = interest;
    $interest.addEventListener("dragstart", handleDragStart);
    $interest.addEventListener("dragend", handleDragEnd);
    return $interest;
}

function addEventHandlers() {
    document.querySelectorAll("#interests li").forEach($li => {
        $li.addEventListener("dragstart", handleDragStart);
        $li.addEventListener("dragend", handleDragEnd);
    });
    document.querySelectorAll("#interests ul").forEach($ul => {
        $ul.addEventListener("dragover", handleDragOver);
        $ul.addEventListener("drop", handleDrop);
    });
}

function handleDragOver(ev) {
    ev.preventDefault();
}

function handleDrop(ev) {
    const interestAsString = ev.dataTransfer.getData("text/plain").toString();
    const $interest = createInterestElement(interestAsString);
    ev.currentTarget.appendChild($interest);
    if (ev.currentTarget.id === "interests-container"){
        postInterest(interestAsString);
    }else if (ev.currentTarget.id === "non-interests-container"){
        deleteInterest(interestAsString);
    }
}

function postInterest(interest) {
    const userId = loadFromStorage("currentUser").userId;
    post(`users/${userId}/${interest}/interests/add`);
}

function deleteInterest(interest) {
    const userId = loadFromStorage("currentUser").userId;
    remove(`users/${userId}/${interest}/interests/remove`);
}

function handleDragStart(ev) {
    ev.dataTransfer.setData("text/plain", ev.currentTarget.dataset.interest);
}

function handleDragEnd(ev) {
    ev.preventDefault();
    ev.currentTarget.parentElement.addEventListener("dragover", handleDragOver);
    ev.currentTarget.parentElement.addEventListener("drop", handleDrop);
    ev.currentTarget.remove();
}
