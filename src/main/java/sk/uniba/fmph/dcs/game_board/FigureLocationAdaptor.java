package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class FigureLocationAdaptor implements InterfaceFigureLocation {
    private final InterfaceFigureLocationInternal figureLocation;
    private final List<Player> players;


    public FigureLocationAdaptor(final InterfaceFigureLocationInternal figureLocation, List<Player> players) {
        this.figureLocation = figureLocation;
        this.players = players;
    }

    // Helper method to find the Player object by PlayerOrder
    private Player getPlayerOrder(PlayerOrder player) {
        for (Player pl : players) {
            if (pl.playerOrder().equals(player)) {
                return pl;
            }
        }
        return null;
    }

    // Places the specified number of figures for the given player
    @Override
    public boolean placeFigures(PlayerOrder player, int figureCount) {
        Player p = getPlayerOrder(player);
        if (p != null) {
            return figureLocation.placeFigures(p, figureCount);
        }
        return false;
    }

    // Checks if figures can be placed for the given player
    @Override
    public HasAction tryToPlaceFigures(PlayerOrder player, int count) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.tryToPlaceFigures(pl, count);
        }
        return null;
    }

    // Performs an action for the player using input and output resources
    @Override
    public ActionResult makeAction(PlayerOrder player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        Effect[] input = inputResources.toArray(new Effect[0]);
        Effect[] output = outputResources.toArray(new Effect[0]);
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.makeAction(pl, input, output);
        }
        return null;
    }

    // Skips the action
    @Override
    public boolean skipAction(PlayerOrder player) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            return figureLocation.skipAction(pl);
        }
        return false;
    }

    // Attempts to perform an action for the given player
    @Override
    public HasAction tryToMakeAction(PlayerOrder player) {
        Player pl = getPlayerOrder(player);
        if (pl != null) {
            figureLocation.tryToMakeAction(pl);
        }
        return null;
    }

    @Override
    public boolean newTurn() {
        return figureLocation.newTurn();
    }
}