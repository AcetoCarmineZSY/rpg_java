package fr.uvsq.hal.pglp.rpgdao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcTest {
  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
  private static Connection connection;

  @BeforeAll
  public static void setupBeforeAll() throws SQLException {
    connection = DriverManager.getConnection(DB_URL);
    Statement statement = connection.createStatement();
    statement.execute("CREATE TABLE characters(Name VARCHAR(25) NOT NULL PRIMARY KEY,Ability1 VARCHAR(20) NOT NULL,Ability2 VARCHAR(20) NOT NULL,Ability3 VARCHAR(20) NOT NULL,Ability4 VARCHAR(20) NOT NULL,Ability5 VARCHAR(20) NOT NULL,Ability6 VARCHAR(20) NOT NULL)"
    );
  }

  @BeforeEach
  public void setup() throws SQLException {
    Statement statement = connection.createStatement();
    statement.execute("TRUNCATE TABLE characters");
    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO characters VALUES(?,?,?,?,?,?,?)");
    psInsert.setString(1, "Alice");
    psInsert.setString(2, "Charisma");
    psInsert.setString(3, "Constitution");
    psInsert.setString(4, "Dexterity");
    psInsert.setString(5, "Intelligence");
    psInsert.setString(6, "Strength");
    psInsert.setString(7, "Wisdom");
    psInsert.executeUpdate();

    psInsert.setString(1, "Bob");
    psInsert.setString(2, "Dexterity");
    psInsert.setString(3, "Wisdom");
    psInsert.setString(4, "Charisma");
    psInsert.setString(5, "Strength");
    psInsert.setString(6, "Intelligence");
    psInsert.setString(7, "Constitution");
    psInsert.executeUpdate();
  }

  @Test
  public void selectTest() throws SQLException {
    assertDbState(List.of("Alice", "Bob"));
  }

  @Test
  public void insertTest() throws SQLException {
    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO characters VALUES(?,?,?,?,?,?,?)");
    psInsert.setString(1, "Charlie");
    psInsert.setString(2, "Dexterity");
    psInsert.setString(3, "Intelligence");
    psInsert.setString(4, "Charisma");
    psInsert.setString(5, "Strength");
    psInsert.setString(6, "Wisdom");
    psInsert.setString(7, "Constitution");
    int nbAffectedRows = psInsert.executeUpdate();
    assertEquals(1, nbAffectedRows);

    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("SELECT Name FROM characters WHERE Name = 'Charlie'");
    rs.next();
    assertEquals("Charlie", rs.getString(1));
  }

  @Test
  public void updateTest() throws SQLException {
    PreparedStatement psUpdate = connection.prepareStatement("UPDATE characters SET Ability1 = ?, Ability2 = ? WHERE Name = ?");
    psUpdate.setString(1, "Wisdom");
    psUpdate.setString(2, "Dexterity"); 
    psUpdate.setString(3, "Bob");
    int nbAffectedRows = psUpdate.executeUpdate();
    assertEquals(1, nbAffectedRows);

    assertDbState(List.of("Alice", "Bob"));
  }

  @Test
  public void deleteTest() throws SQLException {
    PreparedStatement psDelete = connection.prepareStatement("DELETE FROM characters WHERE Name = ?");
    psDelete.setString(1, "Bob");
    int nbAffectedRows = psDelete.executeUpdate();
    assertEquals(1, nbAffectedRows);

    assertDbState(List.of("Alice"));
  }

  private static void assertDbState(List<String> expectedPersons) throws SQLException {
    List<String> persons = new ArrayList<>();
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("SELECT Name FROM characters ORDER BY name");
    while (rs.next()) {
      persons.add(rs.getString(1));
    }
    assertEquals(expectedPersons, persons);
  }  
}
