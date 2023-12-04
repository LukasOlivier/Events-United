package be.howest.ti.mars.logic.domain;

import java.util.List;

public class UserExtraInformation {

    private final List<String> interest;
    private final List<User> friends;
    private final List<Ticket> tickets;
    private final List<Event> favorites;

    public UserExtraInformation(List<String> interest, List<User> friends, List<Ticket> tickets, List<Event> favorites) {
        this.interest = interest;
        this.friends = friends;
        this.tickets = tickets;
        this.favorites = favorites;
    }

    public List<String> getInterest() {
        return interest;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public List<Event> getFavorites() {
        return favorites;
    }
}
