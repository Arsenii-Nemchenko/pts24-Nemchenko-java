package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.List;

public class GetSomethingFixed implements EvaluateCivilizationCardImmediateEffect{
    @Override
    public boolean performEffect(Player player, Effect choice) {
        player.playerBoard().giveEffect(List.of(choice));
        return true;
    }
}
