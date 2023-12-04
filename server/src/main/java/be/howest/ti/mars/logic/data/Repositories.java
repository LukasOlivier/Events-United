package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;

public class Repositories {
    private static MarsH2Repository h2Repo = null;

    private Repositories() {
    }

    public static MarsH2Repository getH2Repo() {
        if (h2Repo == null)
            throw new RepositoryException("MarsH2Repository not configured.");

        return h2Repo;
    }

    public static void configure(JsonObject dbProps) {
        h2Repo = new MarsH2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
    }

    public static void shutdown() {
        if (h2Repo != null)
            h2Repo.cleanUp();

        h2Repo = null;
    }
}
