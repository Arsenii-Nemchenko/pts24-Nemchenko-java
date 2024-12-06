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

    /**
     * Constructs a CurrentThrow object for the specified player, effect, and number of dice.
     *
     * @param player the player performing the throw
     * @param effect the type of resource the throw is for
     * @param dices  the number of dice to roll
     */
    public CurrentThrow(Player player, Effect effect, int dices) {
        this.player = player;
        this.throwsFor = effect;
        int[]  throwsResult = Throw.throw_(dices);
        for(int element: throwsResult){
            this.throwResult += element;
        }

    }
    /**
     * Initializes a throw action for the specified player, effect, and number of dice.
     *
     * @param player the player performing the throw
     * @param effect the type of resource the throw is for
     * @param dices  the number of dice to roll
     */
    public void initiate(Player player, Effect effect, int dices){
        this.player = player;
        this.throwsFor = effect;
        int[] throwsResult = Throw.throw_(dices);
        for(int element: throwsResult){
            this.throwResult += element;
        }
    }
    /**
     * Uses a tool to modify the current throw result.
     *
     * @param idx the index of the tool to use
     * @return {@code true} if the tool was successfully used, {@code false} otherwise
     */
    @Override
    public boolean useTool(int idx) {

        Optional<Integer> res = player.getPlayerBoard().useTool(idx);
        if (res.isEmpty()) {
            return false;
        }

        this.throwResult += res.get();
        return true;
    }
    /**
     * Checks if the player has sufficient tools to modify the throw result.
     *
     * @return {@code true} if tools can be used, {@code false} otherwise
     */
    @Override
    public boolean canUseTools() {
        return player.getPlayerBoard().hasSufficientTools(1);
    }

    /**
     * Finalizes the tool usage and calculates resources based on the throw result.
     *
     * @return {@code true} if the operation is successful, {@code false} otherwise
     */
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

    /**
     * Returns the current state of this throw action as a JSON string.
     *
     * @return a JSON representation of the state
     */

    public String state(){
        return new JSONObject(Map.of("throwsFor", throwsFor,
                "throwResult", throwResult,
                "player", player
        )).toString();
    }
}
