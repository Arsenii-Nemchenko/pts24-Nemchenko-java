package sk.uniba.fmph.dcs.game_board;

import java.util.Collection;

import sk.uniba.fmph.dcs.stone_age.*;

public class PlaceOnHutAdaptor implements InterfaceFigureLocationInternal, InterfaceGetState {
    private ToolMakerHutFields huts;
    public PlaceOnHutAdaptor(ToolMakerHutFields toolMakerHutFields){
        this.huts = toolMakerHutFields;
    }
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(tryToPlaceFigures(player, figureCount).equals(HasAction.WAITING_FOR_PLAYER_ACTION)){
            if(huts.placeOnHut(player)){
                player.getPlayerBoard().takeFigures(figureCount);
                return true;
            }
        }
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if(!player.getPlayerBoard().hasFigures(count)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(count != 2){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        if(!huts.canPlaceOnHut(player)){
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if(huts.actionHut(player)){
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
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        return huts.newTurn();
    }

    @Override
    public String state() {
        return huts.state();
    }
}