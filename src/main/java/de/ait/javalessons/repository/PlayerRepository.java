package de.ait.javalessons.repository;

import de.ait.javalessons.model.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
