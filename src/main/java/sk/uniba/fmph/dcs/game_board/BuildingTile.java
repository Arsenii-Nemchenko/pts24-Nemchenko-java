package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import org.json.JSONObject;

public class BuildingTile implements InterfaceFigureLocationInternal {
    private final Stack<Building> buildings;
    private final ArrayList<PlayerOrder> figures;
    private static final int MAX_FIGURES = 1;
    
    public BuildingTile(List<Building> building) {
        this.buildings = new Stack<>();
        this.buildings.addAll(building);
        this.figures = new ArrayList<>();
    }

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (figureCount != MAX_FIGURES || !figures.isEmpty()) {
            return false;
        }
        figures.add(player.getPlayerOrder());
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (!player.getPlayerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if(placeFigures(player, count)){
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerBoard())) {
            return ActionResult.FAILURE;
        }
        
        Collection<Effect> resources = List.of(inputResources);

        if(buildings.isEmpty()){
            return ActionResult.FAILURE;
        }
        OptionalInt points = buildings.pop().build(resources);
        if(points.isEmpty()){
            return ActionResult.FAILURE;
        }

        if(!player.getPlayerBoard().takeResources(List.of(inputResources))){
            return ActionResult.FAILURE;
        }
        
        // Give points to player
        List<Effect> pointsToGive = new ArrayList<>();
        for(int i=0; i<points.getAsInt(); i++){
            pointsToGive.add(Effect.POINT);
        }
        player.getPlayerBoard().giveEffect(pointsToGive);
        return ActionResult.ACTION_DONE;
    }

    @Override
    public boolean skipAction(Player player) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return false;
        }
        figures.clear();
        return true;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if (figures.isEmpty() || !figures.get(0).equals(player.getPlayerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        if(buildings.isEmpty()){
            return true;
        }
        figures.clear();
        return false;
    }

    public String state() {
        Map<String, Object> state = Map.of(
            "building", buildings,
            "figures", figures.stream().map(PlayerOrder::getOrder).toList()
        );
        return new JSONObject(state).toString();
    }
}
