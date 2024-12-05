package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;

import java.util.*;

public class StoneAgeGame implements InterfaceStoneAgeGame{
    private final Map<Integer, Player> players;
    private final List<Player> totalPlayers;
    private final StoneAgeObservable observable;
    private final InterfaceGamePhaseController gamePhaseController;
    private final GameBoard gameBoard;

    public StoneAgeGame(int amountOfPlayers) {
        //Create map of players
        players = new HashMap<>();
        for (int i = 0; i < amountOfPlayers; i++) {
            players.put(i, new Player(new PlayerOrder(i, amountOfPlayers), new PlayerBoardGameBoardFacade(new PlayerBoard())));
        }


        totalPlayers = new ArrayList<>();
        for(Integer key: players.keySet()){
            totalPlayers.add(players.get(key));
        }
        this.gameBoard = new GameBoard(totalPlayers);


        this.gamePhaseController = new GamePhaseController(createController(), players.get(0).getPlayerOrder());

        this.observable = new StoneAgeObservable();
    }


    private Map<GamePhase, InterfaceGamePhaseState> createController(){
        Map<GamePhase, InterfaceGamePhaseState> map = new HashMap<>();

        map.put(GamePhase.GAME_END, new GameEndState());
        map.put(GamePhase.ALL_PLAYERS_TAKE_A_REWARD, new AllPlayersTakeARewardState());
        map.put(GamePhase.WAITING_FOR_TOOL_USE, new WaitingForToolUseState(null));
        map.put(GamePhase.NEW_ROUND, new NewRoundState());
        map.put(GamePhase.FEED_TRIBE, new FeedTribeState((new HashMap<>())));
        map.put(GamePhase.MAKE_ACTION, new MakeActionState(gameBoard.getMap(), players.get(0).getPlayerOrder()));
        map.put(GamePhase.PLACE_FIGURES, new PlaceFiguresState(gameBoard.getMap()));
        return map;
    }
    private void notifyObserver(){
        observable.notify(gameBoard.state());
        observable.notify(gamePhaseController.state());
        for(Integer key: players.keySet()){
            PlayerBoardGameBoardFacade playerBoard = (PlayerBoardGameBoardFacade) players.get(key).getPlayerBoard();
            observable.notify(playerBoard.getPlayerBoard().state());
        }
    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean figuresResult = gamePhaseController.placeFigures(players.get(playerId).getPlayerOrder(), location, figuresCount);
        notifyObserver();
        return figuresResult;
    }

    @Override
    public boolean makeAction(int playerId, Location location, List<Effect> usedResources, List<Effect> desiredResources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean actionResult = gamePhaseController.makeAction(players.get(playerId).getPlayerOrder(), location, usedResources, desiredResources);
        notifyObserver();
        return actionResult;
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean skipActionResult = gamePhaseController.skipAction(players.get(playerId).getPlayerOrder(), location);
        notifyObserver();
        return skipActionResult;
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean useToolsResult = gamePhaseController.useTools(players.get(playerId).getPlayerOrder(), toolIndex);
        notifyObserver();
        return useToolsResult;
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean noMoreToolsResult = gamePhaseController.noMoreToolsThisThrow(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return noMoreToolsResult;
    }

    @Override
    public boolean feedTribe(int playerId, List<Effect> resources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean feedTribeResult = gamePhaseController.feedTribe(players.get(playerId).getPlayerOrder(), resources);
        notifyObserver();
        return feedTribeResult;
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean doNotFeedResult = gamePhaseController.doNotFeedThisTurn(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return doNotFeedResult;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean allTakeRewardResult = gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId).getPlayerOrder(), reward);
        notifyObserver();
        return allTakeRewardResult;
    }
}


