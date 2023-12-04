package be.howest.ti.mars.web.bridge;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {
    public static final String SPEC_EVENT_ID = "eventId";

    public static final String SPEC_USER_ID = "userId";

    public static final String SPEC_INTEREST_NAME = "interest";

    public static final String SPEC_FRIEND_ID = "friendId";

    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }
    public int getEventId() {
        return params.pathParameter(SPEC_EVENT_ID).getInteger();
    }

    public int getUserId() {
        return params.pathParameter(SPEC_USER_ID).getInteger();
    }

    public String getInterest() {
        return params.pathParameter(SPEC_INTEREST_NAME).getString();
    }

    public int getFriendId() {
        return params.pathParameter(SPEC_FRIEND_ID).getInteger();
    }
    public int getBodyValueInteger(String key) {
        return params.body().getJsonObject().getInteger(key);
    }
    public String getBodyValueString(String key) {
        return params.body().getJsonObject().getString(key);
    }

//    https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public Date getBodyValueDate(String key){
        LocalDate localDate = LocalDate.parse(params.body().getJsonObject().getString(key));
        ZoneId systemTimeZone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(systemTimeZone);
        return Date.from(zonedDateTime.toInstant());
    }
}
