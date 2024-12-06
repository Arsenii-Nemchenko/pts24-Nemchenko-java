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
    /**
     * Constructs a new StoneAgeGame with the specified number of players.
     *
     * @param amountOfPlayers The number of players participating in the game.
     */

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

    /**
     * Creates the controller mapping game phases to their respective states.
     *
     * @return A map of {@link GamePhase} to {@link InterfaceGamePhaseState}.
     */
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

    /**
     * Notifies the observable about the current game state, including the game board, game phases,
     * and player boards.
     */
    private void notifyObserver(){
        observable.notify(gameBoard.state());
        observable.notify(gamePhaseController.state());
        for(Integer key: players.keySet()){
            PlayerBoardGameBoardFacade playerBoard = (PlayerBoardGameBoardFacade) players.get(key).getPlayerBoard();
            observable.notify(playerBoard.getPlayerBoard().state());
        }
    }

    /**
     * Places a figure on the card if conditions are met.
     *
     * @param playerId the player skipping the action
     * @param location the location
     * @param figuresCount the count of figures being placed
     * @return {@code true} if the figures were placed successfully, otherwise {@code false}
     */

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean figuresResult = gamePhaseController.placeFigures(players.get(playerId).getPlayerOrder(), location, figuresCount);
        notifyObserver();
        return figuresResult;
    }

    /**
     * Executes an action for a player.
     *
     * @param playerId  The player performing the action.
     * @param usedResources  The input resources for the action.
     * @param desiredResources The output resources from the action.
     * @return {@code true} if successful, otherwise {@code false} .
     */
    @Override
    public boolean makeAction(int playerId, Location location, List<Effect> usedResources, List<Effect> desiredResources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean actionResult = gamePhaseController.makeAction(players.get(playerId).getPlayerOrder(), location, usedResources, desiredResources);
        notifyObserver();
        return actionResult;
    }

    /**
     * Skips the action for the current player on specified location.
     *
     * @param playerId the player skipping the action
     * @param location the location
     * @return {@code true} if the action was skipped successfully, otherwise {@code false}
     */
    @Override
    public boolean skipAction(int playerId, Location location) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean skipActionResult = gamePhaseController.skipAction(players.get(playerId).getPlayerOrder(), location);
        notifyObserver();
        return skipActionResult;
    }

    /**
     * Uses a tool to modify the throw result.
     *
     * @param playerId the player that uses tools
     * @param toolIndex the index of the tool to use
     * @return {@code true} if the tool was successfully used, {@code false} otherwise
     */
    @Override
    public boolean useTools(int playerId, int toolIndex) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean useToolsResult = gamePhaseController.useTools(players.get(playerId).getPlayerOrder(), toolIndex);
        notifyObserver();
        return useToolsResult;
    }

    /**
     * Indicates that the player will no longer use tools for this turn.
     *
     * @param playerId The ID of the player.
     * @return {@code true} if successfully indicated; {@code false} otherwise.
     */
    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean noMoreToolsResult = gamePhaseController.noMoreToolsThisThrow(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return noMoreToolsResult;
    }


    /**
     * Feeds the tribe for the specified player using the given resources.
     *
     * @param playerId  The ID of the player feeding their tribe.
     * @param resources The resources used to feed the tribe.
     * @return {@code true} if the tribe was successfully fed; {@code false} otherwise.
     */
    @Override
    public boolean feedTribe(int playerId, List<Effect> resources) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean feedTribeResult = gamePhaseController.feedTribe(players.get(playerId).getPlayerOrder(), resources);
        notifyObserver();
        return feedTribeResult;
    }
    /**
     * Indicates that the player will not feed their tribe this turn.
     *
     * @param playerId The ID of the player.
     * @return {@code true} if successfully indicated; {@code false} otherwise.
     */
    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        if(!players.containsKey(playerId)){
            return false;
        }

        boolean doNotFeedResult = gamePhaseController.doNotFeedThisTurn(players.get(playerId).getPlayerOrder());
        notifyObserver();
        return doNotFeedResult;
    }
    /**
     * Makes a reward choice for all players.
     *
     * @param playerId The ID of the player making the choice.
     * @param reward   The reward chosen.
     * @return {@code true} if the reward choice was successful; {@code false} otherwise.
     */
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


