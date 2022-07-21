package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Character;
import fr.uvsq.hal.pglp.rpg.Ability;
import static fr.uvsq.hal.pglp.rpg.Ability.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * La classe <code>CharacterJdbcDaoTest</code> ...
 *
 * @author hal
 * @version 2022
 */
public class CharacterJdbcDaoTest {
  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
  private static Connection connection;

  private Character bob;

  @BeforeAll
  public static void beforeAll() throws SQLException {
    connection = DriverManager.getConnection(DB_URL);
    Statement statement = connection.createStatement();
    statement.execute("CREATE TABLE characters(Name VARCHAR(25) NOT NULL PRIMARY KEY,Ability1 VARCHAR(20) NOT NULL,Ability2 VARCHAR(20) NOT NULL,Ability3 VARCHAR(20) NOT NULL,Ability4 VARCHAR(20) NOT NULL,Ability5 VARCHAR(20) NOT NULL,Ability6 VARCHAR(20) NOT NULL)");
    statement.execute("CREATE TABLE abilityValues(NameCharacter VARCHAR(25) NOT NULL REFERENCES characters(Name),ValueAbility1 INTEGER NOT NULL,ValueAbility2 INTEGER NOT NULL,ValueAbility3 INTEGER NOT NULL,ValueAbility4 INTEGER NOT NULL,ValueAbility5 INTEGER NOT NULL,ValueAbility6 INTEGER NOT NULL)");
  }

  @BeforeEach
  public void setup() throws SQLException {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    bob = new Character.CharacterBuilder("Bob", preAbility).build();
    Statement statement = connection.createStatement();
    statement.execute("DELETE FROM abilityValues");
    statement.execute("DELETE FROM characters");
  }

  @Test
  public void createTest() {
    Dao<Character> CharacterDAO = new CharacterJdbcDao(connection);
    assertTrue(CharacterDAO.create(bob));
    assertEquals(Optional.of(bob), CharacterDAO.read("Bob"));
  }

  @Test
  public void updateTest() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character charlie = new Character.CharacterBuilder("Charlie", preAbility).nonRandomAbilities().build();
    Dao<Character> CharacterDAO = new CharacterJdbcDao(connection);
    assertTrue(CharacterDAO.create(bob));
    assertTrue(CharacterDAO.update(charlie));
    assertEquals(Optional.of(charlie), CharacterDAO.read("Charlie"));
  }

  @Test
  public void deleteTest() {
    Dao<Character> CharacterDAO = new CharacterJdbcDao(connection);
    assertTrue(CharacterDAO.create(bob));
    CharacterDAO.delete(bob);
    assertTrue(CharacterDAO.read("Bob").isEmpty());
  }
}
