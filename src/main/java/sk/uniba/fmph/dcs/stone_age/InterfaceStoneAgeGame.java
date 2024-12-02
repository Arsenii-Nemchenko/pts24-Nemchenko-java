package sk.uniba.fmph.dcs.stone_age;

import java.util.List;

public interface InterfaceStoneAgeGame {
    boolean placeFigures(int playerId, Location location, int figuresCount);

    boolean makeAction(int playerId, Location location, List<Effect> usedResources, List<Effect> desiredResources);

    boolean skipAction(int playerId, Location location);

    boolean useTools(int playerId, int toolIndex);

    boolean noMoreToolsThisThrow(int playerId);

    boolean feedTribe(int playerId, List<Effect> resources);

    boolean doNotFeedThisTurn(int playerId);

    boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward);
}
