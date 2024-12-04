package sk.uniba.fmph.dcs.player_board;

import junit.framework.TestCase;
import org.junit.Test;

public class PlayerFiguresTest extends TestCase {
private PlayerFigures playerFigures;

    public void setUp(){
        playerFigures = new PlayerFigures();
    }

    public void testAddFigure(){
        playerFigures.addNewFigure();

        assertEquals(5, playerFigures.getFigures());
        assertEquals(6, playerFigures.getTotalFigures());

        playerFigures.newTurn();
        assertEquals(6, playerFigures.getFigures());
        assertEquals(6, playerFigures.getTotalFigures());
    }

    public void testMaximumFigures(){

        for(int i = 0; i < 8; i++){
            playerFigures.addNewFigure();
        }

        assertEquals(5, playerFigures.getFigures());
        assertEquals(10, playerFigures.getTotalFigures());

        playerFigures.newTurn();
        assertEquals(10, playerFigures.getFigures());
        assertEquals(10, playerFigures.getTotalFigures());
    }

    public void testHasFiguresTest(){

        for(int i = 0; i < 3; i++){
            playerFigures.addNewFigure();
        }

        assertEquals(8,playerFigures.getTotalFigures());
        assertEquals(5, playerFigures.getFigures());
        assertFalse(playerFigures.hasFigures(8));
        assertTrue(playerFigures.hasFigures(5));
        assertFalse(playerFigures.hasFigures(-1));
        assertTrue(playerFigures.hasFigures(0));

        playerFigures.newTurn();

        assertTrue(playerFigures.hasFigures(8));
        assertFalse(playerFigures.hasFigures(9));
        assertFalse(playerFigures.hasFigures(-1));
        assertTrue(playerFigures.hasFigures(0));
    }


    public void testTakeFiguresTest(){

        for(int i = 0; i < 3; i++){
            playerFigures.addNewFigure();
        }


        assertEquals(8,playerFigures.getTotalFigures());
        assertFalse(playerFigures.takeFigures(7));
        assertTrue(playerFigures.takeFigures(5));
        assertEquals(0, playerFigures.getFigures());

        playerFigures.newTurn();

        assertEquals(8, playerFigures.getFigures());
        assertTrue(playerFigures.takeFigures(7));
        assertEquals(1, playerFigures.getFigures());
        assertFalse(playerFigures.takeFigures(3));
        assertTrue(playerFigures.takeFigures(1));
        assertEquals(0, playerFigures.getFigures());
    }


}