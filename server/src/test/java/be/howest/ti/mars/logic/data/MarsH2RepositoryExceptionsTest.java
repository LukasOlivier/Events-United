package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Event;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

class MarsH2RepositoryExceptionsTest {

    private static final String URL = "jdbc:h2:./db-02";

    @Test
    void getH2RepoWithNoDbFails() {
        // Arrange
        Repositories.shutdown();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, Repositories::getH2Repo);
    }

    @Test
    void functionsWithSQLExceptionFailsNicely() {
        // Arrange
        int id = 1;
        String interest = "cars";
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.shutdown();
        Repositories.configure(dbProperties);
        MarsH2Repository repo = Repositories.getH2Repo();
        Event event = repo.getEvent(id);
        repo.cleanUp();


        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, () -> repo.getEvent(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.createNewEvent(event));
        Assertions.assertThrows(RepositoryException.class, repo::getEvents);
        Assertions.assertThrows(RepositoryException.class, repo::getInterests);
        Assertions.assertThrows(RepositoryException.class, repo::getUsers);
        Assertions.assertThrows(RepositoryException.class, () -> repo.getUser(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getFavorites(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.insertTicket(id, id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateAttendees(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getTickets(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.addInterestToUser(id, interest));
        Assertions.assertThrows(RepositoryException.class, () -> repo.removeInterestFromUser(id, interest));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getFriendsOfUser(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.addInterestedUser(id, id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateInterested(id, id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.removeInterestedUser(id, id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.removeFriendFromUserFriends(id, id));
    }


}
