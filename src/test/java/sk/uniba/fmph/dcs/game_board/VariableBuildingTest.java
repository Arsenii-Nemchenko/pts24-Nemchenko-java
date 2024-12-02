package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;

public class VariableBuildingTest {

    @Test
    public void test_calculation_1(){
        List<Effect> buildingResources = Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.GOLD);

        VariableBuilding building = new VariableBuilding(4, 2);

        assertEquals(building.build(buildingResources), OptionalInt.of(15));

        assertEquals(building.state(), String.format("VariableBuilding[resources=%d,types=%d]", 4, 2));
    }

    @Test
    public void test_calculation_2(){
        List<Effect> buildingResources = Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.GOLD);

        VariableBuilding building = new VariableBuilding(4, 2);

        assertEquals(building.build(buildingResources), OptionalInt.empty());

        assertEquals(building.state(), String.format("VariableBuilding[resources=%d,types=%d]", 4, 2));
    }
    @Test
    public void test_calculation_3(){
        List<Effect> buildingResources = Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD);

        VariableBuilding building = new VariableBuilding(4, 4);

        assertEquals(building.build(buildingResources), OptionalInt.of(18));

        assertEquals(building.state(), String.format("VariableBuilding[resources=%d,types=%d]", 4, 4));
    }
    @Test
    public void test_calculation_4(){
        List<Effect> buildingResources = Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE);

        VariableBuilding building = new VariableBuilding(4, 4);

        assertEquals(building.build(buildingResources), OptionalInt.empty());

        assertEquals(building.state(), String.format("VariableBuilding[resources=%d,types=%d]", 4, 4));
    }

}
