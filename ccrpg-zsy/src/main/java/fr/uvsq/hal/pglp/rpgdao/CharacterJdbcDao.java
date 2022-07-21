package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Ability;
import fr.uvsq.hal.pglp.rpg.Character;

import java.sql.*;
import java.util.Optional;

/**
 * La classe <code>CharacterJdbcDao</code> est un DAO pour les personnages.
 *
 * @author hal
 * @version 2022
 */
public class CharacterJdbcDao implements Dao<Character>{
    private Connection connection;

  public CharacterJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public boolean create(Character objet) {
    try {
        PreparedStatement psInsert = connection.prepareStatement("INSERT INTO characters VALUES(?, ?, ?, ?, ?, ?, ?)");
        psInsert.setString(1, objet.getname());
        psInsert.setString(2, objet.getAbility1().name());
        psInsert.setString(3, objet.getAbility2().name());
        psInsert.setString(4, objet.getAbility3().name());
        psInsert.setString(5, objet.getAbility4().name());
        psInsert.setString(6, objet.getAbility5().name());
        psInsert.setString(7, objet.getAbility6().name());
        psInsert.executeUpdate();

        psInsert = connection.prepareStatement("INSERT INTO abilityValues VALUES(?, ?, ?, ?, ?, ?, ?)");
        psInsert.setString(1, objet.getname());
        psInsert.setInt(2, objet.getScore(objet.getAbility1()));
        psInsert.setInt(3, objet.getScore(objet.getAbility2()));
        psInsert.setInt(4, objet.getScore(objet.getAbility3()));
        psInsert.setInt(5, objet.getScore(objet.getAbility4()));
        psInsert.setInt(6, objet.getScore(objet.getAbility5()));
        psInsert.setInt(7, objet.getScore(objet.getAbility6()));
        psInsert.executeUpdate();

        psInsert = connection.prepareStatement("INSERT INTO modificateurs VALUES(?, ?, ?, ?, ?, ?, ?)");
        psInsert.setString(1, objet.getname());
        psInsert.setInt(2, objet.getModifier(objet.getAbility1()));
        psInsert.setInt(3, objet.getModifier(objet.getAbility2()));
        psInsert.setInt(4, objet.getModifier(objet.getAbility3()));
        psInsert.setInt(5, objet.getModifier(objet.getAbility4()));
        psInsert.setInt(6, objet.getModifier(objet.getAbility5()));
        psInsert.setInt(7, objet.getModifier(objet.getAbility6()));
        psInsert.executeUpdate();

        psInsert = connection.prepareStatement("INSERT INTO proficiencyBonus VALUES(?, ?, ?, ?, ?, ?, ?)");
        psInsert.setString(1, objet.getname());
        psInsert.setInt(2, objet.getProficiencyBonus(objet.getAbility1()));
        psInsert.setInt(3, objet.getProficiencyBonus(objet.getAbility2()));
        psInsert.setInt(4, objet.getProficiencyBonus(objet.getAbility3()));
        psInsert.setInt(5, objet.getProficiencyBonus(objet.getAbility4()));
        psInsert.setInt(6, objet.getProficiencyBonus(objet.getAbility5()));
        psInsert.setInt(7, objet.getProficiencyBonus(objet.getAbility6()));
        psInsert.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public Optional<Character> read(String identifier) {
    Character Character = null;
    try {
        PreparedStatement psInsert = connection.prepareStatement(
                                    "SELECT * FROM characters WHERE Name = ?");
        psInsert.setString(1, identifier);
        ResultSet rs = psInsert.executeQuery();
        if (rs.next()) {
            Ability ability1 = Ability.valueOf(rs.getString(2));
            Ability ability2 = Ability.valueOf(rs.getString(3));
            Ability ability3 = Ability.valueOf(rs.getString(4));
            Ability ability4 = Ability.valueOf(rs.getString(5));
            Ability ability5 = Ability.valueOf(rs.getString(6));          
            Ability ability6 = Ability.valueOf(rs.getString(7));
            Ability[] preAbilities = new Ability[]{ability1,ability2,ability3,ability4,ability5,ability6};
            Character = new Character.CharacterBuilder(rs.getString(1), preAbilities).build();
        }
        
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
    return Optional.ofNullable(Character);
  }

  @Override
  public boolean update(Character objet) {
    try {
        PreparedStatement ps = connection.prepareStatement(
            "UPDATE characters SET Ability1 = ?, Ability2 = ?, Ability3 = ?, Ability4 = ?, Ability5 = ?, Ability6 = ? WHERE Name = ?");
        ps.setString(1, objet.getAbility1().name());
        ps.setString(2, objet.getAbility2().name());
        ps.setString(3, objet.getAbility3().name());
        ps.setString(4, objet.getAbility4().name());
        ps.setString(5, objet.getAbility5().name());
        ps.setString(6, objet.getAbility6().name());
        ps.executeUpdate();

        ps = connection.prepareStatement("DELETE FROM abilityValues WHERE NameCharacter = ?");
        ps.setString(1, objet.getname());
        ps.executeUpdate();

        ps = connection.prepareStatement("INTO abilityValues VALUES(?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, objet.getname());
        ps.setInt(2, objet.getScore(objet.getAbility1()));
        ps.setInt(3, objet.getScore(objet.getAbility2()));
        ps.setInt(4, objet.getScore(objet.getAbility3()));
        ps.setInt(5, objet.getScore(objet.getAbility4()));
        ps.setInt(6, objet.getScore(objet.getAbility5()));
        ps.setInt(7, objet.getScore(objet.getAbility6()));
        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    return true;
  }

  @Override
  public void delete(Character objet) {
    try {
      PreparedStatement ps = connection.prepareStatement("DELETE FROM abilityValues WHERE NameCharacter = ?");
      ps.setString(1, objet.getname());
      ps.executeUpdate();
      ps = connection.prepareStatement("DELETE FROM characters WHERE Name = ?");
      ps.setString(1, objet.getname());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
