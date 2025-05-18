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
    public TurnManager playerPlayCard(Player player, Card card){
        if ( !(currentPlayer.getName().equals(player.getName())) ) {
            throw new RuntimeException("Error: this player can't play at the moment because" +
                    " it isn't their turn yet!");
        }
        if (!player.getCards().contains(card)) {
            throw new RuntimeException("Error: invalid card was played." +
                    " The player doesn't have the requested card in their hand!");
        }
        if ( card.canBePlayedWith(lastCardPlayed) ) {
            player.getCards().remove(card);
            player.checkForVictory();
            card.play(this);
            return this;
        }
        throw new RuntimeException("Error: the selected card can't be played because" +
                "it's not compatible with the card that was played before");
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
