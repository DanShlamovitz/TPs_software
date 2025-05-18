package uno;

import java.util.ArrayList;
import java.util.Arrays;

// CAMBIAR TurnManager POR Game??

public class TurnManager {

    private ArrayList<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private Card lastCardPlayed;
    private Deck deck;

    public TurnManager(Player... players) {
        this.players = new ArrayList<>(Arrays.asList(players));
        deck = new Deck();

        for (int i = 0; i < this.players.size(); i++) {
            this.players.get(i).drawCards(deck, 7);
        }
        currentPlayer = this.players.getFirst();
        lastCardPlayed = new NoCard();
    }
    public TurnManager playCard(Card card){
        return card.play(this);
    }
    public ArrayList<Player> getPlayers() { return players; }
    public int getNumberOfPlayers() { return players.size(); }
    public Player getCurrentPlayer() { return currentPlayer; }
    public int getCurrentPlayerIndex() { return players.indexOf(currentPlayer); }
    public Deck getDeck() { return deck; }

    public TurnManager setCurrentPlayer(Player nextPlayer) {
        currentPlayer = nextPlayer;
        return this;
    }
    public Card getLastCardPlayed() { return lastCardPlayed; }
    public TurnManager setLastCardPlayed(Card lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
        return this;
    }
}
