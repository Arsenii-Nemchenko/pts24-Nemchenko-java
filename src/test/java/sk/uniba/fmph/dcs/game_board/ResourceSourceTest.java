package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

import static org.junit.Assert.*;

public class ResourceSourceTest {
    private ResourceSource resourceSource;

    private Player player1;
    private Player player2;
    private  Player player3;
    private Player player4;
    


    @Before
    public void setUp() {
        player1 = new Player(new PlayerOrder( 0, 4), GetSomethingChoiceTest.getBoard());
        player2 = new Player(new PlayerOrder( 1, 4), GetSomethingChoiceTest.getBoard());
        player3 = new Player(new PlayerOrder( 2, 4), GetSomethingChoiceTest.getBoard());
        player4 = new Player(new PlayerOrder( 3, 4), GetSomethingChoiceTest.getBoard());
    }

    @Test
    public void testHunting(){
        resourceSource = new ResourceSource(
                "Hunting grounds", Effect.FOOD, Integer.MAX_VALUE,
                Integer.MAX_VALUE, new CurrentThrow(player1, Effect.FOOD, 1)
        );

        assertTrue(resourceSource.placeFigures(player1, 5));
        assertTrue(resourceSource.placeFigures(player2, 5));
        assertTrue(resourceSource.placeFigures(player3, 5));
        assertTrue(resourceSource.placeFigures(player4, 5));

        assertFalse(resourceSource.placeFigures(player1, 1));
        assertFalse(resourceSource.placeFigures(player2, 1));
        assertFalse(resourceSource.placeFigures(player3, 1));
        assertFalse(resourceSource.placeFigures(player4, 1));

        assertFalse(resourceSource.newTurn());

        //Give players tools
        player1.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        player2.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        player3.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        player4.getPlayerBoard().giveEffect(List.of(Effect.TOOL));

        //Players actions
        Effect[] output = new Effect[0];
        Effect[] input = {Effect.TOOL};

        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player1, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player2, input, output));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player3, input, output));

        assertFalse(resourceSource.newTurn());

        Effect[] badInput = {Effect.STONE};
        assertEquals(ActionResult.FAILURE, resourceSource.makeAction(player4, badInput, output));

        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player4, input, output));

        assertTrue(resourceSource.newTurn());
    }

    @Test
    public void testRiverAndLessPlayers(){
        resourceSource = new ResourceSource(
                "River", Effect.GOLD, 7,
                2, new CurrentThrow(player1, Effect.GOLD, 1)
        );

        assertTrue(resourceSource.placeFigures(player1, 3));
        assertTrue(resourceSource.placeFigures(player2, 2));


        assertFalse(resourceSource.placeFigures(player3, 3));
        assertFalse(resourceSource.placeFigures(player3, 2));

        assertFalse(resourceSource.newTurn());

        //Players actions
        Effect[] output = new Effect[0];
        Effect[] input = {Effect.TOOL};

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, resourceSource.tryToMakeAction(player1));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player1, input, output));

        assertFalse(resourceSource.newTurn());

        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, resourceSource.tryToMakeAction(player2));
        assertEquals(ActionResult.ACTION_DONE, resourceSource.makeAction(player2, input, output));


        assertTrue(resourceSource.newTurn());
    }
}
