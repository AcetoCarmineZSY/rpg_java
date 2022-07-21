package fr.uvsq.hal.pglp.rpgdao;
import java.util.Optional;

public interface Dao<T> {
    boolean create(T objet);

    Optional<T> read(String identifier);

    boolean update(T objet);

    void delete(T objet);
}
