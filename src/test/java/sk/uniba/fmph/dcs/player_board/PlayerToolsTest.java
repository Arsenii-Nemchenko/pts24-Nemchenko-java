package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class PlayerToolsTest {

    private PlayerTools playerTools;


    @Test
    public void tryToAddOneTool() throws NoSuchFieldException, IllegalAccessException {
        playerTools = new PlayerTools();

        playerTools.addTool();
        assertEquals(1, playerTools.getTotalToolsCount());
    }

    @Test
    public void tryToAddSomeTools(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }
        assertEquals(7, playerTools.getTotalToolsCount());
        assertArrayEquals(new int[]{3, 2, 2}, playerTools.getTools());
    }

    @Test
    public void addMoreThanPossibleTools(){

        playerTools = new PlayerTools();

        for(int i = 0; i < 15; i++){
            playerTools.addTool();
        }
        assertEquals(12, playerTools.getTotalToolsCount());
        assertArrayEquals(new int[]{4, 4, 4}, playerTools.getTools());
    }

    @Test
    public void addSingleUseTool(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        playerTools.addSingleUseTool(2);
        assertEquals(9, playerTools.getTotalToolsCount());
        assertArrayEquals(new int[]{3, 2, 2}, playerTools.getTools());

        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        assertEquals(expected,playerTools.getAdditionalTools());

        //Add another SingleUseTool with power 3
        playerTools.addSingleUseTool(3);
        assertEquals(12, playerTools.getRoundToolsCount());
        assertEquals(12, playerTools.getTotalToolsCount());
        assertArrayEquals(new int[]{3, 2, 2}, playerTools.getTools());

        expected.add(3);
        assertEquals(expected,playerTools.getAdditionalTools());

    }

    @Test
    public void useTool(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        playerTools.useTool(2);
        assertEquals(7, playerTools.getTotalToolsCount());
        assertEquals(5, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2}, playerTools.getTools());
    }

    @Test
    public void useSingleUseTool(){
        playerTools = new PlayerTools();

        for(int i = 0; i < 7; i++){
            playerTools.addTool();
        }

        playerTools.addSingleUseTool(2);
        playerTools.addSingleUseTool(3);
        assertEquals(12, playerTools.getTotalToolsCount());
        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        assertEquals(expected,playerTools.getAdditionalTools());


        playerTools.useTool(4);
        expected.remove(1);

        assertEquals(expected,playerTools.getAdditionalTools());
        assertEquals(9, playerTools.getTotalToolsCount());
        assertEquals(9, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2}, playerTools.getTools());

        playerTools.useTool(3);
        expected.remove(0);

        assertEquals(expected,playerTools.getAdditionalTools());
        assertEquals(7, playerTools.getTotalToolsCount());
        assertEquals(7, playerTools.getRoundToolsCount());
        assertArrayEquals(new int[]{3, 2, 2}, playerTools.getTools());
    }


}