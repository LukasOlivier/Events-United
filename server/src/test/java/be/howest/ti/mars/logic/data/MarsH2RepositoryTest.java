package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Event;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-02";

    @BeforeEach
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties);
    }
    @Test
    void getEvent() {
        // Arrange
        int id = 1;

        // Act
        Event testEvent = Repositories.getH2Repo().getEvent(id);

        // Assert
        Assertions.assertEquals(1, testEvent.getId());
    }
    @Test
    void getEvents() {
        // Act
        Set<Event> testEvents = Repositories.getH2Repo().getEvents();

        // Assert
        Assertions.assertTrue(testEvents.size() > 0);
    }
}
