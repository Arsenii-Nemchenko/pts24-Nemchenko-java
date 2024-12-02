package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;

public interface InterfaceFigureLocationInternal {
    boolean placeFigures(Player player, int figureCount);
    HasAction tryToPlaceFigures(Player player, int count);
    ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources);
    boolean skipAction(Player player);
    HasAction tryToMakeAction(Player player);
    boolean newTurn();
}
