package be.howest.ti.mars.logic.domain;

public class EventExtraInformation {

    private final int ticketPrice;
    private final int interested;
    private final int attendees;
    private final String location;

    public EventExtraInformation(int ticketPrice, int interested, int attendees, String location) {
        this.ticketPrice = ticketPrice;
        this.interested = interested;
        this.attendees = attendees;
        this.location = location;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public int getInterested() {
        return interested;
    }

    public int getAttendees() {
        return attendees;
    }

    public String getLocation() {
        return location;
    }
}
