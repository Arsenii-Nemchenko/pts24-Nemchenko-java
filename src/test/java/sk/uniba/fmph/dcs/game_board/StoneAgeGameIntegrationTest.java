package sk.uniba.fmph.dcs.game_board;



import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class StoneAgeGameIntegrationTest {

    private StoneAgeGame game;
    private int playerId1;
    private int playerId2;
    private int playerId3;
    private int playerId4;
    private List<Effect> effects;

    @Before
    public void setUp() {
        game = new StoneAgeGame(4);
        playerId1 = 0;
        playerId2 = 1;
        playerId3 = 2;
        playerId4 = 3;

        effects = List.of(Effect.WOOD, Effect.FOOD);
    }

    @Test
    public void test1_placeFigures() {
        assertTrue(game.placeFigures(playerId1, Location.FIELD, 1));
        assertTrue(game.placeFigures(playerId1, Location.HUT, 2));
        assertTrue(game.placeFigures(playerId1, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 1));

        assertFalse(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 1));

        assertTrue(game.placeFigures(playerId2, Location.HUNTING_GROUNDS, 1));
        assertTrue(game.placeFigures(playerId2, Location.RIVER, 3));
        assertTrue(game.placeFigures(playerId2, Location.CIVILISATION_CARD1, 1));

        assertFalse(game.placeFigures(playerId2, Location.CIVILISATION_CARD1, 10));

        assertFalse(game.placeFigures(playerId3, Location.BUILDING_TILE1, 2));
        assertTrue(game.placeFigures(playerId3, Location.BUILDING_TILE1, 1));
        assertTrue(game.placeFigures(playerId3, Location.QUARY, 4));
        assertTrue(game.placeFigures(playerId3, Location.QUARY, 5));

        assertTrue(game.placeFigures(playerId4, Location.FOREST, 1));
        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 3));
        assertTrue(game.placeFigures(playerId4, Location.BUILDING_TILE4, 1));
    }

    @Test
    public void test2_placeFigures_InvalidPlayer() {
        int invalidPlayerId = 99;
        assertFalse(game.placeFigures(invalidPlayerId, Location.HUNTING_GROUNDS, 2));
        assertFalse(game.placeFigures(invalidPlayerId, Location.BUILDING_TILE4, 2));
        assertFalse(game.placeFigures(invalidPlayerId, Location.TOOL_MAKER, 2));
        assertFalse(game.placeFigures(invalidPlayerId, Location.CIVILISATION_CARD1, 1));
    }

    @Test
    public void test1_makeAction() throws NoSuchFieldException, IllegalAccessException {
        assertTrue(game.makeAction(playerId1, Location.FIELD, List.of(), List.of(Effect.FIELD)));
        assertTrue(game.makeAction(playerId1, Location.HUT, List.of(), List.of(Effect.FIGURE)));
        assertFalse(game.makeAction(playerId1, Location.HUT, List.of(), List.of(Effect.WOOD)));
        assertTrue(game.makeAction(playerId1, Location.TOOL_MAKER, List.of(), List.of(Effect.TOOL)));
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD)));

        assertFalse(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD)));

        assertTrue(game.makeAction(playerId2, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD)));
        assertTrue(game.makeAction(playerId2, Location.RIVER, List.of(), List.of(Effect.GOLD)));

        Field players = StoneAgeGame.class.getDeclaredField("players");
        players.setAccessible(true);

        List<Player> players1 = (List<Player>)players.get(game);
        assertFalse(players1.get(2).getPlayerBoard().takeResources(List.of(Effect.WOOD)));

        assertTrue(game.skipAction(playerId2, Location.CIVILISATION_CARD1));

        Field gameBoardField = StoneAgeGame.class.getDeclaredField("gameBoard");
        gameBoardField.setAccessible(true);

        GameBoard gameBoard = (GameBoard) gameBoardField.get(game);

        Field tileField = BuildingTile.class.getDeclaredField("buildings");
        tileField.setAccessible(true);

        Stack<Building> buildingTile = (Stack<Building>)tileField.get(gameBoard.getMapInternal().get(Location.BUILDING_TILE1));

        SimpleBuilding simpleBuilding = (SimpleBuilding)buildingTile.peek();
        Field simpleBuildingResources = SimpleBuilding.class.getDeclaredField("requiredResources");
        simpleBuildingResources.setAccessible(true);
        ArrayList<Effect> required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);

        assertFalse(players1.get(3).getPlayerBoard().takeResources(required));

        assertFalse(game.makeAction(playerId3, Location.BUILDING_TILE1,  required, List.of(Effect.POINT)));
        assertTrue(game.placeFigures(playerId3, Location.QUARY, 4));
        assertTrue(game.placeFigures(playerId3, Location.QUARY, 5));

        assertTrue(game.placeFigures(playerId4, Location.FOREST, 1));
        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 3));
        assertTrue(game.placeFigures(playerId4, Location.BUILDING_TILE4, 1));
    }

}
