package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.exceptions.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoriesTest {

    @BeforeEach
    void setupTest() {
        Repositories.shutdown();
    }

    @Test
    void getH2RepoWithoutConfiguration() {
        assertThrows(RepositoryException.class, Repositories::getH2Repo);
    }
}
