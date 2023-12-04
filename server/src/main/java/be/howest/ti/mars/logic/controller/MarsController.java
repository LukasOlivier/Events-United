package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Event;
import be.howest.ti.mars.logic.domain.Ticket;
import be.howest.ti.mars.logic.domain.User;

import java.util.List;
import java.util.Set;

public interface MarsController {

    Event getEvent(int eventId);

    Set<Event> getEvents();

    List<String> getInterests();
    Set<User> getUsers();

    User getUser(int userId);

    void buyTicket(User user, Event event);
    List<Ticket> getTickets(int userId);

    List<String> getInterestsFromUser(int userId);

    void addInterestToUser(User user, String interest);

    void addFriendToUser(User sender, User receiver);

    void removeInterestFromUser(User user, String interest);

    void removeInterestedUser(Event event,User user);

    void removeFriendFromUserFriends(User user, int friendId);

    void addInterestedUser(Event event, User user);

    void createNewEvent(Event event);

    List<User> getFriendsOfUser(int userId);
}
