package nl.hu.bep2.casino.blackjack.data;

import nl.hu.bep2.casino.blackjack.domain.enums.GameState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "blackjack")
public class BlackJack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long playerScore;
    private Long dealerScore;
    private GameState gameState;
    private Long chipsWon;
    private Long bet;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date creationDate;

    public BlackJack(){}
    public BlackJack(Long id, Long playerscore, Long dealerscore, GameState gameState, Long chipsWon, Long bet) {
        this.id = id;
        this.playerScore = playerscore;
        this.dealerScore = dealerscore;
        this.gameState = gameState;
        this.chipsWon = chipsWon;
        this.bet = bet;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerscore() {
        return playerScore;
    }

    public Long getDealerscore() {
        return dealerScore;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Long getChipsWon() {
        return chipsWon;
    }
}
