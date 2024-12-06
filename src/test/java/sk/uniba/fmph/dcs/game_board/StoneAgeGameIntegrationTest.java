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

    @Before
    public void setUp() {
        game = new StoneAgeGame(4);
        playerId1 = 0;
        playerId2 = 1;
        playerId3 = 2;
        playerId4 = 3;
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
        assertFalse(game.placeFigures(playerId3, Location.QUARY, 5));

        assertTrue(game.placeFigures(playerId4, Location.FOREST, 1));
        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 3));
        assertTrue(game.placeFigures(playerId4, Location.BUILDING_TILE4, 1));
    }

    @Test
    public void test2_placeFigures_InvalidPlayer() {
        int invalidPlayerId = 99;
        assertFalse(game.placeFigures(invalidPlayerId, Location.HUNTING_GROUNDS, 2));
        assertFalse(game.placeFigures(invalidPlayerId, Location.BUILDING_TILE4, 1));
        assertFalse(game.placeFigures(invalidPlayerId, Location.TOOL_MAKER, 1));
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
        assertTrue(game.makeAction(playerId3, Location.QUARY, List.of(), List.of(Effect.STONE, Effect.STONE, Effect.STONE, Effect.STONE)));
        assertFalse(game.makeAction(playerId3, Location.QUARY, List.of(), List.of(Effect.STONE, Effect.STONE, Effect.STONE, Effect.STONE, Effect.STONE)));


        assertFalse(game.makeAction(playerId3, Location.FOREST, List.of(), List.of(Effect.WOOD)));
        assertTrue(game.makeAction(playerId4, Location.FOREST, List.of(), List.of(Effect.WOOD)));
        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD)));
        assertFalse(game.makeAction(playerId4, Location.BUILDING_TILE4, List.of(Effect.WOOD, Effect.STONE, Effect.GOLD, Effect.STONE, Effect.WOOD, Effect.WOOD,Effect.WOOD), List.of(Effect.POINT)));
        //Player does not have resources to pay for building
        assertTrue(game.skipAction(playerId4, Location.BUILDING_TILE4));

        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
    }

    @Test
    public void test_make_all_players_take_a_reward(){
        assertTrue(game.makeAllPlayersTakeARewardChoice(playerId1, Effect.FOOD));
    }

    @Test
    public void testUntilGame_End() throws IllegalAccessException, NoSuchFieldException {
        //As I removed randomization of tiles and cards I can predict what resources I need to build a building
        //I guess a player would know what resources are needed looking at GUI, but since we dont have any type of
        //Communication with gameBoard(I mean at least what resources I need to pay as player)
        //The best I can do is removing shuffle of tiles
        //Now required resources became predictable

        //Also I dont know how to correctly test classes with random outputs(like currentThrow)


        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.FOREST, 5));
        assertTrue(game.placeFigures(playerId1, Location.QUARY, 1));


        assertTrue(game.placeFigures(playerId2, Location.FOREST, 1));
        assertTrue(game.placeFigures(playerId2, Location.CLAY_MOUND, 2));
        assertTrue(game.placeFigures(playerId2, Location.QUARY, 2));

        assertTrue(game.placeFigures(playerId3, Location.CLAY_MOUND, 3));
        assertTrue(game.placeFigures(playerId3, Location.QUARY, 2));

        assertTrue(game.placeFigures(playerId4, Location.RIVER, 5));

        //Make actions
        assertTrue(game.makeAction(playerId1, Location.FOREST, List.of(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD)));
        assertTrue(game.makeAction(playerId1, Location.QUARY, List.of(), List.of(Effect.STONE)));


        assertTrue(game.makeAction(playerId2, Location.FOREST, List.of(), List.of(Effect.WOOD)));
        assertTrue(game.makeAction(playerId2, Location.CLAY_MOUND, List.of(), List.of(Effect.CLAY)));
        assertTrue(game.makeAction(playerId2, Location.QUARY, List.of(), List.of(Effect.STONE)));

        assertTrue(game.makeAction(playerId3, Location.CLAY_MOUND, List.of(),List.of(Effect.CLAY, Effect.CLAY)));
        assertTrue(game.makeAction(playerId3, Location.QUARY, List.of(), List.of(Effect.STONE)));

        assertTrue(game.makeAction(playerId4, Location.RIVER, List.of(), List.of(Effect.GOLD, Effect.GOLD)));

        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));

        //Newturn

        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.QUARY, 5));
        assertTrue(game.placeFigures(playerId1, Location.BUILDING_TILE1, 1));


        assertTrue(game.placeFigures(playerId2, Location.FOREST, 5));

        assertTrue(game.placeFigures(playerId3, Location.FOREST, 2));
        assertTrue(game.placeFigures(playerId3, Location.HUNTING_GROUNDS, 3));

        assertTrue(game.placeFigures(playerId4, Location.HUT, 2));
        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 3));


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

        //Make actions
        assertTrue(game.makeAction(playerId1, Location.QUARY, List.of(), List.of(Effect.STONE, Effect.STONE)));
        assertTrue(game.makeAction(playerId1, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        assertTrue(game.makeAction(playerId2, Location.FOREST, List.of(), List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD)));

        assertTrue(game.makeAction(playerId3, Location.FOREST, List.of(), List.of(Effect.WOOD)));
        assertTrue(game.makeAction(playerId3, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD)));

        assertTrue(game.makeAction(playerId4, Location.HUT, List.of(), List.of(Effect.FIGURE)));
        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD)));

        //At this moment will decrease player's points
        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));

        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.FOREST, 3));
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 3));

        assertTrue(game.placeFigures(playerId2, Location.CLAY_MOUND, 4));
        assertTrue(game.placeFigures(playerId2, Location.BUILDING_TILE1, 1));

        assertTrue(game.placeFigures(playerId3, Location.QUARY, 5));

        assertTrue(game.placeFigures(playerId4, Location.FOREST, 3));
        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 2));

        //Get required resources from top of building tile
        simpleBuilding = (SimpleBuilding)buildingTile.peek();
        required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);

        //Make actions
        assertTrue(game.makeAction(playerId1, Location.FOREST, List.of(), List.of(Effect.WOOD, Effect.WOOD)));
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD)));


        assertTrue(game.makeAction(playerId2, Location.CLAY_MOUND, List.of(), List.of(Effect.CLAY, Effect.CLAY)));
        assertTrue(game.makeAction(playerId2, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        assertTrue(game.makeAction(playerId3, Location.QUARY, List.of(), List.of(Effect.STONE, Effect.STONE, Effect.STONE)));

        assertTrue(game.makeAction(playerId4, Location.FOREST, List.of(), List.of(Effect.WOOD, Effect.WOOD)));
        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD)));

        //At this moment will most probably decrease player's points
        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));


        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 5));
        assertTrue(game.placeFigures(playerId1, Location.BUILDING_TILE1, 1));

        assertTrue(game.placeFigures(playerId2, Location.HUNTING_GROUNDS, 5));

        assertTrue(game.placeFigures(playerId3, Location.HUNTING_GROUNDS, 5));

        assertTrue(game.placeFigures(playerId4, Location.QUARY, 3));
        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 3));

        //Get required resources from top of building tile
        simpleBuilding = (SimpleBuilding)buildingTile.peek();
        required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);


        //Make actions
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));
        assertTrue(game.makeAction(playerId1, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        assertTrue(game.makeAction(playerId2, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId3, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId4, Location.QUARY, List.of(), List.of(Effect.STONE)));
        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD)));


        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));

        //Now all players most probably should have all resources to pay for buildings on building tile 1
        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 6));

        assertTrue(game.placeFigures(playerId2, Location.HUNTING_GROUNDS, 5));

        assertTrue(game.placeFigures(playerId3, Location.HUNTING_GROUNDS, 4));
        assertTrue(game.placeFigures(playerId3, Location.BUILDING_TILE1, 1));

        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 6));

        //Get required resources from top of building tile
        simpleBuilding = (SimpleBuilding)buildingTile.peek();
        required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);

        //Make actions
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId2, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId3, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));
        assertTrue(game.makeAction(playerId3, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD,Effect.FOOD)));

        //Feed all the tribes(Everyone should have enough food)
        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));

        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 6));

        assertTrue(game.placeFigures(playerId2, Location.HUNTING_GROUNDS, 5));

        assertTrue(game.placeFigures(playerId3, Location.HUNTING_GROUNDS, 4));
        assertTrue(game.placeFigures(playerId3, Location.BUILDING_TILE1, 1));

        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 6));

        //Get required resources from top of building tile
        simpleBuilding = (SimpleBuilding)buildingTile.peek();
        required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);

        //Make actions
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId2, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId3, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));
        assertTrue(game.makeAction(playerId3, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD,Effect.FOOD)));


        //Feed all the tribes(Everyone should have enough food)
        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));


        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 6));

        assertTrue(game.placeFigures(playerId2, Location.HUNTING_GROUNDS, 5));

        assertTrue(game.placeFigures(playerId3, Location.HUNTING_GROUNDS, 5));


        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 5));
        assertTrue(game.placeFigures(playerId3, Location.BUILDING_TILE1, 1));

        //Get required resources from top of building tile
        simpleBuilding = (SimpleBuilding)buildingTile.peek();
        required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);

        //Make actions
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId2, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId3, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD,Effect.FOOD)));
        assertTrue(game.makeAction(playerId4, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        //Feed all the tribes(Everyone should have enough food)
        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));

        //PlaceFigures
        assertTrue(game.placeFigures(playerId1, Location.HUNTING_GROUNDS, 6));

        assertTrue(game.placeFigures(playerId2, Location.HUNTING_GROUNDS, 4));
        assertTrue(game.placeFigures(playerId2, Location.BUILDING_TILE1, 1));

        assertTrue(game.placeFigures(playerId3, Location.HUNTING_GROUNDS, 5));


        assertTrue(game.placeFigures(playerId4, Location.HUNTING_GROUNDS, 6));

        //Get required resources from top of building tile
        simpleBuilding = (SimpleBuilding)buildingTile.peek();
        required = (ArrayList<Effect>)simpleBuildingResources.get(simpleBuilding);


        //Make actions
        assertTrue(game.makeAction(playerId1, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId2, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));
        assertTrue(game.makeAction(playerId2, Location.BUILDING_TILE1, required, List.of(Effect.POINT)));

        assertTrue(game.makeAction(playerId3, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD) ));

        assertTrue(game.makeAction(playerId4, Location.HUNTING_GROUNDS, List.of(), List.of(Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD,Effect.FOOD)));


        //Feed all the tribes(Everyone should have enough food)
        assertTrue(game.feedTribe(playerId1, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId2, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD)));
        assertTrue(game.feedTribe(playerId4, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD,Effect.FOOD, Effect.FOOD, Effect.FOOD)));


        assertFalse(game.placeFigures(playerId1, Location.HUT, 2));
        //It is the moment when Building tile 1 is empty
        //GamePhaseController should finish the game
        assertFalse(game.placeFigures(playerId1, Location.TOOL_MAKER, 1));

    }
}
