package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class PlayerBoardGameBoardFacade implements InterfaceFeedTribe, InterfaceNewTurn, InterfacePlayerBoardGameBoard {
    private final PlayerBoard playerBoard;

    /**
     * Constructs a {@code PlayerBoardGameBoardFacade} with the specified player board.
     *
     * @param playerBoard
     *            the player board to be used for this facade
     */
    public PlayerBoardGameBoardFacade(final PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Feeds the tribe if there is enough food available.
     *
     * @return {@code true} if the tribe is fed, {@code false} otherwise.
     */
    @Override
    public boolean feedTribeIfEnoughFood() {
        return this.playerBoard.getTribeFedStatus().feedTribeIfEnoughFood();
    }

    /**
     * Feeds the tribe using the provided resources.
     *
     * @param resources
     *            The collection of resources to use for feeding the tribe.
     *
     * @return {@code true} if the tribe is successfully fed, {@code false} otherwise.
     */
    @Override
    public boolean feedTribe(final Collection<Effect> resources) {
        return this.playerBoard.getTribeFedStatus().feedTribe(resources);
    }

    /**
     * Indicates that the tribe should not be fed this turn, applying any penalties if applicable.
     *
     * @return {@code true} if the tribe feeding status is set, {@code false} otherwise.
     */
    @Override
    public boolean doNotFeedThisTurn() {
        boolean fed = this.playerBoard.getTribeFedStatus().doNotFeedThisTurn();
        if (fed) {
            this.playerBoard.addPoints(-10);
        }
        return fed;
    }

    /**
     * Checks if the tribe has been fed.
     *
     * @return {@code true} if the tribe is fed, {@code false} otherwise.
     */
    @Override
    public boolean isTribeFed() {
        return this.playerBoard.getTribeFedStatus().isTribeFed();
    }

    @Override
    public boolean newTurn() {
        this.playerBoard.newTurn();
        return false;
    }

    /**
     * Provides resources to the player.
     *
     * @param effects The array of effects to give to the player.
     */
    @Override
    public void giveEffect(final Collection<Effect> effects) {
        this.playerBoard.getPlayerResourcesAndFood().giveResources(effects);
    }

    /**
     * Provides end-of-game effects to the player's civilization cards.
     *
     * @param endEffects The array of end-of-game effects to give to the player.
     */
    @Override
    public void giveEndOfGameEffect(final Collection<EndOfGameEffect> endEffects) {
        this.playerBoard.getPlayerCivilisationCards().addEndOfGameEffects(endEffects.toArray(new EndOfGameEffect[0]));
    }

    /**
     * Takes the specified resources of the player
     *
     * @param stuff The array of resources to be taken.
     */
    @Override
    public boolean takeResources(final Collection<Effect> stuff) {
        if (this.playerBoard.getPlayerResourcesAndFood().hasResources(stuff)) {
            this.playerBoard.getPlayerResourcesAndFood().takeResources(stuff);
            return true;
        }
        return false;
    }

    /**
     * Adds a new figure to the player's collection.
     */
    @Override
    public void giveFigure() {
        this.playerBoard.getPlayerFigures().addNewFigure();
    }

    /**
     * Takes the specified number of figures from the player if they have enough.
     *
     * @param count The number of figures to take.
     */
    @Override
    public boolean takeFigures(final int count) {
        if (this.playerBoard.getPlayerFigures().hasFigures(count)) {
            this.playerBoard.getPlayerFigures().takeFigures(count);
            return true;
        }
        return false;
    }

    /**
     * Checks if the player has at least the specified number of figures.
     *
     * @param count The number of figures to check for
     */
    @Override
    public boolean hasFigures(final int count) {
        return this.playerBoard.getPlayerFigures().hasFigures(count);
    }

    /**
     * Checks if the player has sufficient tools to meet the specified goal.
     * @param goal The goal to check against the player's tools.
     */
    @Override
    public boolean hasSufficientTools(final int goal) {
        return this.playerBoard.getPlayerTools().hasSufficientTools(goal);
    }

    /** @param idx - index of the tool we use */
    @Override
    public Optional<Integer> useTool(final int idx) {
        Optional<Integer> value = this.playerBoard.getPlayerTools().useTool(idx);
        return value;
    }

    public InterfaceGetState getPlayerBoard() {
        return playerBoard;
    }
}