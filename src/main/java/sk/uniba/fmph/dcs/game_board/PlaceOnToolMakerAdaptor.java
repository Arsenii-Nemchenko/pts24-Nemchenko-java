package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.Map;

public class PlaceOnToolMakerAdaptor implements InterfaceFigureLocationInternal{

private final ToolMakerHutFields toolMakerHutFields;

    public PlaceOnToolMakerAdaptor(ToolMakerHutFields toolMakerHutFields){
        this.toolMakerHutFields = toolMakerHutFields;
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(figureCount >= 1 && toolMakerHutFields.canPlaceOnToolMaker(player)){
            toolMakerHutFields.placeOnToolMaker(player);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(!player.playerBoard().hasFigures(count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(toolMakerHutFields.canPlaceOnToolMaker(player)){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if(!toolMakerHutFields.canPlaceOnToolMaker(player)){
            return ActionResult.FAILURE;
        }

        if(toolMakerHutFields.actionToolMaker(player)) {
            return ActionResult.ACTION_DONE;
        }

        return ActionResult.FAILURE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if(toolMakerHutFields.canPlaceOnToolMaker(player)){
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }else{
            return HasAction.NO_ACTION_POSSIBLE;
        }
    }

    @Override
    public boolean newTurn() {
        return false;
    }

    @Override
    public String state(){
        Map<String, Object> map = Map.of("toolMakerHutFields", toolMakerHutFields);
        return new JSONObject(map).toString();
    }
}
