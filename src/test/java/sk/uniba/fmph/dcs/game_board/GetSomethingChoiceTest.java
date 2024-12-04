package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardGameBoardFacade;
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

    public static InterfacePlayerBoardGameBoard getBoard(){
        return new PlayerBoardGameBoardFacade(new PlayerBoard());
    }

    @Before
    public void setUp(){
        player1 = new Player(new PlayerOrder(0, 2) ,getBoard());
        player1 = new Player(new PlayerOrder(1, 2) , getBoard());
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
