package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Character;

import java.nio.file.Path;

public class SerializedDaoFactory extends DaoFactory{
    private final Path tmpDirectory;

    public SerializedDaoFactory(Path tmpDirectory) {
        this.tmpDirectory = tmpDirectory;
    }

    public Dao<Character> getCharacterDao() {
        return new CharacterSerializedDao(tmpDirectory);
    }
}
