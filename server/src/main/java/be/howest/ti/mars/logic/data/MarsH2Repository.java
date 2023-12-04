package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start project there was no need for a Java interface MarsRepository.
Please always use interfaces when needed.

To make this class useful, please complete it with the topics seen in the module OOA & SD
 */

public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());

    private static final String CREATE_TICKET_FAILED = "Creating ticket failed, no rows affected.";
    private static final String UPDATE_INTEREST_FAILED = "Failed to update interested.";
    private static final String FAILED_UPDATING_INTERESTED = "Could not update interested.";
    private static final String FAILED_GETTING_INTEREST = "Failed to get interest.";

    private static final String DESCRIPTION = "description";
    private static final String INTEREST = "interest";
    private static final String USER_ID = "userId";
    private static final String HEIGHT = "height";
    private static final String GENDER = "gender";

    // SQL get
    private static final String SQL_EVENT_BY_ID = "select * from events where id = ?;";
    private static final String SQL_SELECT_ALL_EVENTS = "select * from events;";
    private static final String SQL_SELECT_ALL_INTERESTS = "select * from interests;";
    private static final String SQL_SELECT_ALL_USERS = "select * from users;";
    private static final String SQL_USER_BY_ID = "select * from users where userId = ?;";
    private static final String SQL_TICKETS_BY_USERID = "select * from tickets where userId = ?;";

    private static final String SQL_FAVORITES_BY_USERID = "select * from interestedusers where userId = ?;";

    private static final String SQL_INTEREST_BY_USER = "select * from userInterests where userId = ?;";
    private static final String SQL_FRIENDS_BY_USER = "select u2.* from users u join userfriends uf on u.userId = uf.userId join users u2 on u2.userId = uf.friendid where u.userid = ?";


    // SQL insert
    private static final String SQL_ADD_INTEREST_TO_USER = "insert into userInterests select ?, ? from dual where not exists(select * from userInterests where userId = ? and interest = ?);";

    private static final String SQL_ADD_TICKET = "insert into tickets select ?, ? from dual where not exists(select * from tickets where eventId = ? and userId = ?);";

    private static final String SQL_ADD_INTERESTED = "insert into interestedUsers select ?, ? from dual where not exists(select * from tickets where eventId = ? and userId = ?);";

    private static final String SQL_ADD_FRIEND_TO_USER = "insert into userFriends select ?, ? from dual where not exists(select * from userFriends where userId = ? and friendId = ?);";
    private static final String SQL_ADD_EVENT = "insert into events(name,description,startDate,endDate,interest,location,ticketPrice,attendees,interested) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";


    // SQL update
    private static final String SQL_UPDATE_ATTENDEES = "update events set attendees = attendees + 1 where id = ?;";
    private static final String SQL_UPDATE_INTERESTED_ADD = "update events set interested = interested + ? where id = ?;";



    // SQL delete
    private static final String SQL_REMOVE_INTEREST_FROM_USER = "delete from userInterests where userId = ? and interest = ?;";
    private static final String SQL_REMOVE_FRIEND_FROM_USER_FRIENDS = "delete from userFriends where userId = ? and friendId = ?;";
    private static final String SQL_REMOVE_INTERESTED = "delete from interestedUsers where eventId = ? and userId = ?;";
    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database web console started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public Event getEvent(int eventId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_EVENT_BY_ID)
        ) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EventExtraInformation eventExtraInformation = new EventExtraInformation(rs.getInt("ticketPrice"), rs.getInt("interested"), rs.getInt("attendees"), rs.getString("location"));
                    return new Event(rs.getInt("id"), rs.getString("name"), rs.getString(DESCRIPTION), rs.getDate("startDate"), rs.getDate("endDate"), Interest.valueOf(rs.getString(INTEREST)), eventExtraInformation);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get event.", ex);
            throw new RepositoryException("Could not get event.");
        }
    }

    public void createNewEvent(Event event) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ADD_EVENT)) {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, Util.convertToSqlDate(event.getStartDate()));
            stmt.setDate(4, Util.convertToSqlDate(event.getEndDate()));
            stmt.setString(5, event.getInterest().toString());
            stmt.setString(6, event.getLocation());
            stmt.setInt(7, event.getTicketPrice());
            stmt.setInt(8, 0);
            stmt.setInt(9, 0);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, UPDATE_INTEREST_FAILED, ex);
            throw new RepositoryException(FAILED_UPDATING_INTERESTED);
        }
    }

    public Set<Event> getEvents() {
        Set<Event> events = new HashSet<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_EVENTS)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EventExtraInformation eventExtraInformation = new EventExtraInformation(rs.getInt("ticketPrice"), rs.getInt("interested"), rs.getInt("attendees"), rs.getString("location"));
                    Event event = new Event(rs.getInt("id"), rs.getString("name"), rs.getString(DESCRIPTION), rs.getDate("startDate"), rs.getDate("endDate"), Interest.valueOf(rs.getString(INTEREST)), eventExtraInformation);
                    events.add(event);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get event.", ex);
            throw new RepositoryException("Could not get event.");
        }
        return events;
    }

    public List<String> getInterests() {
        List<String> interests = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_INTERESTS)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    interests.add(rs.getString(INTEREST));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_GETTING_INTEREST, ex);
            throw new RepositoryException("Could not get interest.");
        }
        return interests;
    }

    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_USERS);
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt(USER_ID);
                    User user = getUser(userId);
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get users.", ex);
            throw new RepositoryException("Could not get users.");
        }
        return users;
    }

    public User getUser(int userId) {
        List<String> userInterests = getInterestsFromUser(userId);
        List<User> userFriends = getFriendsOfUser(userId);
        List<Ticket> userTickets = getTickets(userId);
        List<Event> userFavorites = getFavorites(userId);

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_USER_BY_ID);
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    FullName fullName = new FullName(rs.getString("firstname"), rs.getString("lastname"));
                    UserExtraInformation userExtraInformation = new UserExtraInformation(userInterests, userFriends, userTickets, userFavorites);
                    return new User(rs.getInt(USER_ID), fullName, rs.getDate("birthdate"), rs.getInt(HEIGHT), rs.getString(GENDER), rs.getString(DESCRIPTION), userExtraInformation);
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user.", ex);
            throw new RepositoryException("Could not get user.");
        }
    }

    public List<Event> getFavorites(int userId){
        List<Event> userFavorites = new ArrayList<>();

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_FAVORITES_BY_USERID);
        ) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userFavorites.add(getEvent(rs.getInt("eventId")));
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get user.", ex);
            throw new RepositoryException("Could not get user.");
        }
        return userFavorites;
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();

        try {
            Files.deleteIfExists(Path.of("./db-02.mv.db"));
            Files.deleteIfExists(Path.of("./db-02.trace.db"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Database cleanup failed.", e);
            throw new RepositoryException("Database cleanup failed.");
        }
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql);
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void insertTicket(int userId, int eventId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ADD_TICKET, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.setInt(3, eventId);
            stmt.setInt(4, userId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(CREATE_TICKET_FAILED);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create ticket.", ex);
            throw new RepositoryException("Could not create ticket.");
        }
    }

    public void updateAttendees(int eventId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ATTENDEES)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update attendees.", ex);
            throw new RepositoryException("Could not update attendees.");
        }
    }

    public List<Ticket> getTickets(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_TICKETS_BY_USERID)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(new Ticket(getEvent(rs.getInt("eventId")), userId));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get tickets.", ex);
            throw new RepositoryException("Could not get tickets.");
        }
        return tickets;
    }

    public void addInterestToUser(int userId, String interest) {
        interest = interest.toLowerCase();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ADD_INTEREST_TO_USER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, interest);
            stmt.setInt(3, userId);
            stmt.setString(4, interest);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating userInterest failed, no rows affected.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create userInterest.", ex);
            throw new RepositoryException("Could not create userInterest.");
        }
    }

    public void addFriendToUser(int userId, int friendId) {
        if (userId != friendId) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_ADD_FRIEND_TO_USER, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, userId);
                stmt.setInt(2, friendId);
                stmt.setInt(3, userId);
                stmt.setInt(4, friendId);

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("adding friend failed, no rows affected.");
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Failed to add friend.", ex);
                throw new RepositoryException("User is already friends with you.");
            }
        } else {
            throw new IllegalArgumentException("Can not add yourself as friend!");
        }
    }

    public void removeInterestFromUser(int userId, String interest) {
        interest = interest.toLowerCase();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_REMOVE_INTEREST_FROM_USER)) {
            stmt.setInt(1, userId);
            stmt.setString(2, interest);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error, no rows affected.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete userInterest.", ex);
            throw new RepositoryException("Could not delete userInterest.");
        }
    }

    public List<String> getInterestsFromUser(int userId) {
        List<String> interests = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INTEREST_BY_USER)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    interests.add(rs.getString(INTEREST));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, FAILED_GETTING_INTEREST, ex);
            LOGGER.log(Level.SEVERE, FAILED_GETTING_INTEREST, ex);
            throw new RepositoryException("Could not get interest.");
        }
        return interests;
    }

    public List<User> getFriendsOfUser(int userId) {
        List<User> friendsList = new ArrayList<>();
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_FRIENDS_BY_USER)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FullName fullName = new FullName(rs.getString("firstname"), rs.getString("lastname"));
                    List<String> friendInterests = getInterestsFromUser(rs.getInt(USER_ID));
                    friendsList.add(new User(rs.getInt(USER_ID), fullName, rs.getDate("birthdate"), rs.getInt(HEIGHT), rs.getString(GENDER), rs.getString(DESCRIPTION), friendInterests));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get friends.", ex);
            throw new RepositoryException("Could not get friends.");
        }
        return friendsList;
    }

    public void addInterestedUser(int eventId, int userId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ADD_INTERESTED)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.setInt(3, eventId);
            stmt.setInt(4, userId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(CREATE_TICKET_FAILED);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, UPDATE_INTEREST_FAILED, ex);
            throw new RepositoryException(FAILED_UPDATING_INTERESTED);
        }
    }

    public void updateInterested(int eventId, int amount) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_INTERESTED_ADD)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, UPDATE_INTEREST_FAILED, ex);
            throw new RepositoryException(FAILED_UPDATING_INTERESTED);
        }
    }

    public void removeInterestedUser(int eventId, int userId) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_REMOVE_INTERESTED)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(CREATE_TICKET_FAILED);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, UPDATE_INTEREST_FAILED, ex);
            throw new RepositoryException(FAILED_UPDATING_INTERESTED);
        }
    }

    public void removeFriendFromUserFriends(int userId, int friendId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_REMOVE_FRIEND_FROM_USER_FRIENDS)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Removing a failed, no rows affected.");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete friend.", ex);
            throw new RepositoryException("Could not delete friend.");
        }
    }
}
