package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Objects;

public class Event {
    public static final int NO_ASSIGNED_ID = -1;
    private final int id;
    private final String name;
    private final String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    private final Interest interest;
    private final EventExtraInformation eventExtraInformation;


    public Event(int id, String name, String description, Date startDate, Date endDate, Interest interest, EventExtraInformation eventExtraInformation) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        setStartDate(Objects.requireNonNull(startDate));
        setEndDate(Objects.requireNonNull(startDate), Objects.requireNonNull(endDate));
        this.interest = Objects.requireNonNull(interest);
        this.eventExtraInformation = eventExtraInformation;
    }

    public Event(String name, String description, Date startDate, Date endDate, Interest interest, EventExtraInformation eventExtraInformation) {
        this(NO_ASSIGNED_ID, name, description, startDate, endDate, interest, eventExtraInformation);
    }

    public int getInterested() {
        return eventExtraInformation.getInterested();
    }

    public int getAttendees() {
        return eventExtraInformation.getAttendees();
    }

    public int getTicketPrice() {
        return eventExtraInformation.getTicketPrice();
    }

    public String getLocation() {
        return eventExtraInformation.getLocation();
    }

    public void setStartDate(Date startDate) {
        Date todayDate = new Date();
        if (startDate.after(todayDate)) {
            this.startDate = startDate;
        } else {
            throw new IllegalStateException("Start date can not be earlier than current date");
        }
    }

    public void setEndDate(Date startDate, Date endDate) {
        if (endDate.after(startDate)) {
            this.endDate = endDate;
        } else {
            throw new IllegalStateException("End date can not be earlier than start date");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {

        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Interest getInterest() {
        return interest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", interest=" + interest +
                ", ticketPrice=" + eventExtraInformation.getTicketPrice() +
                '}';
    }
}
