package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CivilizationCardPlace implements InterfaceFigureLocationInternal, InterfaceGetState {
    private final int requiredResources;
    private Optional<CivilizationCard> card;
    private PlayerOrder figures;
    private final CivilizationCardDeck deck;
    private CivilizationCardPlace nextCardPlace;

    private GetCard getCardImmediateEffectPerformer;
    private AllPlayersTakeReward allPlayersTakeARewardEffectPerformer;
    private GetSomethingFixed getSomethingFixedPerformer;

    private GetSomethingChoice getSomethingChoicePerformer;
    private GetSomethingThrow getSomethingThrow;

    /**
     * Constructs a CivilizationCardPlace object.
     *
     * @param previousCard                      the previous card place in the chain
     * @param deck                              the deck of civilization cards
     * @param requiredResources                 the number of resources required to interact with the card
     * @param getSomethingChoicePerformer       performer for "get something by choice" effect
     * @param getSomethingThrow                 performer for "throw something" effect
     * @param getCardImmediateEffectPerformer   performer for "get card immediately" effect
     * @param allPlayersTakeARewardEffectPerformer performer for "all players take a reward" effect
     * @param getSomethingFixedPerformer        performer for "get something fixed" effect
     */
    public CivilizationCardPlace(CivilizationCardPlace previousCard, CivilizationCardDeck deck,
                                 int requiredResources, GetSomethingChoice getSomethingChoicePerformer,
                                 GetSomethingThrow getSomethingThrow, GetCard getCardImmediateEffectPerformer,
                                 AllPlayersTakeReward allPlayersTakeARewardEffectPerformer, GetSomethingFixed getSomethingFixedPerformer
    ) {
        this.requiredResources = requiredResources;
        this.nextCardPlace = null;
        this.deck = deck;
        if(previousCard != null) {
            previousCard.setNextCard(this);
        }
        this.card = deck.getTop();
        this.getCardImmediateEffectPerformer = getCardImmediateEffectPerformer;
        this.getSomethingChoicePerformer = getSomethingChoicePerformer;
        this.getSomethingThrow = getSomethingThrow;
        this.allPlayersTakeARewardEffectPerformer = allPlayersTakeARewardEffectPerformer;
        this.getSomethingFixedPerformer = getSomethingFixedPerformer;

    }

    /**
     * Sets the next card place in the chain.
     *
     * @param next the next card place
     */
    public void setNextCard(CivilizationCardPlace next) {
        nextCardPlace = next;
    }

    /**
     * Places a figure on the card if conditions are met.
     *
     * @param player      the player placing the figure
     * @param figureCount the number of figures to place
     * @return {@code true} if the figures were placed successfully, otherwise {@code false}
     */
    @Override
    public boolean placeFigures(Player player,int figureCount) {

        if (figureCount != 1 || !player.getPlayerBoard().hasFigures(figureCount)) {
            return false;
        }

        if (figures != null) {
            return false;
        }

        figures = player.getPlayerOrder();
        player.getPlayerBoard().takeFigures(1);
        return true;
    }

    /**
     * Attempts to place figures on the card.
     *
     * @param player the player attempting to place figures
     * @param count  the number of figures to place
     * @return a {@link HasAction} indicating the result of the attempt
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (card.isPresent() && placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

     /**
     * Makes an action on the card if conditions are met.
     *
     * @param player         the player performing the action
     * @param inputResources the resources to pay
     * @param outputResources the resources to receive in case of choice
     * @return an {@link ActionResult} indicating the result of the action
     */
    @Override
    public ActionResult makeAction(Player player, Effect[] inputResources, Effect[] outputResources) {
        List<Effect> input = Arrays.asList(inputResources);

        if (!figures.equals(player.getPlayerOrder())) {
            return ActionResult.FAILURE;
        }

        if (input.size() != requiredResources) {
            return ActionResult.FAILURE;
        }

        for (Effect effect : input) {
            if (!effect.isResource()) {
                return ActionResult.FAILURE;
            }
        }
        if(!player.getPlayerBoard().takeResources(List.of(inputResources))){
            return ActionResult.FAILURE;
        }

        List<ImmediateEffect> immediateEffects = card.get().getImmediateEffectType();

        for(ImmediateEffect immediateEffect: immediateEffects){
            boolean result = switch (immediateEffect){
                case ThrowGold -> getSomethingThrow.performEffect(player, Effect.GOLD);
                case ThrowStone -> getSomethingThrow.performEffect(player, Effect.STONE);
                case ThrowClay -> getSomethingThrow.performEffect(player, Effect.CLAY);
                case ThrowWood -> getSomethingThrow.performEffect(player, Effect.WOOD);
                case POINT -> getSomethingFixedPerformer.performEffect(player, Effect.POINT);
                case WOOD -> getSomethingFixedPerformer.performEffect(player, Effect.WOOD);
                case CLAY -> getSomethingFixedPerformer.performEffect(player, Effect.CLAY);
                case STONE -> getSomethingFixedPerformer.performEffect(player, Effect.STONE);
                case GOLD -> getSomethingFixedPerformer.performEffect(player, Effect.GOLD);
                case CARD -> getCardImmediateEffectPerformer.performEffect(player, Effect.CARD);
                case FOOD -> getSomethingFixedPerformer.performEffect(player, Effect.FOOD);
                case ArbitraryResource -> performChoice(outputResources, player);
                case AllPlayersTakeReward -> allPlayersTakeARewardEffectPerformer.performEffect(player, Effect.BUILDING);
                default -> false;
            };
            if(!result){
                return ActionResult.FAILURE;
            }

        }

        figures = null;
        card = Optional.empty();
        return ActionResult.ACTION_DONE;
    }

    private boolean performChoice(Effect[] output, Player player){
        for(Effect outputResource: output){
            if(!getSomethingChoicePerformer.performEffect(player, outputResource)){
                return false;
            }
        }
        return true;
    }

    /**
     * Skips the action for the current player.
     *
     * @param player the player skipping the action
     * @return {@code true} if the action was skipped successfully, otherwise {@code false}
     */
    @Override
    public boolean skipAction(Player player) {
        if (!player.getPlayerOrder().equals(figures)) {
            return false;
        }

        figures = null;
        return true;
    }

    /**
     * Checks if the player can make an action.
     *
     * @param player the player to check
     * @return a {@link HasAction} indicating whether an action can be made
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (!player.getPlayerOrder().equals(figures)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    /**
     * Prepares for a new turn by shifting cards if all actions are taken.
     *
     * @return {@code true} if the turn is ready, otherwise {@code false}
     */
    @Override
    public boolean newTurn() {
        if (figures != null) {
            return false;
        }

        this.card = this.getNextCard();
        if(card.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves the next card in the chain or from the deck.
     *
     * @return an {@link Optional} containing the next card, if available
     */
    public Optional<CivilizationCard> getNextCard(){
        if(card.isPresent()){
            Optional<CivilizationCard> temp = card;
            card = Optional.empty();
            return temp;
        }else{
            if(nextCardPlace != null){
                card = nextCardPlace.getNextCard();
            }else{
                card = deck.getTop();
            }
        }
        return card;

    }

    /**
     * Returns the state of this card place as a JSON string.
     *
     * @return a JSON representation of the state
     */
    @Override
    public String state() {
        Map<String, Object> map = Map.of(
                "requiredResources", requiredResources,
                "card", card,
                "figures", figures,
                "deck", deck,
                "nextCardPlace", nextCardPlace);
        return new JSONObject(map).toString();
    }
}
