package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.*;

import java.util.*;

public class MockMarsController implements MarsController {

    @Override
    public Event getEvent(int eventId) {
       return Repositories.getH2Repo().getEvent(eventId);
    }

    @Override
    public Set<Event> getEvents(){
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
        return Repositories.getH2Repo().getUser(userId);
    }

    @Override
    public void buyTicket(User user, Event event) {
        Repositories.getH2Repo().insertTicket(user.getUserId(),event.getId());
        Repositories.getH2Repo().updateAttendees(event.getId());
    }

    @Override
    public List<Ticket> getTickets(int userId) {
       return Repositories.getH2Repo().getTickets(userId);
    }//

    @Override
    public List<String> getInterestsFromUser(int userId) {
       return Repositories.getH2Repo().getInterestsFromUser(userId);
    }

    @Override
    public void addInterestToUser(User user, String interest) {
        Repositories.getH2Repo().addInterestToUser(user.getUserId(),interest);
    }

    @Override
    public void addFriendToUser(User sender, User receiver) {
        Repositories.getH2Repo().addFriendToUser(sender.getUserId(), receiver.getUserId());
    }

    @Override
    public void removeInterestFromUser(User user, String interest) {
        Repositories.getH2Repo().removeInterestFromUser(user.getUserId(),interest);
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
    public List<User> getFriendsOfUser(int userId) {
       return Repositories.getH2Repo().getFriendsOfUser(userId);
    }

    @Override
    public void removeInterestedUser(Event event, User user) {

    }

    @Override
    public void removeFriendFromUserFriends(User user, int friendId) {
        Repositories.getH2Repo().removeFriendFromUserFriends(user.getUserId(),friendId);
    }
}
