package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Map;

public class ResourceSource implements InterfaceFigureLocationInternal {
    private String name;
    private final Effect resource;
    private int maxFigures;
    private final int maxFigureColors;
    private List<PlayerOrder> figures;

    private CurrentThrow currentThrow;

    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColors, CurrentThrow currentThrow) {
        this.currentThrow = currentThrow;
        this.resource = resource;
        this.name = name;
        this.maxFigures = maxFigures;
        this.maxFigureColors = maxFigureColors;
        figures = new ArrayList<>();
    }

    //Places figureCount figures
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if(!canPlaceFigures(player, figureCount)){
            return false;
        }

        for (int i = 0; i < figureCount; i++) {
            this.figures.add(player.getPlayerOrder());
        }
        player.getPlayerBoard().takeFigures(figureCount);

        return true;
    }

    //Returns true if player can place figureCount figures
    private boolean canPlaceFigures(Player player, int figureCount){
        if (figures.contains(player.getPlayerOrder()) || !player.getPlayerBoard().hasFigures(figureCount)) {
            return false;
        }

        if (maxFigures - figures.size() < figureCount) {
            return false;
        }

        List<PlayerOrder> differentPlayerFigures = new ArrayList<>();
        for (PlayerOrder order : this.figures) {
            if (!differentPlayerFigures.contains(order)) {
                differentPlayerFigures.add(order);
            }
        }

        if(maxFigureColors <= differentPlayerFigures.size()){
            return false;
        }
        return true;
    }

    //Tries to place figures
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    //Makes action and uses tools at once
    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if (outputResources.length != 0) {
            return ActionResult.FAILURE;
        }

        int playerFigureCount = 0;
        for (PlayerOrder playerOrder : figures) {
            if (playerOrder.equals(player.getPlayerOrder())) {
                playerFigureCount++;
            }
        }
        currentThrow.initiate(player, resource, playerFigureCount);


        List<PlayerOrder> toRemove = new ArrayList<>();
        for(PlayerOrder playerOrder: figures){
            if(playerOrder.equals(player.getPlayerOrder())){
                toRemove.add(playerOrder);
            }
        }

        figures.removeAll(toRemove);

        for (Effect effect : inputResources) {
            if (effect != Effect.TOOL) {
                return ActionResult.FAILURE;
            }
        }

        for (Effect effect : inputResources) {
            int i = 0;
            while (currentThrow.canUseTools() && !currentThrow.useTool(i++)){
                if(i == 6){
                    break;
                }
            }
        }
        currentThrow.finishUsingTools();

        return ActionResult.ACTION_DONE;
    }

    //You never can skip this action
    @Override
    public boolean skipAction(final Player player) {
        return false;
    }
    //Tries to make action
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.contains(player.getPlayerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    //If no action possible returns true
    @Override
    public boolean newTurn() {
        if(figures.isEmpty()){
            figures.clear();
        }

        return false;
    }

    //Returns state of ResourceSource class
    public String state() {
        Map<String, Object> state = Map.of(
                "name", name,
                "resource", resource,
                "maxFigures", maxFigures,
                "maxFigureColors", maxFigureColors,
                "figures", figures);
        return new JSONObject(state).toString();
    }
}
