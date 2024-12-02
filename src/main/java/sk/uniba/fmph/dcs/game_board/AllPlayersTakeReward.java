package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

public class AllPlayersTakeReward implements EvaluateCivilizationCardImmediateEffect{
    @Override
    public boolean performEffect(Player player, Effect effect){
        return true;
    }
}
