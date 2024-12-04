package sk.uniba.fmph.dcs.game_board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
import sk.uniba.fmph.dcs.stone_age.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;



public class GameBoardIntegrationTest {
    private Map<Location, InterfaceFigureLocationInternal> map;
    
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private List<Player> players;
    @Before
    public void setUp(){
        players = new ArrayList<>();
        player1 = new Player(new PlayerOrder( 0, 4), GetSomethingChoiceTest.getBoard());
        players.add(player1);

        player2= new Player(new PlayerOrder( 1, 4),GetSomethingChoiceTest.getBoard());
        players.add(player2);

        player3 = new Player(new PlayerOrder( 2, 4), GetSomethingChoiceTest.getBoard());
        players.add(player3);
        player4 = new Player(new PlayerOrder( 3, 4), GetSomethingChoiceTest.getBoard());
        players.add(player4);

        GameBoard gameBoard = new GameBoard(players);
        map = gameBoard.getMapInternal();
    }
    @Test
    public void testInitialization(){
        for(Location location: map.keySet()){
            assertNotNull(map.get(location));
        }
    }
    @Test
    public void testOneResources() throws NoSuchFieldException, IllegalAccessException {
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, map.get(Location.CIVILISATION_CARD1).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, map.get(Location.FOREST).tryToPlaceFigures(player1, 2));
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, map.get(Location.HUNTING_GROUNDS).tryToPlaceFigures(player1, 2));



        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.QUARY).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.BUILDING_TILE2).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.CIVILISATION_CARD1).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.TOOL_MAKER).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.FIELD).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.CLAY_MOUND).tryToPlaceFigures(player1, 1));


        assertEquals(ActionResult.FAILURE, map.get(Location.BUILDING_TILE1).makeAction(player1, new Effect[0], new Effect[0]));
        assertEquals(ActionResult.FAILURE, map.get(Location.CIVILISATION_CARD1).makeAction(player1, new Effect[0], new Effect[0]));

        //Players skips action because he does not have resources to pay
        assertFalse(player1.getPlayerBoard().takeResources(List.of(Effect.WOOD)));

        assertTrue(map.get(Location.CIVILISATION_CARD1).skipAction(player1));

        assertEquals(ActionResult.ACTION_DONE, map.get(Location.FOREST).makeAction(player1, new Effect[0], new Effect[0]));
        //Player got 2 Wood
        assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.WOOD, Effect.WOOD)));

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, map.get(Location.HUNTING_GROUNDS).tryToMakeAction(player1));
        assertEquals(ActionResult.ACTION_DONE, map.get(Location.HUNTING_GROUNDS).makeAction(player1, new Effect[0], new Effect[0]));

        List<Effect> totalFood = new ArrayList<>();
        for(int i=0; i<14; i++){
            totalFood.add(Effect.FOOD);
        }
        //Player should have 14 Food, this test is a bit random. I dont know how to optimize
        assertTrue(player1.getPlayerBoard().takeResources(totalFood));

        assertFalse(newTurn());
    }

    private boolean newTurn(){
        for(Location location: map.keySet()){
            if(map.get(location).newTurn()){
                return true;
            }
        }
        return false;
    }
    @Test
    public void testHutsFieldsWithMorePlayers() throws NoSuchFieldException, IllegalAccessException {
        player1 = new Player(new PlayerOrder(0, 4), GetSomethingChoiceTest.getBoard());
        //Place figure on toolmaker
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, map.get(Location.TOOL_MAKER).tryToPlaceFigures(player1, 1));
        assertTrue(map.get(Location.TOOL_MAKER).placeFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.TOOL_MAKER).tryToPlaceFigures(player2, 1));

        //Place figures on fields
        //Incorrect figures count
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.FIELD).tryToPlaceFigures(player2, 2));
        //Correct count
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, map.get(Location.FIELD).tryToPlaceFigures(player2, 1));

        assertTrue(map.get(Location.FIELD).placeFigures(player2, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.FIELD).tryToPlaceFigures(player3, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.FIELD).tryToPlaceFigures(player3, 2));

        //Place figures on hut
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, map.get(Location.HUT).tryToPlaceFigures(player2, 2));
        assertTrue(map.get(Location.HUT).placeFigures(player2, 2));

        //Make corresponding actions

        assertEquals(ActionResult.ACTION_DONE, map.get(Location.HUT).makeAction(player2, new Effect[0],new Effect[]{Effect.FIGURE}));
        assertEquals(ActionResult.FAILURE, map.get(Location.TOOL_MAKER).makeAction(player3, new Effect[0], new Effect[0]));
        assertEquals(ActionResult.ACTION_DONE, map.get(Location.TOOL_MAKER).makeAction(player1, new Effect[0], new Effect[0]));

        assertEquals(ActionResult.FAILURE, map.get(Location.FIELD).makeAction(player3, new Effect[0], new Effect[0]));
        assertEquals(ActionResult.ACTION_DONE, map.get(Location.FIELD).makeAction(player2, new Effect[0], new Effect[0]));
        assertFalse(newTurn());

        //Check weather player2 got one extra figure
        Field field = PlayerBoardGameBoardFacade.class.getDeclaredField("playerBoard");
        field.setAccessible(true);

        PlayerBoard playerBoard = (PlayerBoard) field.get(player2.getPlayerBoard());
        assertEquals(6, playerBoard.getPlayerFigures().getTotalFigures());
    }

    @Test
    public void testCards() throws NoSuchFieldException, IllegalAccessException {
        player1 = new Player(new PlayerOrder( 0, 4), GetSomethingChoiceTest.getBoard());
        player2= new Player(new PlayerOrder( 1, 4), GetSomethingChoiceTest.getBoard());
        player3 = new Player(new PlayerOrder( 2, 4), GetSomethingChoiceTest.getBoard());
        player4 = new Player(new PlayerOrder( 3, 4), GetSomethingChoiceTest.getBoard());

        player1.getPlayerBoard().giveEffect(List.of(Effect.WOOD));

        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, map.get(Location.CIVILISATION_CARD1).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.NO_ACTION_POSSIBLE, map.get(Location.CIVILISATION_CARD1).tryToPlaceFigures(player2, 1));
        Field field = PlayerBoardGameBoardFacade.class.getDeclaredField("playerBoard");
        field.setAccessible(true);

        PlayerBoard playerBoard = (PlayerBoard) field.get(player1.getPlayerBoard());
        assertEquals(4, playerBoard.getPlayerFigures().getFigures());

        Field cardField = CivilizationCardPlace.class.getDeclaredField("card");
        cardField.setAccessible(true);

        Optional<CivilizationCard> cardOnCivilization_card1 = (Optional<CivilizationCard>)cardField.get(map.get(Location.CIVILISATION_CARD1));

        List<ImmediateEffect> immediateEffects = cardOnCivilization_card1.get().getImmediateEffectType();
        List<Effect> wanted = new ArrayList<>();
        for(ImmediateEffect immediateEffect: immediateEffects){
            switch (immediateEffect){
                case ThrowGold -> wanted.add(Effect.GOLD);
                case ThrowStone -> wanted.add(Effect.STONE);
                case ThrowClay -> wanted.add(Effect.CLAY);
                case ThrowWood -> wanted.add(Effect.WOOD);
                case POINT -> wanted.add(Effect.POINT);
                case WOOD -> wanted.add(Effect.WOOD);
                case CLAY -> wanted.add(Effect.CLAY);
                case STONE -> wanted.add(Effect.STONE);
                case GOLD -> wanted.add(Effect.GOLD);
                case CARD -> wanted.add(Effect.CARD);
                case FOOD -> wanted.add(Effect.FOOD);
                case ArbitraryResource -> wanted.addAll(List.of(Effect.GOLD, Effect.GOLD));
            };
        }

        assertTrue(player1.getPlayerBoard().takeResources(List.of(Effect.WOOD)));

        assertEquals(ActionResult.ACTION_DONE, map.get(Location.CIVILISATION_CARD1).makeAction(player1, List.of(Effect.WOOD).toArray(new Effect[0]), List.of(Effect.GOLD, Effect.GOLD).toArray(new Effect[0])));
        assertTrue(player1.getPlayerBoard().takeResources(wanted));

        assertFalse(newTurn());
    }

}
