package be.howest.ti.mars.web;

import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.web.bridge.MarsOpenApiBridge;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAPITest {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    private Vertx vertx;
    private WebClient webClient;

    @BeforeAll
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new MarsOpenApiBridge(new MockMarsController()), new MarsRtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterAll
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }

    @Test
    void getAllEvents(final VertxTestContext testContext) {
        int firstObjectInArray = 0;
        webClient.get(PORT, HOST, "/api/events").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonArray().getString(firstObjectInArray))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getEventWithId(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/events/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name"))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void createEvent(final VertxTestContext testContext) {
        JsonObject testEvent = createTestEvent();
        webClient.post(PORT, HOST, "/api/events/create").sendJsonObject(testEvent)
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void buyTicket(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/1/ticket/buy").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name"))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void userIsInterestedInEvent(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/2/interested/add").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name"))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void userRemovesInterestedFromEvent(final VertxTestContext testContext) {
        webClient.delete(PORT, HOST, "/api/users/1/1/interested/remove").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name"))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getTicketsOfUser(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/2/tickets").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonArray().isEmpty()
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUsers(final VertxTestContext testContext) {
        int firstObjectInArray = 0;
        webClient.get(PORT, HOST, "/api/users").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonArray().getString(firstObjectInArray))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUserWithId(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("firstName"))
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getInterestOfUser(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/interests").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonArray().isEmpty()
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void addInterestToUser(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/sports/interests/add").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void removeInterestsFromUser(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/sports/interests/add").send();
        webClient.delete(PORT, HOST, "/api/users/1/sports/interests/remove").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getFriendsOfUser(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users/1/friends").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonArray().isEmpty()
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void addFriendToUser(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/2/friends/add").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void removeFriendFromUser(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/users/1/2/friends/add").send();
        webClient.delete(PORT, HOST, "/api/users/1/2/friends/remove").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getAllInterest(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/interests").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonArray().getString(0))
                    );
                    testContext.completeNow();
                }));
    }

    private JsonObject createTestEvent() {
        return new JsonObject()
                .put("title", "title")
                .put("description", "description")
                .put("startDate", "2028-12-15")
                .put("endDate", "2029-12-15")
                .put("location", "location")
                .put("interest", "CARS")
                .put("ticketPrice", 20);
    }

}