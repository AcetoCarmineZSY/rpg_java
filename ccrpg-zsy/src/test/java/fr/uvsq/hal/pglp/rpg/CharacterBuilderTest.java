package fr.uvsq.hal.pglp.rpg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static fr.uvsq.hal.pglp.rpg.Ability.Charisma;
import static fr.uvsq.hal.pglp.rpg.Ability.Constitution;
import static fr.uvsq.hal.pglp.rpg.Ability.Dexterity;
import static fr.uvsq.hal.pglp.rpg.Ability.Intelligence;
import static fr.uvsq.hal.pglp.rpg.Ability.Strength;
import static fr.uvsq.hal.pglp.rpg.Ability.Wisdom;
import static fr.uvsq.hal.pglp.rpg.Skill.Arcana;

public class CharacterBuilderTest {
  @Test
  public void CharacterWithPreAbility() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character character = new Character.CharacterBuilder("Bob", preAbility).build();
    assertEquals("Bob", character.getname());

    //Détermination de l'intervalle de la somme des ability
    int sum=0;
    for (Ability a : preAbility){
      sum += character.getScore(a);
    }
    assertTrue(60 <= sum || sum <= 80);
  }

  @Test
  public void CharacterWithNonRandomAbility() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character character = new Character.CharacterBuilder("Charlie", preAbility).nonRandomAbilities().build();
    assertEquals("Charlie", character.getname());
    assertEquals(15, character.getScore(Charisma));
    assertEquals(14, character.getScore(Constitution));
    assertEquals(13, character.getScore(Dexterity));
    assertEquals(12, character.getScore(Intelligence));
    assertEquals(10, character.getScore(Strength));
    assertEquals(8, character.getScore(Wisdom));
    assertEquals(2, character.getModifier(Charisma));
    assertEquals(2, character.getModifier(Constitution));
    assertEquals(1, character.getModifier(Dexterity));
    assertEquals(1, character.getModifier(Intelligence));
    assertEquals(0, character.getModifier(Strength));
    assertEquals(-1, character.getModifier(Wisdom));
  }

  @Test
  public void CharacterWithSkill() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character character = new Character.CharacterBuilder("David", preAbility)
                                       .nonRandomAbilities()
                                       .setProficiencyBonus(Intelligence, 2).build();
    assertEquals(3, character.getSkillProficiency(Arcana));
  }

  @Test
  public void CharacterWithAbilitySet() {
    Ability[] preAbility = new Ability[]{Charisma,Constitution,Dexterity,Intelligence,Strength,Wisdom};
    Character character = new Character.CharacterBuilder("Eva", preAbility)
                                       .nonRandomAbilities()
                                       .setAbilityScore(Wisdom, 2)
                                       .build();
    assertEquals(2, character.getScore(Wisdom));
  }

}
