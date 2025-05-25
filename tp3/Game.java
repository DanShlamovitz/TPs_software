package uno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

// CAMBIAR TurnManager POR Game??

public class Game {

    private ArrayList<Player> players = new ArrayList<>();
    private Card lastCardPlayed;
    private ArrayList<Card> deck;

//    public TurnManager(Player... players) {
//        this(new Deck(), 7, players);
//    }
//    public TurnManager(Deck aDeck, int nOfCards, Player... players) {
//        this(aDeck.cards, nOfCards, players);
//    }
    public Game(ArrayList<Card> aDeck, int nOfCards, Player... players) {
        this.players = new ArrayList<>(Arrays.asList(players));
        deck = aDeck;
        lastCardPlayed = draw();
    }
    public Card draw() {
        if (deck.isEmpty()) {
            throw new NoSuchElementException("The deck is empty!");
        }
        return deck.removeFirst();
    }
    private boolean canPlayerPlay(Player player) {
        if ( !(players.getFirst().getName().equals(player.getName())) ) {
            return false;
        }
        return player.getCards().stream().anyMatch(card -> card.canBeStackedOnTopOf(lastCardPlayed));
    }
    public Game playNumberCard(NumberCard card) {
        setLastCardPlayed(card);
        players.add(players.getFirst());
        players.removeFirst();
        return this;
    }
    public Game playSkipCard(SkipCard card) {
        setLastCardPlayed(card);
        players.add(players.getFirst());
        players.add(players.get(1));
        players.removeFirst();
        players.removeFirst();
        return this;
    }
    public Game playReverseCard(ReverseCard card) {
        setLastCardPlayed(card);
        Collections.reverse(getPlayers());
        return this;
    }
    public Game playWildcard(Wildcard card) {
        if (card.getColor().equals("Any")) {
            throw new RuntimeException("A Wildcard's color must be set before it is played!");
        }
        setLastCardPlayed(card);
        players.add(players.getFirst());
        players.removeFirst();
        return this;
    }
    public Game playDrawTwoCard(DrawTwoCard card) {
        setLastCardPlayed(card);
        players.add(players.getFirst());
        players.add(players.get(1));
        players.removeFirst();
        players.getFirst().drawCard(this);
        players.getFirst().drawCard(this);
        players.removeFirst();
        return this;
    }
    private Game checkPenalizeForNotShoutingUno(Player player) {
        if (!player.shoutedUno() && player.getCards().size() == 1) {
            player.drawCard(this);
            player.drawCard(this);
        }
        return this;
    }
    public Game playerPlayCard(Player player, Card card){

        if ( !canPlayerPlay(player) ) {
            player.drawCard(this);
            return this;
        }
        if (!player.getCards().contains(card)) {
            throw new RuntimeException("Error: invalid card was played." +
                    " The player doesn't have the requested card in their hand!");
        }
        if ( card.canBeStackedOnTopOf(lastCardPlayed) ) {
            player.getCards().remove(card);
            player.checkForVictory();
            card.play(this);
            checkPenalizeForNotShoutingUno(player);
            player.resetUnoShout();
            return this;
        }
        throw new RuntimeException("Error: the selected card can't be played because" +
                "it's not compatible with the card that was played before");
    }
    public ArrayList<Player> getPlayers() { return players; }
    public Card getLastCardPlayed() { return lastCardPlayed; }
    public Player getCurrentPlayer() { return players.getFirst(); }
    public Game setLastCardPlayed(Card lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
        return this;
    }
}
