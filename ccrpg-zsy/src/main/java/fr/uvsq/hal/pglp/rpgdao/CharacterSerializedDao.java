package fr.uvsq.hal.pglp.rpgdao;

import fr.uvsq.hal.pglp.rpg.Character;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class CharacterSerializedDao implements Dao<Character>{
    private final Path directory;

    public CharacterSerializedDao(Path directory) {
        this.directory = directory;
    }

    @Override
    public boolean create(Character Character) {
        String filename = Character.getname();
        Path CharacterPath = directory.resolve(filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(CharacterPath))) {
        oos.writeObject(Character);
        } catch (IOException e) {
        return false;
        }
        return true;
    }

    @Override
    public Optional<Character> read(String identifier) {
        Path CharacterPath = directory.resolve(identifier);
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(CharacterPath))) {
        Character Character = (Character) ois.readObject();
        return Optional.of(Character);
        } catch (IOException e) {
        // return Optional.empty();
        } catch (ClassNotFoundException e) {
        // return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Character Character) {
        delete(Character);
        return create(Character);
    }

    @Override
    public void delete(Character Character) {
        String filename = Character.getname();
        Path CharacterPath = directory.resolve(filename);
        try {
        Files.deleteIfExists(CharacterPath);
        } catch (IOException e) {
        // Ignore the error
        }
    }
}
