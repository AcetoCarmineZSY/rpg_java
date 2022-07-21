package fr.uvsq.hal.pglp.rpg;

import org.junit.Test;

import static org.junit.Assert.*;

import static fr.uvsq.hal.pglp.rpg.Ability.Charisma;
import static fr.uvsq.hal.pglp.rpg.Ability.Constitution;
import static fr.uvsq.hal.pglp.rpg.Ability.Dexterity;
import static fr.uvsq.hal.pglp.rpg.Ability.Intelligence;
import static fr.uvsq.hal.pglp.rpg.Ability.Strength;
import static fr.uvsq.hal.pglp.rpg.Ability.Wisdom;
import static fr.uvsq.hal.pglp.rpg.Skill.Arcana;

public class CharacterTest {
  @Test
  public void CharacterWithNonRandomAbility_abilityCheckTest() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character character = new Character.CharacterBuilder("Alice", preAbility).nonRandomAbilities().build();
    assertTrue(character.abilityCheck(Charisma,3));
    assertTrue(character.abilityCheck(Dexterity,2));
    assertTrue(character.abilityCheck(Strength,1));
    assertTrue(character.abilityCheck(Wisdom,0));
  }

  @Test
  public void CharacterWithWithSkill_skillCheckTest() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character character = new Character.CharacterBuilder("Tom", preAbility)
                                       .nonRandomAbilities()
                                       .setProficiencyBonus(Intelligence, 2).build();
    assertTrue(character.skillCheck(Arcana, 4));
  }
}
