package be.howest.ti.mars.logic.domain;

import java.util.List;

public enum Interest {
    CARS("cars"),
    MUSIC("music"),
    MOVIES("movies"),
    COMPUTERS("computers"),
    SPORTS("sports"),
    FOOD("food"),
    SANDSURFING("sandsurfing"),
    GAMES("games"),
    DISCOVERING("discovering"),
    FIGHTS("fights"),
    ANIMALS("animals"),
    PSYCHOLOGY("psychology"),
    SOCIALMEDIA("socialmedia"),
    ROVERS("rovers"),
    CRAFTS("crafts"),
    EARTHWATCHING("earthwatching"),
    BOOKS("books"),
    TRAVEL("travel");

    public Interest interestFromString(String query){
        return Interest.valueOf(query);
    }

    private final String interests;
    Interest(String interest){
        this.interests = interest;
    }

    public String getInterest() {
        return interests;
    }

    public static List<Interest> allInterests(){
        return List.of(Interest.values());
    }
}

