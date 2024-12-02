package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

public class GetSomethingThrow implements EvaluateCivilizationCardImmediateEffect {
    private CurrentThrow currentThrow;

    public GetSomethingThrow(CurrentThrow currentThrow){
        this.currentThrow = currentThrow;
    }

    @Override
    public boolean performEffect(Player player, Effect choice) {
        currentThrow.initiate(player, choice, 2);
        return currentThrow.finishUsingTools();
    }

}
