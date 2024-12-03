package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameBoardIntegrationTest {
    private GameBoard gameBoard;
    private Player player1 = null;
    private Player player2 = null;
    private Player player3 = null;
    private Player player4 = null;
    @Before
    public void setUp(){
        List<Player> players = new ArrayList<>();
        Player player1 = null;
        players.add(GetSomethingChoiceTest.getCardPlayerMaker(player1, 0, new GetSomethingChoiceTest.GetCardPlayerBoardGameBoard(5)));
        Player player2 = null;
        players.add(GetSomethingChoiceTest.getCardPlayerMaker(player2, 1, new GetSomethingChoiceTest.GetCardPlayerBoardGameBoard(5)));
        Player player3 = null;
        players.add(GetSomethingChoiceTest.getCardPlayerMaker(player3, 2, new GetSomethingChoiceTest.GetCardPlayerBoardGameBoard(5)));
        Player player4 = null;
        players.add(GetSomethingChoiceTest.getCardPlayerMaker(player4, 3, new GetSomethingChoiceTest.GetCardPlayerBoardGameBoard(5)));

        gameBoard = new GameBoard(players);
    }

    @Test
    public void test1(){
        FigureLocationAdaptor adaptor = new FigureLocationAdaptor()
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, gameBoard.getMap().get(Location.BUILDING_TILE1).tryToPlaceFigures(player1, 1));
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, gameBoard.getMap().get(Location.FOREST).tryToPlaceFigures(player1, 2));

    }
}
