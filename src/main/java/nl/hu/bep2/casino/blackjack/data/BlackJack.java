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
    private Long bet;
    private String username;
    private boolean isGameDone = false;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date creationDate;

    public BlackJack(){}
    public BlackJack(Long playerscore, Long dealerscore, GameState gameState, Long bet, String username) {
        this.playerScore = playerscore;
        this.dealerScore = dealerscore;
        this.gameState = gameState;
        this.bet = bet;
        this.username = username;
    }

    public void update(Long playerscore, Long dealerscore, GameState gameState, boolean isGameDone) {
        this.playerScore = playerscore;
        this.dealerScore = dealerscore;
        this.gameState = gameState;
        this.isGameDone = isGameDone;
    }

    public Long getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Long getPlayerScore() {
        return playerScore;
    }

    public Long getDealerScore() {
        return dealerScore;
    }

    public Long getBet() {
        return bet;
    }

    public String getUsername() {
        return username;
    }

    public Date getCreationDate() {
        return creationDate;
    }

}
