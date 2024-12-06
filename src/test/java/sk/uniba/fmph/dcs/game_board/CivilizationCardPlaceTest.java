package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class CivilizationCardPlaceTest {
    private CivilizationCardDeck deck;
    private CivilizationCardDeck getDeck(){
        List<CivilizationCard> allCards = new ArrayList<>();
        // Dice roll (10 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.WRITING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.SUNDIAL)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.POTTERY)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TRANSPORT)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)));

        // Food (7 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.MEDICINE)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.POTTERY)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.WEAVING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.WEAVING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.BUILDER)));

        // Resource (5 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE, ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.TRANSPORT)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.CLAY), Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.GOLD), Arrays.asList(EndOfGameEffect.SHAMAN)));

        // Resources with dice roll (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowStone), Arrays.asList(EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowGold), Arrays.asList(EndOfGameEffect.ART)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.WOOD), Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));

        // Victory points (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.MUSIC)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.MUSIC)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));

        // Extra tool tile (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Tool), Arrays.asList(EndOfGameEffect.ART)));

        // Agriculture (2 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Field), Arrays.asList(EndOfGameEffect.SUNDIAL)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Field), Arrays.asList(EndOfGameEffect.FARMER)));

        // Civilization card for final scoring (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.CARD), Arrays.asList(EndOfGameEffect.WRITING)));

        // One-use tool (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool2), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool3), Arrays.asList(EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool4), Arrays.asList(EndOfGameEffect.BUILDER)));

        // Any 2 resources (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource), Arrays.asList(EndOfGameEffect.MEDICINE)));
        Collections.shuffle(allCards);
        return new CivilizationCardDeck(allCards);
    }

    private CivilizationCardPlace place1;
    private CivilizationCardPlace place2;
    private CivilizationCardPlace place3;
    private CivilizationCardPlace place4;

    private Player player1;
    private Player player2;
    private Player player3;
    private  Player player4;

    @Before
    public void setUp(){
        player1 = new Player(new PlayerOrder( 0, 4), GetSomethingChoiceTest.getBoard());
        player2 = new Player(new PlayerOrder( 1, 4), GetSomethingChoiceTest.getBoard());
        player3 = new Player(new PlayerOrder( 2, 4), GetSomethingChoiceTest.getBoard());
        player4 = new Player(new PlayerOrder( 3, 4), GetSomethingChoiceTest.getBoard());


        CurrentThrow currentThrow = new CurrentThrow(player1, Effect.WOOD, 1);
        GetSomethingThrow getThrow = new GetSomethingThrow(currentThrow);
        GetSomethingFixed getFixed = new GetSomethingFixed();
        GetSomethingChoice getChoice = new GetSomethingChoice(2);
        deck = getDeck();
        GetCard getCard = new GetCard(deck);
        AllPlayersTakeReward getReward = new AllPlayersTakeReward();


        place1 = new CivilizationCardPlace(
                null, deck, 1,
                getChoice, getThrow, getCard, getReward, getFixed
        );
        place2 = new CivilizationCardPlace(
                place1, deck, 2,
                getChoice, getThrow, getCard, getReward, getFixed
        );
        place3 = new CivilizationCardPlace(
                place2, deck, 3,
                getChoice, getThrow, getCard, getReward, getFixed
        );
        place4 = new CivilizationCardPlace(
                place3, deck, 4,
                getChoice, getThrow, getCard, getReward, getFixed
        );
    }
    @Test
    public void test1() {

        assertFalse(place4.placeFigures(player1, 2));
        assertTrue(place4.placeFigures(player1, 1));
        assertFalse(place4.placeFigures(player1, 1));

        assertFalse(place4.placeFigures(player2, 2));
        assertFalse(place4.placeFigures(player2, 1));

        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, place3.tryToPlaceFigures(player3,1));
        assertFalse(place3.placeFigures(player3, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, place4.tryToPlaceFigures(player3, 1));


        assertTrue(place2.placeFigures(player3, 1));
        assertFalse(place2.placeFigures(player3, 1));

        assertFalse(place1.newTurn());
        assertFalse(place2.newTurn());
        assertFalse(place3.newTurn());
        assertFalse(place4.newTurn());
    }
    @Test
    public void testMakeAction() throws NoSuchFieldException {

        assertEquals(HasAction.NO_ACTION_POSSIBLE, place4.tryToMakeAction(player1));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, place4.tryToPlaceFigures(player1,1));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, place4.tryToMakeAction(player1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, place4.tryToMakeAction(player3));

        //Input contains resources player paid for this place
        Effect[] badInput1 = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.FOOD};
        Effect[] badInput2 = new Effect[]{Effect.STONE,Effect.STONE};
        Effect[] input = new Effect[]{Effect.STONE,Effect.STONE,Effect.STONE, Effect.CLAY};
        player1.getPlayerBoard().giveEffect(Arrays.stream(input).toList());

        assertEquals(ActionResult.FAILURE, place4.makeAction(player3, input, new Effect[0]));
        assertEquals(ActionResult.FAILURE, place4.makeAction(player1, badInput1, new Effect[0]));
        assertEquals(ActionResult.FAILURE, place4.makeAction(player1, badInput2, new Effect[0]));

        assertEquals(ActionResult.ACTION_DONE, place4.makeAction(player1, input, new Effect[]{Effect.GOLD, Effect.GOLD}));
        assertFalse(place4.skipAction(player3));
        assertFalse(place4.skipAction(player1));

        assertTrue(place3.placeFigures(player3, 1));
        assertFalse(place3.skipAction(player1));
        assertTrue(place3.skipAction(player3));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, place3.tryToMakeAction(player3));

        assertFalse(place1.newTurn());
        assertFalse(place2.newTurn());
        assertFalse(place3.newTurn());
        assertFalse(place4.newTurn());

        Field place1Card = place1.getClass().getDeclaredField("card");
        Field place2Card = place2.getClass().getDeclaredField("card");
        Field place3Card = place3.getClass().getDeclaredField("card");
        Field place4Card = place4.getClass().getDeclaredField("card");

        assertNotEquals(Optional.empty(), place1Card);
        assertNotEquals(Optional.empty(), place2Card);
        assertNotEquals(Optional.empty(), place3Card);
        assertNotEquals(Optional.empty(), place4Card);

    }
    
}
