package be.howest.ti.mars.logic.domain;

public class Ticket {
    Event event;

    int userId;

    public Ticket(Event event, int user) {
        this.event = event;
        this.userId = user;
    }

    public Event getEvent() {
        return event;
    }

    public int getUserId() {
        return userId;
    }
}
