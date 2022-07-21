package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Character;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public abstract class DaoFactory {
    public enum DaoType { SERIALIZED, JDBC; }

    private static final String DIRECTORY_PREFIX = "dao_tests";
    private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";

    public abstract Dao<Character> getCharacterDao();

    public static DaoFactory getDaoFactory(DaoType daoType) throws IOException, SQLException {
        Objects.requireNonNull(daoType);
        if (daoType == DaoType.SERIALIZED) {
        Path tmpDirectory = Files.createTempDirectory(DIRECTORY_PREFIX);

        return new SerializedDaoFactory(tmpDirectory);
        }
        if (daoType == DaoType.JDBC) {
        Connection connection = DriverManager.getConnection(DB_URL);
        return new JdbcDaoFactory(connection);
        }
        return null;
    }
}
