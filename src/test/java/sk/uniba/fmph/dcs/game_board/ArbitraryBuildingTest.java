package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class ArbitraryBuildingTest {
    @Test
    public void test1() {
        List<Effect> resources = List.of();
        ArbitraryBuilding building = new ArbitraryBuilding(3);

        OptionalInt result = building.build(resources);
        assertEquals(building.build(resources), OptionalInt.empty());
    }

    @Test
    public void test2() {
        List<Effect> resources = List.of(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.STONE);

        ArbitraryBuilding building = new ArbitraryBuilding(4);
        OptionalInt result = building.build(resources);

        assertTrue(result.isPresent());
        assertEquals(result.getAsInt(), 17);
    }

    @Test
    public void testStateMethod() {
        ArbitraryBuilding building = new ArbitraryBuilding(5);
        assertEquals("ArbitraryBuilding[maxResources=5]", building.state());
    }
}
