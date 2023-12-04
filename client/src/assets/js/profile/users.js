function insertUserInfo(user) {
    const $article = document.querySelector("article");
    $article.querySelector("#profile-picture").src = `images/profile-picture-${user.gender}.png`;
    const exceptions = ["userId", "friends", "tickets", "birthDate", "interests", "favorites"];

    createTemplateFromObject($article, "#user-", user, exceptions);
}
