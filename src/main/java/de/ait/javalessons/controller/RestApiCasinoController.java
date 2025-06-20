package de.ait.javalessons.controller;

import de.ait.javalessons.model.Bet;
import de.ait.javalessons.model.Player;
import de.ait.javalessons.repository.BetRepository;
import de.ait.javalessons.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/casino")
public class RestApiCasinoController {

    private PlayerRepository playerRepository;

    private BetRepository betRepository;

    public RestApiCasinoController(PlayerRepository playerRepository, BetRepository betRepository) {
        this.playerRepository = playerRepository;
        this.betRepository = betRepository;
    }

    @PostMapping("/player")
    public Player createPlayer(@RequestParam String name) {
        Player player = new Player();
        player.setName(name);
        player.setBalance(1000);

        Player savedPlayer = playerRepository.save(player);
        log.info("Player with id {} created", savedPlayer.getId());
        return savedPlayer;
    }

    @PostMapping("/bet")
    public String placeBet(@RequestParam Long playerId, @RequestParam int amount) {
        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new IllegalArgumentException("Игрок не найден"));
        if (player.getBalance() < amount) {
            return "Недостаточно средств";
        }

        boolean win = Math.random() > 0.5;
        int winSize = win ? amount * 2 : 0;

        Bet bet = new Bet();
        bet.setAmount(amount);
        bet.setWin(win);
        bet.setTimeOfBet(LocalDateTime.now());

        player.addBett(bet);

        player.setBalance(player.getBalance() - amount + winSize);

        playerRepository.save(player);

        if (win) {
            return "Поздравляем, вы выиграли";
        }
        return "Вы проиграли";
    }

    @GetMapping("/player/{id}/bets")
    public List<Bet> getPlayersBets(@PathVariable Long id) {
        Player player = playerRepository.findById(id).orElseThrow();
        return player.getBets();
    }

    @GetMapping("/casino/winners")
    public List<Bet> getWinnersBets(){
        return betRepository.getAllByWinIsTrue();
    }


}
