package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Event;
import be.howest.ti.mars.logic.domain.Ticket;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Set;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }

    public static void sendEvent(RoutingContext ctx, Event event) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(event));
    }

    static void sendOkResponse(RoutingContext ctx) {
        sendJsonResponse(ctx, 200, new JsonObject().put("message", "ok"));
    }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendEvents(RoutingContext ctx, Set<Event> events) {

        sendJsonResponse(ctx, 200, events);
    }

    public static void sendUsers(RoutingContext ctx, Set<User> users) {
        sendJsonResponse(ctx, 200, users);
    }

    public static void sendUser(RoutingContext ctx, User user) {
        sendJsonResponse(ctx, 200, user);
    }

    public static void sendInterests(RoutingContext ctx, List<String> interests) {
        sendJsonResponse(ctx, 200, interests);
    }

    public static void sendTickets(RoutingContext ctx, List<Ticket> tickets) {
        sendJsonResponse(ctx, 200, tickets);
    }

    public static void sendUserFriends(RoutingContext routingContext, List<User> friends) {
        sendJsonResponse(routingContext, 200, friends);
    }
}
