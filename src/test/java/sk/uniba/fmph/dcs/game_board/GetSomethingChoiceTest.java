package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GetSomethingChoiceTest {
    private GetSomethingChoice getSomethingChoice;
    private Player player1;
    private Player player2;
    public static Player getCardPlayerMaker(Player player, int orderNum, InterfacePlayerBoardGameBoard board) {
            PlayerOrder order = new PlayerOrder(orderNum, 4);
            player = new Player() {

                public PlayerOrder playerOrder() {
                    return order;
                }
                public InterfacePlayerBoardGameBoard playerBoard() {
                    return board;
                }
            };
            return player;
        }

    public static class GetCardPlayerBoardGameBoard implements InterfacePlayerBoardGameBoard {
        private List<Effect> resources = new ArrayList<>();
        private int figures = 0;
        private List<CivilizationCard> cards = new ArrayList<>();
        private List<EndOfGameEffect> endOfGameEffects = new ArrayList<>();
        private List<Integer> tools = new ArrayList<>();

        public GetCardPlayerBoardGameBoard(int figureCount){
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
    public void setUp(){
        player1 = getCardPlayerMaker(player1, 0, new GetCardPlayerBoardGameBoard(5));
        player2 = getCardPlayerMaker(player2, 1, new GetCardPlayerBoardGameBoard(5));
    }

    @Test
    public void test1(){
        getSomethingChoice = new GetSomethingChoice(2);
        assertTrue(getSomethingChoice.performEffect(player1, Effect.WOOD));
        assertTrue(getSomethingChoice.performEffect(player1, Effect.WOOD));
        assertFalse(getSomethingChoice.performEffect(player1, Effect.WOOD));
    }

    @Test
    public void test2(){
        getSomethingChoice = new GetSomethingChoice(2);
        assertTrue(getSomethingChoice.performEffect(player1, Effect.WOOD));
        assertFalse(getSomethingChoice.performEffect(player2, Effect.WOOD));
        assertTrue(getSomethingChoice.performEffect(player1, Effect.WOOD));
        assertFalse(getSomethingChoice.performEffect(player1, Effect.GOLD));
    }


}
