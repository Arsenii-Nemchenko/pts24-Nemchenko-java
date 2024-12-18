package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Player;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToolMakerHutFieldsTest {
    private ToolMakerHutFields toolMakerHutFields;
    private Player player1;

    private Player player2;
    private Player player3;
    private Player player4;

    @Before
    public void setUp(){
        player1 = new Player(new PlayerOrder(0, 4) , GetSomethingChoiceTest.getBoard());
        player1 = new Player(new PlayerOrder(1, 4) , GetSomethingChoiceTest.getBoard());
        player1 = new Player(new PlayerOrder(2, 4) , GetSomethingChoiceTest.getBoard());
        player1 = new Player(new PlayerOrder(3, 4) , GetSomethingChoiceTest.getBoard());
        toolMakerHutFields = new ToolMakerHutFields(4);
    }


    @Test
    public void test1(){
        assertTrue(toolMakerHutFields.canPlaceOnToolMaker(player1));
        assertTrue(toolMakerHutFields.placeOnToolMaker(player1));


        assertFalse(toolMakerHutFields.canPlaceOnToolMaker(player2));

        assertTrue(toolMakerHutFields.canPlaceOnHut(player2));
        assertTrue(toolMakerHutFields.placeOnHut(player2));

        assertFalse(toolMakerHutFields.canPlaceOnToolMaker(player3));

        assertTrue(toolMakerHutFields.canPlaceOnFields(player3));
        assertTrue(toolMakerHutFields.placeOnFields(player3));

        assertFalse(toolMakerHutFields.placeOnFields(player4));


        //Try to make action as other player
        assertFalse(toolMakerHutFields.actionToolMaker(player4));
        assertFalse(toolMakerHutFields.actionHut(player4));
        assertFalse(toolMakerHutFields.actionFields(player4));
        //Player on toolMaker action
        assertTrue(toolMakerHutFields.actionToolMaker(player1));
        assertTrue(toolMakerHutFields.actionHut(player2));
        assertTrue(toolMakerHutFields.actionFields(player3));


        toolMakerHutFields.newTurn();
    }


}
