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

    public static Player playerMaker(Player player, int orderNum, InterfacePlayerBoardGameBoard board) {
        PlayerOrder order = new PlayerOrder(orderNum, 4);
        player = new Player() {
            @Override
            public PlayerOrder playerOrder() {
                return order;
            }

            @Override
            public InterfacePlayerBoardGameBoard playerBoard() {
                return board;
            }
        };
        return player;
    }

    public static class PlayerBoardGameBoard implements InterfacePlayerBoardGameBoard {
        private List<Effect> resources = new ArrayList<>();
        private int figures = 0;
        private List<CivilizationCard> cards = new ArrayList<>();
        private List<EndOfGameEffect> endOfGameEffects = new ArrayList<>();
        private List<Integer> tools = new ArrayList<>();

        public PlayerBoardGameBoard(int figureCount){
            this.figures = figureCount;
        }

        @Override
        public void giveEffect(Collection<Effect> stuff) {
            resources.addAll(stuff);
            for(Effect effect : stuff){
                if(effect.equals(Effect.TOOL)){
                    tools.add(1);
                }
            }
        }

        @Override
        public void giveFigure() {
            figures++;
        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {
            endOfGameEffects.addAll(stuff);
        }

        @Override
        public void giveCard(CivilizationCard card) {
            cards.add(card);
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            if (resources.containsAll(stuff)) {
                resources.removeAll(stuff);
                return true;
            }
            return false;
        }

        @Override
        public boolean takeFigures(int count) {
            if (figures >= count) {
                figures -= count;
                return true;
            }
            return false;
        }

        @Override
        public boolean hasFigures(int count) {
            return figures >= count;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            for (int tool : tools) {
                if (tool >= goal) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            if (idx >= 0 && idx < tools.size()) {
                return Optional.of(tools.remove(idx));
            }
            return Optional.empty();
        }
    }


    @Before
    public void setUp() {
        player1 = playerMaker(player1, 0, new PlayerBoardGameBoard(5));
        player2 = playerMaker(player2, 1,  new PlayerBoardGameBoard(5));
        player3 = playerMaker(player3, 2,  new PlayerBoardGameBoard(5));
        player4 = playerMaker(player4, 3,  new PlayerBoardGameBoard(5));
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
        player1.playerBoard().giveEffect(List.of(Effect.TOOL));
        player2.playerBoard().giveEffect(List.of(Effect.TOOL));
        player3.playerBoard().giveEffect(List.of(Effect.TOOL));
        player4.playerBoard().giveEffect(List.of(Effect.TOOL));

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
