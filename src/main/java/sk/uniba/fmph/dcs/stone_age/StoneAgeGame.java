package sk.uniba.fmph.dcs.stone_age;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoneAgeGame implements InterfaceStoneAgeGame{
    private Map<Integer, PlayerOrder> players;
    private StoneAgeObservable observable;
    private InterfaceGamePhaseController gamePhaseController;
    private InterfaceGetState playerBoard;
    private InterfaceGetState gameBoard;

    public StoneAgeGame(int playersAmount, StoneAgeObservable observable,
                        InterfaceGamePhaseController gamePhaseController,
                        InterfaceGetState playerBoard, InterfaceGetState gameBoard) {

        this.observable = observable;
        this.gamePhaseController = gamePhaseController;
        this.gameBoard = gameBoard;
        this.playerBoard = playerBoard;
        this.players = new HashMap<>();
        for (int i = 1; i <= playersAmount; i++) {
            PlayerOrder playerOrder = new PlayerOrder(i, playersAmount);
            players.put(i, playerOrder);
        }
    }

    private void notifyObserver(){
        observable.notify(gameBoard.state());
        observable.notify(playerBoard.state());
        observable.notify(gamePhaseController.state());
    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.placeFigures(players.get(playerId), location, figuresCount);
        notifyObserver();
        return result;
    }

    @Override
    public boolean makeAction(int playerId, Location location, List<Effect> usedResources, List<Effect> desiredResources) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.makeAction(players.get(playerId), location, usedResources, desiredResources);
        notifyObserver();
        return result;
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.skipAction(players.get(playerId), location);
        notifyObserver();
        return result;
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.useTools(players.get(playerId), toolIndex);
        notifyObserver();
        return result;
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.noMoreToolsThisThrow(players.get(playerId));
        notifyObserver();
        return result;
    }

    @Override
    public boolean feedTribe(int playerId, List<Effect> resources) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.feedTribe(players.get(playerId), resources);
        notifyObserver();
        return result;
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.doNotFeedThisTurn(players.get(playerId));
        notifyObserver();
        return result;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        if(players.containsKey(playerId)){
            return false;
        }

        boolean result = gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId), reward);
        notifyObserver();
        return result;
    }
}

