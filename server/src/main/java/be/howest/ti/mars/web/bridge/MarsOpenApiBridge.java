package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * In the MarsOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the controller is turned to Json or another Web data type in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getEvent");
        routerBuilder.operation("getEvent").handler(this::getEvent);

        LOGGER.log(Level.INFO, "Installing handler for: getEvents");
        routerBuilder.operation("getEvents").handler(this::getEvents);

        LOGGER.log(Level.INFO, "Installing handler for: getUsers");
        routerBuilder.operation("getUsers").handler(this::getUsers);

        LOGGER.log(Level.INFO, "Installing handler for: getUser");
        routerBuilder.operation("getUser").handler(this::getUser);

        LOGGER.log(Level.INFO, "Installing handler for: getInterests");
        routerBuilder.operation("getInterests").handler(this::getInterests);

        LOGGER.log(Level.INFO, "Installing handler for: buyTicket");
        routerBuilder.operation("buyTicket").handler(this::buyTicket);

        LOGGER.log(Level.INFO, "Installing handler for: getTickets");
        routerBuilder.operation("getTickets").handler(this::getTickets);

        LOGGER.log(Level.INFO, "Installing handler for: addInterestToUser");
        routerBuilder.operation("addInterest").handler(this::addInterestToUser);

        LOGGER.log(Level.INFO, "Installing handler for: addFriendToUser");
        routerBuilder.operation("addFriend").handler(this::addFriend);

        LOGGER.log(Level.INFO, "Installing handler for: getInterestsOfUser");
        routerBuilder.operation("getInterestsOfUser").handler(this::getInterestsOfUser);

        LOGGER.log(Level.INFO, "Installing handler for: getFriendsOfUser");
        routerBuilder.operation("getFriendsOfUser").handler(this::getFriendsOfUser);

        LOGGER.log(Level.INFO, "Installing handler for: removeInterestFromUser");
        routerBuilder.operation("removeInterestFromUser").handler(this::removeInterestFromUser);

        LOGGER.log(Level.INFO, "Installing handler for: addInterestedUser");
        routerBuilder.operation("addInterestedUser").handler(this::addInterestedUser);

        LOGGER.log(Level.INFO, "Installing handler for: removeInterestedUser");
        routerBuilder.operation("removeInterestedUser").handler(this::removeInterestedUser);

        LOGGER.log(Level.INFO, "Installing handler for: removeFriendFromUserFriends");
        routerBuilder.operation("removeFriendFromUserFriends").handler(this::removeFriendFromUserFriends);

        LOGGER.log(Level.INFO, "Installing handler for: createEvent");
        routerBuilder.operation("createEvent").handler(this::createEvent);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }


    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }

    public void getEvent(RoutingContext ctx) {
        Event event = controller.getEvent(Request.from(ctx).getEventId());

        Response.sendEvent(ctx, event);
    }

    public void getEvents(RoutingContext ctx) {
        Set<Event> events = controller.getEvents();

        Response.sendEvents(ctx, events);
    }

    public void getTickets(RoutingContext ctx) {
        int userId = controller.getUser(Request.from(ctx).getUserId()).getUserId();
        List<Ticket> tickets = controller.getTickets(userId);

        Response.sendTickets(ctx, tickets);
    }

    public void getInterests(RoutingContext ctx) {
        List<String> interests = controller.getInterests();

        Response.sendInterests(ctx, interests);
    }

    private void getUsers(RoutingContext ctx) {
        Set<User> users = controller.getUsers();

        if (!ctx.queryParam("firstName").isEmpty()) {
            filterByFirstname(ctx, ctx.queryParam("firstName").get(0), users);
        } else if (!ctx.queryParam("lastName").isEmpty()) {
            filterByLastName(ctx, ctx.queryParam("lastName").get(0), users);
        } else {
            Response.sendUsers(ctx, users);
        }
    }

    private void filterByFirstname(RoutingContext routingContext, String names, Set<User> users) {
        Set<User> filteredUsers = users.stream()
                .filter(user -> user.getFirstName().equals(names))
                .collect(Collectors.toSet());

        Response.sendUsers(routingContext, filteredUsers);
    }

    private void filterByLastName(RoutingContext routingContext, String names, Set<User> users) {
        Set<User> filteredUsers = users.stream()
                .filter(user -> user.getLastName().equals(names))
                .collect(Collectors.toSet());

        Response.sendUsers(routingContext, filteredUsers);
    }

    private void getUser(RoutingContext ctx) {
        User user = controller.getUser(Request.from(ctx).getUserId());
        Response.sendUser(ctx, user);
    }

    public void addInterestToUser(RoutingContext ctx) {
        String interest = Request.from(ctx).getInterest();
        User user = controller.getUser(Request.from(ctx).getUserId());
        controller.addInterestToUser(user, interest);
        Response.sendOkResponse(ctx);
    }

    private void addFriend(RoutingContext ctx) {
        User sender = controller.getUser(Request.from(ctx).getUserId());
        User receiver = controller.getUser(Request.from(ctx).getFriendId());
        controller.addFriendToUser(sender, receiver);
        Response.sendOkResponse(ctx);
    }

    public void removeInterestFromUser(RoutingContext ctx) {
        String interest = Request.from(ctx).getInterest();
        User user = controller.getUser(Request.from(ctx).getUserId());
        controller.removeInterestFromUser(user, interest);
        Response.sendOkResponse(ctx);
    }

    private void removeFriendFromUserFriends(RoutingContext routingContext) {
        int friendId = Request.from(routingContext).getFriendId();
        User user = controller.getUser(Request.from(routingContext).getUserId());
        controller.removeFriendFromUserFriends(user, friendId);
        Response.sendOkResponse(routingContext);
    }

    private void getInterestsOfUser(RoutingContext ctx) {
        List<String> interests = controller.getInterestsFromUser(Request.from(ctx).getUserId());
        Response.sendInterests(ctx, interests);
    }

    private void getFriendsOfUser(RoutingContext routingContext) {
        List<User> friends = controller.getFriendsOfUser(Request.from(routingContext).getUserId());
        Response.sendUserFriends(routingContext, friends);
    }

    public void buyTicket(RoutingContext ctx) {
        Event event = controller.getEvent(Request.from(ctx).getEventId());
        User user = controller.getUser(Request.from(ctx).getUserId());
        controller.buyTicket(user, event);
        Event updatedEvent = controller.getEvent(Request.from(ctx).getEventId());
        Response.sendEvent(ctx, updatedEvent);
    }

    public void addInterestedUser(RoutingContext ctx) {
        Event event = controller.getEvent(Request.from(ctx).getEventId());
        User user = controller.getUser(Request.from(ctx).getUserId());
        controller.addInterestedUser(event,user);
        Event updatedEvent = controller.getEvent(Request.from(ctx).getEventId());
        Response.sendEvent(ctx, updatedEvent);
    }

    public void removeInterestedUser(RoutingContext ctx) {
        Event event = controller.getEvent(Request.from(ctx).getEventId());
        User user = controller.getUser(Request.from(ctx).getUserId());
        controller.removeInterestedUser(event,user);
        Event updatedEvent = controller.getEvent(Request.from(ctx).getEventId());
        Response.sendEvent(ctx, updatedEvent);
    }

    public void createEvent(RoutingContext ctx) {
        Request request = Request.from(ctx);

        String name = request.getBodyValueString("title");
        String description = request.getBodyValueString("description");
        String location = request.getBodyValueString("location");
        Date startDate = request.getBodyValueDate("startDate");
        Date endDate = request.getBodyValueDate("endDate");
        Interest interest = Interest.valueOf(request.getBodyValueString("interest"));
        int ticketPrice = request.getBodyValueInteger("ticketPrice");
        EventExtraInformation eventExtraInformation = new EventExtraInformation(ticketPrice, 0, 0, location);
        Event newEvent = new Event(name, description, startDate, endDate, interest, eventExtraInformation);
        controller.createNewEvent(newEvent);
        Response.sendOkResponse(ctx);
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
