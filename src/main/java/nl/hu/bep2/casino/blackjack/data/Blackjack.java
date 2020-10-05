package nl.hu.bep2.casino.blackjack.data;

import nl.hu.bep2.casino.blackjack.domain.BlackjackGame;
import nl.hu.bep2.casino.security.data.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "blackjack")
public class Blackjack {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private BlackjackGame blackjackGame;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date creationDate;

    @Column
    private Boolean gameDone;

    @OneToOne
    private User user;

    public Blackjack(){}
    public Blackjack(BlackjackGame blackjackGame, Boolean gameDone, User user) {
        this.blackjackGame = blackjackGame;
        this.gameDone = gameDone;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public BlackjackGame getBlackjackGame() {
        return blackjackGame;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isGameDone() {
        return gameDone;
    }

    public void setGameDone(Boolean gameDone) {
        this.gameDone = gameDone;
    }


}
