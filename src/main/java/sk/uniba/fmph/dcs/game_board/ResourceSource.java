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

    /**
     * Constructs a new ResourceSource.
     *
     * @param name             The name of the resource source.
     * @param resource         The type of resource provided by this source.
     * @param maxFigures       The maximum number of figures allowed at this source.
     * @param maxFigureColors  The maximum number of different players' figures allowed.
     * @param currentThrow     The CurrentThrow instance to handle dice-based resource gathering.
     */

    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColors, CurrentThrow currentThrow) {
        this.currentThrow = currentThrow;
        this.resource = resource;
        this.name = name;
        this.maxFigures = maxFigures;
        this.maxFigureColors = maxFigureColors;
        figures = new ArrayList<>();
    }

    /**
     * Places a specified number of figures for the given player.
     *
     * @param player      The player placing the figures.
     * @param figureCount The number of figures to place.
     * @return True if the figures were placed successfully, false otherwise.
     */
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

    /**
     * Checks if the specified player can place the given number of figures.
     *
     * @param player      The player attempting to place figures.
     * @param figureCount The number of figures to place.
     * @return True if placement is allowed, false otherwise.
     */
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

    /**
     * Attempts to place figures and returns the result.
     *
     * @param player The player attempting to place figures.
     * @param count  The number of figures to place.
     * @return {@code HasAction.AUTOMATIC_ACTION_DONE} if successful, otherwise {@code HasAction.NO_ACTION_POSSIBLE}.
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Executes an action for a player, including using tools and gathering resources.
     *
     * @param player          The player performing the action.
     * @param inputResources  The input resources for the action.
     * @param outputResources The output resources from the action.
     * @return {@code ActionResult.ACTION_DONE} if successful, {@code ActionResult.FAILURE} otherwise.
     */
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

    /**
     * Indicates that skipping an action is never allowed at this resource source.
     *
     * @param player The player attempting to skip the action.
     * @return Always false.
     */
    @Override
    public boolean skipAction(final Player player) {
        return false;
    }

    /**
     * Attempts to make an action for the given player.
     *
     * @param player The player attempting the action.
     * @return {@code HasAction.WAITING_FOR_PLAYER_ACTION} if the action is pending, otherwise {@code HasAction.NO_ACTION_POSSIBLE}.
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.contains(player.getPlayerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Prepares for a new turn by clearing all figures if empty.
     *
     * @return False, indicating no additional actions are needed.
     */
    @Override
    public boolean newTurn() {
        if(figures.isEmpty()){
            figures.clear();
        }

        return false;
    }

    /**
     * Returns the current state of the ResourceSource as a JSON string.
     *
     * @return The state of the resource source.
     */
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
