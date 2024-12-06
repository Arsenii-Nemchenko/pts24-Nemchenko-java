package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

public class CivilizationCardDeck implements InterfaceGetState {
    private Stack<CivilizationCard> stack;

    /**
     * Constructs a CivilizationCardDeck with the given list of cards.
     *
     * @param civilizationCardList the list of civilization cards to initialize the deck with.
     */
    public CivilizationCardDeck(List<CivilizationCard> civilizationCardList){
        stack = new Stack<>();
        if(civilizationCardList!=null) {
            stack.addAll(civilizationCardList);
        }
    }

    /**
     * Retrieves and removes the top card from the deck.
     *
     * @return an {@link Optional} containing the top card if the deck is not empty,
     *         or {@link Optional#empty()} if the deck is empty.
     */
    public Optional<CivilizationCard> getTop(){
        if(stack.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(stack.pop());
    }
    /**
     * Returns the current state of the deck as a JSON string.
     * The state includes the remaining cards in the stack.
     *
     * @return a JSON representation of the deck's state.
     */
    @Override
    public String state(){
        return new JSONObject(Map.of("stack", stack)).toString();
    }
}
