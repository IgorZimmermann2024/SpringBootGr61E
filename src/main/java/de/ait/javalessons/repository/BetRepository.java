package de.ait.javalessons.repository;

import de.ait.javalessons.model.Bet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BetRepository extends CrudRepository <Bet, Long>{

    List<Bet> getAllByWinIsTrue();

}
