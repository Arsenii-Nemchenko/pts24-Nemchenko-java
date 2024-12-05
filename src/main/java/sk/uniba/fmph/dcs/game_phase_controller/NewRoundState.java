package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;

/**
 * The {@code NewRoundState} class represents the "New Round" phase in the Stone Age game.
 * During this phase, the game prepares for a new round by resetting locations and checking
 * for end-game conditions. This class implements the {@link InterfaceGamePhaseState} interface
 * and provides the logic for handling the automatic transition to the next phase.
 */
public class NewRoundState implements InterfaceGamePhaseState {

    /**
     * An interface that handles the initialization of a new turn.
     * It provides the {@code newTurn()} method to reset the game state for the new round.
     */
    private final InterfaceNewTurn places;

    /**
     * A flag indicating whether the new round has been initialized.
     * This ensures that the initialization occurs only once per round.
     */
    private boolean roundInitialized = false;

    /**
     * Constructs a new {@code NewRoundState} with the specified {@code InterfaceNewTurn} instance.
     */
    public NewRoundState() {
        this.roundInitialized = false;
        places = new InterfaceNewTurn() {
            @Override
            public boolean newTurn() {
                return false;
            }
        };
    }

    /**
     * Players cannot place figures during the "New Round" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player        the player attempting to place figures
     * @param location      the location where the player wants to place figures
     * @param figuresCount  the number of figures the player wants to place
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult placeFigures(PlayerOrder player, Location location, int figuresCount) {
        // Players cannot place figures during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Players cannot perform actions during the "New Round" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player           the player attempting to make an action
     * @param location         the location where the action is to be made
     * @param inputResources   resources provided as input for the action
     * @param outputResources  resources expected as output from the action
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources,
                                   Collection<Effect> outputResources) {
        // Players cannot make actions during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Skipping actions is not applicable during the "New Round" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player    the player attempting to skip an action
     * @param location  the location where the action is to be skipped
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        // No actions to skip during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Tool usage is not applicable during the "New Round" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player    the player attempting to use a tool
     * @param toolIndex the index of the tool being used
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult useTools(PlayerOrder player, int toolIndex) {
        // Tool usage is not applicable during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Indicating that the player will not use more tools for the current throw is not applicable.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player the player indicating no more tool usage
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder player) {
        // Not applicable during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Feeding the tribe is not handled during the "New Round" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player     the player attempting to feed their tribe
     * @param resources  the resources being used to feed the tribe
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult feedTribe(PlayerOrder player, Collection<Effect> resources) {
        // Feeding the tribe happens during the 'Feed Tribe' phase
        return ActionResult.FAILURE;
    }

    /**
     * Indicating that the player chooses not to feed their tribe this turn is not applicable.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player the player choosing not to feed their tribe
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult doNotFeedThisTurn(PlayerOrder player) {
        // Not applicable during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Making all players take a reward choice is not applicable during the "New Round" phase.
     * This method always returns {@link ActionResult#FAILURE}.
     *
     * @param player  the player attempting to make a reward choice
     * @param reward  the reward being chosen
     * @return {@code ActionResult.FAILURE} indicating the action is not allowed
     */
    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward) {
        // Not applicable during the 'New Round' phase
        return ActionResult.FAILURE;
    }

    /**
     * Attempts to perform any automatic actions required during the "New Round" phase.
     * This method initializes the new round by calling {@code places.newTurn()} and
     * checks if the game has ended. It ensures that the initialization occurs only once.
     *
     * @param player the player for whom the automatic action is being attempted
     * @return {@code HasAction.AUTOMATIC_ACTION_DONE} if the new round was initialized,
     *         {@code HasAction.NO_ACTION_POSSIBLE} if the game has ended or initialization already occurred
     */
    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        if (!roundInitialized) {
            boolean gameEnded = places.newTurn();
            roundInitialized = true;
            if (gameEnded) {
                return HasAction.NO_ACTION_POSSIBLE;
            } else {
                return HasAction.AUTOMATIC_ACTION_DONE;
            }
        } else {
            // Round has already been initialized
            return HasAction.NO_ACTION_POSSIBLE;
        }
    }
}