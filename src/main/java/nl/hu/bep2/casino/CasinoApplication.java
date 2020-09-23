package nl.hu.bep2.casino;

import nl.hu.bep2.casino.blackjack.domain.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CasinoApplication {

    public static void main(String[] args) {
        //♣ ♦ ♥ ♠
        SpringApplication.run(CasinoApplication.class, args);
        Utils.printWelcome();
    }
}
