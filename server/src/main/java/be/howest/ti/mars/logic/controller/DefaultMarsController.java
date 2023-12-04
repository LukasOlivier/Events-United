package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Event;
import be.howest.ti.mars.logic.domain.Ticket;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * The controller shouldn't even know that it is used in an API context..
 *
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Keep libraries and frameworks out of the logic packages as much as possible.
 * Do not be afraid to create your own Java classes if needed.
 */
public class DefaultMarsController implements MarsController {

    private static final String MSG_USER_ID_UNKNOWN = "No user with id: %d";
    private static final String MSG_EVENT_ID_UNKNOWN = "No event with id: %d";

    static MarsRtcBridge marsRtcBridge;

    public static void setMarsRtcBridge(MarsRtcBridge marsRtcBridge) {
        DefaultMarsController.marsRtcBridge = marsRtcBridge;
    }

    @Override
    public Event getEvent(int eventId) {
        Event event = Repositories.getH2Repo().getEvent(eventId);
        if (event == null)
            throw new NoSuchElementException(String.format(MSG_EVENT_ID_UNKNOWN, eventId));

        return event;
    }

    @Override
    public Set<Event> getEvents() {
        return Repositories.getH2Repo().getEvents();
    }

    @Override
    public List<String> getInterests() {
        return Repositories.getH2Repo().getInterests();
    }

    @Override
    public Set<User> getUsers() {
        return Repositories.getH2Repo().getUsers();
    }

    @Override
    public User getUser(int userId) {
        User user = Repositories.getH2Repo().getUser(userId);
        if (user == null)
            throw new NoSuchElementException(String.format(MSG_USER_ID_UNKNOWN, userId));

        return user;
    }

    @Override
    public void buyTicket(User user, Event event) {
        Repositories.getH2Repo().insertTicket(user.getUserId(),event.getId());
        Repositories.getH2Repo().updateAttendees(event.getId());
    }

    @Override
    public List<Ticket> getTickets(int userId) {
        return Repositories.getH2Repo().getTickets(userId);
    }

    @Override
    public List<String> getInterestsFromUser(int userId) {
        return Repositories.getH2Repo().getInterestsFromUser(userId);
    }

    @Override
    public List<User> getFriendsOfUser(int userId) {
        return Repositories.getH2Repo().getFriendsOfUser(userId);
    }

    @Override
    public void addInterestToUser(User user, String interest) {
        Repositories.getH2Repo().addInterestToUser(user.getUserId(),interest);
    }

    @Override
    public void addFriendToUser(User sender, User receiver) {
        Repositories.getH2Repo().addFriendToUser(sender.getUserId(), receiver.getUserId());
        marsRtcBridge.sendFriendNotification(sender, receiver);
    }

    @Override
    public void removeInterestFromUser(User user, String interest) {
        Repositories.getH2Repo().removeInterestFromUser(user.getUserId(),interest);
    }

    @Override
    public void removeFriendFromUserFriends(User user, int friendId) {
        Repositories.getH2Repo().removeFriendFromUserFriends(user.getUserId(),friendId);
    }

    @Override
    public void addInterestedUser(Event event, User user) {
        Repositories.getH2Repo().addInterestedUser(event.getId(),user.getUserId());
        Repositories.getH2Repo().updateInterested(event.getId(),1);
    }

    @Override
    public void createNewEvent(Event event) {
        Repositories.getH2Repo().createNewEvent(event);
    }

    @Override
    public void removeInterestedUser(Event event, User user) {
        Repositories.getH2Repo().removeInterestedUser(event.getId(),user.getUserId());
        Repositories.getH2Repo().updateInterested(event.getId(),-1);
    }
}