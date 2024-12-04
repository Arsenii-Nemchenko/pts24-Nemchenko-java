package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor;
    private int throwResult;
    private Player player;


    public CurrentThrow(Player player, Effect effect, int dices) {
        this.player = player;
        this.throwsFor = effect;
        int[]  throwsResult = Throw.throw_(dices);
        for(int element: throwsResult){
            this.throwResult += element;
        }

    }

    public void initiate(Player player, Effect effect, int dices){
        this.player = player;
        this.throwsFor = effect;
        int[] throwsResult = Throw.throw_(dices);
        for(int element: throwsResult){
            this.throwResult += element;
        }
    }

    @Override
    public boolean useTool(int idx) {

        Optional<Integer> res = player.getPlayerBoard().useTool(idx);
        if (res.isEmpty()) {
            return false;
        }

        this.throwResult += res.get();
        return true;
    }

    @Override
    public boolean canUseTools() {
        return player.getPlayerBoard().hasSufficientTools(1);
    }

    @Override
    public boolean finishUsingTools() {

        List<Effect> list = new ArrayList<>();
        switch (throwsFor) {
            case FOOD:
                for(int i =0; i< this.throwResult/2; i++){
                    list.add(Effect.FOOD);
                }
            case WOOD:
                for (int i = 0; i < this.throwResult / 3; i++) {
                    list.add(Effect.WOOD);
                }
                break;
            case CLAY:
                for (int i = 0; i < this.throwResult / 4; i++) {
                    list.add(Effect.CLAY);
                }
                break;
            case STONE:
                for (int i = 0; i < this.throwResult / 5; i++) {
                    list.add(Effect.STONE);
                }
                break;
            case GOLD:
                for (int i = 0; i < this.throwResult / 6; i++) {
                    list.add(Effect.GOLD);
                }
                break;
            default:
                return false;
        }

        player.getPlayerBoard().giveEffect(list);

        return true;
    }

    public String state(){
        return new JSONObject(Map.of("throwsFor", throwsFor,
                "throwResult", throwResult,
                "player", player
        )).toString();
    }
}
