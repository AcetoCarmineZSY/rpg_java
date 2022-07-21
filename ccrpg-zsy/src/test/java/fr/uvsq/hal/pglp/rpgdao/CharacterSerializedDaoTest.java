package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Character;
import fr.uvsq.hal.pglp.rpg.Ability;
import static fr.uvsq.hal.pglp.rpg.Ability.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * La classe <code>CharacterSerializedDAOTest</code> ...
 *
 * @author hal
 * @version 2022
 */
public class CharacterSerializedDaoTest {
  private static final String DIRECTORY_PREFIX = "dao_tests";
  private static Path tmpDirectory;

  private Character alice;

  @BeforeAll
  public static void beforeAll() throws IOException {
    tmpDirectory = Files.createTempDirectory(DIRECTORY_PREFIX);
  }

  @BeforeEach
  public void setup() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    alice = new Character.CharacterBuilder("Alice", preAbility).nonRandomAbilities().build();
  }

  @Test
  public void createTest() {
    CharacterSerializedDao CharacterDAO = new CharacterSerializedDao(tmpDirectory);
    CharacterDAO.create(alice);
    assertEquals(Optional.of(alice), CharacterDAO.read("Alice"));
  }

  @Test
  public void updateTest() {
    Ability[] preAbility2 = new Ability[]{Dexterity,Charisma,Constitution,Wisdom,Intelligence,Strength};
    Character alice2 = new Character.CharacterBuilder("Alice",preAbility2).build();
    CharacterSerializedDao CharacterDAO = new CharacterSerializedDao(tmpDirectory);
    CharacterDAO.create(alice);
    CharacterDAO.update(alice2);
    assertEquals(Optional.of(alice2), CharacterDAO.read("Alice"));
  }

  @Test
  public void deleteTest() {
    CharacterSerializedDao CharacterDAO = new CharacterSerializedDao(tmpDirectory);
    CharacterDAO.create(alice);
    CharacterDAO.delete(alice);
    assertTrue(CharacterDAO.read("Alice").isEmpty());
  }
}
