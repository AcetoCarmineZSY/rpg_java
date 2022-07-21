package fr.uvsq.hal.pglp.rpg;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;
import static fr.uvsq.hal.pglp.rpg.Ability.Charisma;
import static fr.uvsq.hal.pglp.rpg.Ability.Constitution;
import static fr.uvsq.hal.pglp.rpg.Ability.Dexterity;
import static fr.uvsq.hal.pglp.rpg.Ability.Intelligence;
import static fr.uvsq.hal.pglp.rpg.Ability.Strength;
import static fr.uvsq.hal.pglp.rpg.Ability.Wisdom;

public class TeamTest {
  private Character Alice;
  private Character Bob;
  private Team team;

  @Before
  public void setup() {
    Ability[] preAbilityAlice = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Ability[] preAbilityBob = new Ability[]{Dexterity,Constitution,Charisma,Wisdom,Strength,Intelligence};
    Alice = new Character.CharacterBuilder("Alice", preAbilityAlice).build();
    Bob = new Character.CharacterBuilder("Bob", preAbilityBob).build();
    team = new Team();
    team.add(Alice);
    team.add(Bob);
  }

  @Test
  public void getSizedUnGroupe() {
    Team team = new Team();
    team.add(Alice);
    team.add(Bob);
    assertEquals(2, team.getSize());
  }

  @Test
  public void getSizedUnGroupeContientDesCharactersEtDesGroupes() {
    Team team1 = new Team();
    Team team2 = new Team();
    team1.add(Alice);
    team2.add(Bob);
    team2.add(Alice);
    team1.add(team2);
    assertEquals(3, team1.getSize());
  }
  
  @Test
  public void unGroupeContientDesCharacters() {
    assertTrue(team.contains(Alice));
    assertTrue(team.contains(Bob));
  }

  @Test
  public void unGroupeContientDesCharactersEtDesGroupes() {
    Team team1 = new Team();
    team.add(team1);
    assertTrue(team.contains(team1));
  }

  @Test
  public void containsFaitUneRechercheRecursive() {
    Team team1 = new Team();
    team1.add(Alice);
    Team team2 = new Team();
    team2.add(Bob);
    team1.add(team2);
    assertTrue(team1.contains(Bob));
  }

  @Test
  public void unGroupeNeSeContientPasLuiMemeDirectement() {
    team.add(team);
    assertFalse(team.contains(team));
  }

  @Test
  public void unGroupeNeSeContientPasLuiMemeMemeIndirectement() {
    Team team1 = new Team();
    team1.add(Alice);
    Team team2 = new Team();
    team2.add(Bob);
    team2.add(team1);
    assertFalse(team1.contains(team1));
  }

  @Test
  public void unGroupePeutEtreParcourus() {
    final List<OrganizationElement> expecteCharacters = List.of(Alice, Bob);
    List<OrganizationElement> visiteCharacters = new ArrayList<>();
    for (OrganizationElement element : team) {
      visiteCharacters.add(element);
    }
    assertEquals(expecteCharacters, visiteCharacters);
  }

  @Test
  public void unGroupeImbriquePeutEtreParcourus() {
    final List<OrganizationElement> expecteCharacters = List.of(Alice, Bob);
    List<OrganizationElement> visiteCharacters = new ArrayList<>();
    Team team1 = new Team();
    team1.add(Alice);
    Team team2 = new Team();
    team2.add(Bob);
    team1.add(team2);
    for (OrganizationElement element : team1) {
      visiteCharacters.add(element);
    }
    assertEquals(expecteCharacters, visiteCharacters);
  }

  @Test
  public void unAutreGroupeImbriquePeutEtreParcourus() {
    final List<OrganizationElement> expecteCharacters = List.of(Alice, Bob, Alice, Bob, Alice, Bob, Bob, Alice, Bob, Alice);
    List<OrganizationElement> visiteCharacters = new ArrayList<>();
    Team team1 = new Team();
    team1.add(Alice);
    Team team2 = new Team();
    team2.add(Bob);
    Team team3 = new Team();
    team3.add(Alice); team3.add(Bob);
    team2.add(team3);
    team2.add(Alice);
    team1.add(team2);
    team1.add(Bob);
    team1.add(team2);
    for (OrganizationElement element : team1) {
      visiteCharacters.add(element);
    }
    assertEquals(expecteCharacters, visiteCharacters);
  }
}
