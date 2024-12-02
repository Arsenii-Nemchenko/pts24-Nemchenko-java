package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

public class CivilizationCardDeck implements InterfaceGetState {
    private Stack<CivilizationCard> stack;

    public CivilizationCardDeck(List<CivilizationCard> civilizationCardList){
        stack = new Stack<>();
        if(civilizationCardList!=null) {
            stack.addAll(civilizationCardList);
        }
    }

    public Optional<CivilizationCard> getTop(){
        if(stack.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(stack.pop());
    }
    @Override
    public String state(){
        return new JSONObject(Map.of("stack", stack)).toString();
    }
}
