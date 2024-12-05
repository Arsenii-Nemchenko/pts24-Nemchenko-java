package sk.uniba.fmph.dcs.game_board;

import org.apache.commons.collections4.Unmodifiable;
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

    //Resolves a problem with card chain
    public void setNextCard(CivilizationCardPlace next) {
        nextCardPlace = next;
    }
    //Places figures on the card if the conditions are right
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

    //Tries to place figure
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (card.isPresent() && placeFigures(player, count)) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    //Makes action if the conditions are right
    //InputResources contains resources to pay,
    // outputResources contains resources to get in case of getSomethingChoice
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

    //Skips action
    @Override
    public boolean skipAction(Player player) {
        if (!player.getPlayerOrder().equals(figures)) {
            return false;
        }

        figures = null;
        return true;
    }

    //Tries to make action
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (!player.getPlayerOrder().equals(figures)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    //If all the action are taken -> newTurn
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

    //Shifts cards to the right
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

    //State of this cardPlace class
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
