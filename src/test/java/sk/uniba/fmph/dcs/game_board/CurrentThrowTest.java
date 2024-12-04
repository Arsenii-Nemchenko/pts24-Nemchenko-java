package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.List;

import static org.junit.Assert.*;

public class CurrentThrowTest {
    private CurrentThrow currentThrow;
    private Player player1;
    private Player player2;

    @Before
    public void setUp(){
        player1 = new Player(new PlayerOrder(0, 2), GetSomethingChoiceTest.getBoard());
        player2 = new Player(new PlayerOrder(1, 2), GetSomethingChoiceTest.getBoard());
        currentThrow = new CurrentThrow(player1, Effect.GOLD, 1);
    }

    @Test
    public void test1(){
        currentThrow.initiate(player2, Effect.GOLD, 4);
        player2.getPlayerBoard().giveEffect(List.of(Effect.TOOL));
        assertTrue(currentThrow.canUseTools());
        assertTrue(currentThrow.useTool(0));
        assertFalse(currentThrow.useTool(1));
        assertTrue(currentThrow.finishUsingTools());
    }

    @Test
    public void test2(){
        currentThrow.initiate(player1, Effect.GOLD, 2);
        assertFalse(currentThrow.canUseTools());
        assertFalse(currentThrow.useTool(0));
        assertFalse(currentThrow.useTool(1));
        assertTrue(currentThrow.finishUsingTools());
    }
}
