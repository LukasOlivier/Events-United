package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.*;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:./db-02";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:./db-02",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getTicketsTest() throws ParseException {
        // Arrange
        User mockUser = createMockUser();
        Event mockEvent = Repositories.getH2Repo().getEvent(1);
        MarsController mockController = new MockMarsController();

        // Act
        mockController.buyTicket(mockUser, mockEvent);

        // Assert
        assertEquals(1, mockController.getTickets(mockUser.getUserId()).size());
    }

    @Test
    void getEventTest() {
        // Arrange
        MarsController mockController = new MockMarsController();
        // Act
        Event test = mockController.getEvent(1);

        // Assert
        assertEquals(1, test.getId());
    }

    @Test
    void getUserTest() {
        // Arrange
        MarsController mockController = new MockMarsController();

        // Act
        User user = mockController.getUser(1);

        // Assert
        assertEquals(1, user.getUserId());
    }

    @Test
    void getInterestsFromUserTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();

        // Act
        mockController.addInterestToUser(mockUser, "Car");

        // Assert
        assertEquals(1, mockController.getInterestsFromUser(mockUser.getUserId()).size());

        // Act
        mockController.removeInterestFromUser(mockUser, "Car");

        // Assert
        assertEquals(0, mockController.getFriendsOfUser(mockUser.getUserId()).size());
    }

    @Test
    void getFriendsOfUserTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();
        User mockFriend = createMockFriend();

        // Act
        mockController.addFriendToUser(mockUser, mockFriend);

        // Assert
        assertEquals(1, mockController.getFriendsOfUser(mockUser.getUserId()).size());

        // Act
        mockController.removeFriendFromUserFriends(mockUser, 2);

        // Assert
        assertEquals(0, mockController.getFriendsOfUser(mockUser.getUserId()).size());
    }


    @Test
    void getEventsTest() {
        // Arrange
        MarsController mockController = new MockMarsController();

        // Act
        Set<Event> testEvents = mockController.getEvents();

        // Assert
        assertEquals(2, testEvents.size());
    }

    @Test
    void getInterestsTest() {
        // Arrange
        MarsController mockController = new MockMarsController();

        // Act
        List<String> testInterests = mockController.getInterests();

        // Assert
        assertEquals(1, testInterests.size());
    }

    @Test
    void getUsersTest() {
        // Arrange
        MarsController mockController = new MockMarsController();

        // Act
        Set<User> testUsers = mockController.getUsers();

        // Assert
        assertEquals(2, testUsers.size());
    }

    @Test
    void createEventTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        Event mockEvent = Repositories.getH2Repo().getEvent(1);

        // Act
        mockController.createNewEvent(mockEvent);

        // Assert
        assertEquals(3,Repositories.getH2Repo().getEvents().size());
    }

    @Test
    void buyTicketTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();
        Event mockEvent = Repositories.getH2Repo().getEvent(1);

        // Act
        mockController.buyTicket(mockUser, mockEvent);

        // Assert
        assertEquals(
                1, mockController.getTickets(mockUser.getUserId()).size()
        );
    }

    @Test
    void addInterestToUserTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();

        // Act
        mockController.addInterestToUser(mockUser, "CARS");
        mockController.addInterestToUser(mockUser, "FOOD");

        // Assert
        assertEquals(
                2, mockController.getInterestsFromUser(mockUser.getUserId()).size()
        );
    }

    @Test
    void removeInterestFromUser() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();

        // Act
        mockController.addInterestToUser(mockUser, "CARS");
        mockController.addInterestToUser(mockUser, "FOOD");

        mockController.removeInterestFromUser(mockUser, "CARS");

        // Assert
        assertEquals(
                1, mockController.getInterestsFromUser(mockUser.getUserId()).size()
        );
    }

    @Test
    void addFriendToUseTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();
        User mockFriend = createMockFriend();

        // Act
        mockController.addFriendToUser(mockUser, mockFriend);

        // Assert
        assertEquals(
                1, mockController.getFriendsOfUser(mockUser.getUserId()).size()
        );
    }

    @Test
    void removeFriendFromUserTest() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();
        User mockFriend = createMockFriend();

        // Act
        mockController.addFriendToUser(mockUser, mockFriend);
        mockController.removeFriendFromUserFriends(mockUser, mockFriend.getUserId());

        // Assert
        assertEquals(
                0, mockController.getFriendsOfUser(mockUser.getUserId()).size()
        );
    }

    @Test
    void addInterestedUser() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();
        Event mockEvent = Repositories.getH2Repo().getEvent(1);

        // Act
        mockController.addInterestedUser(mockEvent, mockUser);

        // Assert
        assertEquals(
                1, mockController.getEvent(1).getInterested()
        );
    }

    @Test
    void removeInterestedUser() throws ParseException {
        // Arrange
        MarsController mockController = new MockMarsController();
        User mockUser = createMockUser();
        Event mockEvent = Repositories.getH2Repo().getEvent(2);

        // Act
        mockController.addInterestedUser(mockEvent, mockUser);
        mockController.removeInterestedUser(mockEvent, mockUser);

        // Assert
        assertEquals(
                0, mockController.getEvent(1).getInterested()
        );
    }

    private User createMockUser() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date birthdate = formatter.parse("18-03-2003");
        FullName fullName = new FullName("niels", "soete");
        return new User(1, fullName, birthdate, 180, "male", "developer");
    }

    private User createMockFriend() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date birthdate = formatter.parse("18-03-2003");
        FullName fullName = new FullName("lukas", "olivier");
        return new User(2, fullName, birthdate, 180, "male", "developer");
    }

    private Event createMockEvent() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date startDate = formatter.parse("18-03-2030");
        Date endDate = formatter.parse("18-03-2031");
        EventExtraInformation eventExtraInformation = new EventExtraInformation(20, 0, 0, "location");
        return new Event("name", "description", startDate, endDate, Interest.CARS, eventExtraInformation);
    }

}