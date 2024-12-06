package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

/**
 * Represents the game board for a multiplayer game, managing locations, players,
 * and game elements like Civilization Cards and buildings.
 * Implements {@link InterfaceGetState} for providing a JSON representation of its state.
 */
public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> gameBoardLocations =  new HashMap<>();
    private final List<Player> players;

    /**
     * Constructs a new GameBoard with the given players.
     * Initializes game locations, cards, buildings, and other game-specific elements.
     *
     * @param players a list of players participating in the game
     */
    public GameBoard(List<Player> players) {
        this.players = players;
        List<CivilizationCard> civilizationCards = getCards();
        CivilizationCardDeck deck = new CivilizationCardDeck(civilizationCards);

        CurrentThrow currentThrow = new CurrentThrow(players.get(0), Effect.WOOD, 2);

        GetSomethingThrow getThrow = new GetSomethingThrow(currentThrow);
        GetSomethingFixed getFixed = new GetSomethingFixed();
        GetSomethingChoice getChoice = new GetSomethingChoice(2);
        GetCard getCard = new GetCard(deck);
        AllPlayersTakeReward getReward = new AllPlayersTakeReward();


        CivilizationCardPlace civilizationCardPlace1 = new CivilizationCardPlace(
                null, deck, 1,
                getChoice, getThrow, getCard, getReward, getFixed
        );
        CivilizationCardPlace civilizationCardPlace2 = new CivilizationCardPlace(
                civilizationCardPlace1, deck, 2,
                getChoice, getThrow, getCard, getReward, getFixed
        );
        CivilizationCardPlace civilizationCardPlace3 = new CivilizationCardPlace(
                civilizationCardPlace2, deck, 3,
                getChoice, getThrow, getCard, getReward, getFixed
        );
        CivilizationCardPlace civilizationCardPlace4 = new CivilizationCardPlace(
                civilizationCardPlace3, deck, 4,
                getChoice, getThrow, getCard, getReward, getFixed
        );

        gameBoardLocations.put(Location.CIVILISATION_CARD1, civilizationCardPlace1);
        gameBoardLocations.put(Location.CIVILISATION_CARD2,civilizationCardPlace2);
        gameBoardLocations.put(Location.CIVILISATION_CARD3, civilizationCardPlace3);
        gameBoardLocations.put(Location.CIVILISATION_CARD4, civilizationCardPlace4);

        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(players.size());

        gameBoardLocations.put(Location.HUT, new PlaceOnHutAdaptor(toolMakerHutFields));
        gameBoardLocations.put(Location.FIELD, new PlaceOnFieldsAdaptor(toolMakerHutFields));
        gameBoardLocations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(toolMakerHutFields));

        int maxFigures = 7;
        int maxFigureColors;
        if(players.size() ==  4){
            maxFigureColors = 4;
        }else if(players.size() == 3){
            maxFigureColors = 2;
        }else{
            maxFigureColors = 1;
        }
        gameBoardLocations.put(
                Location.HUNTING_GROUNDS,
                new ResourceSource("Hunting grounds", Effect.FOOD, Integer.MAX_VALUE, Integer.MAX_VALUE, currentThrow)
                );
        gameBoardLocations.put(
                Location.CLAY_MOUND,
                new ResourceSource("Clay mound", Effect.CLAY, maxFigures, maxFigureColors, currentThrow)
                );
        gameBoardLocations.put(
                Location.FOREST,
               new ResourceSource("Forest", Effect.WOOD, maxFigures, maxFigureColors, currentThrow)
                );
        gameBoardLocations.put(
                Location.QUARY,
                new ResourceSource("Quarry", Effect.STONE, maxFigures, maxFigureColors, currentThrow)
                );
        gameBoardLocations.put(
                Location.RIVER,
               new ResourceSource("River", Effect.GOLD, maxFigures, maxFigureColors, currentThrow)
                );



        List<Building> buildings = getBuildings();

        List<Building> tile1 = new ArrayList<>();
        List<Building> tile2 = new ArrayList<>();
        List<Building> tile3 = new ArrayList<>();
        List<Building> tile4 = new ArrayList<>();

        int counter = 0;
        for(int i = counter; i != 7; i++){
            tile1.add(buildings.get(i));
            counter++;
        }
        for(int i = counter; i != 14; i++){
            tile2.add(buildings.get(i));
            counter++;
        }
        for(int i = counter; i != 21; i++){
            tile3.add(buildings.get(i));
            counter++;
        }
        for(int i = counter; i != 28; i++){
            tile4.add(buildings.get(i));
            counter++;
        }


        gameBoardLocations.put(Location.BUILDING_TILE1, new BuildingTile(tile1));
        gameBoardLocations.put(Location.BUILDING_TILE2, new BuildingTile(tile2));
        if(players.size() == 3) {
            gameBoardLocations.put(Location.BUILDING_TILE3, new BuildingTile(tile3));
        }
        if(players.size() == 4) {
            gameBoardLocations.put(Location.BUILDING_TILE4, new BuildingTile(tile4));
        }


    }

    /**
     * Creates and returns a map of locations with adapters for figure placement.
     *
     * @return a map linking locations to their figure placement adapters
     */
    public final Map<Location, InterfaceFigureLocation> getMap(){
        Map<Location, InterfaceFigureLocation> map = new HashMap<>();
        for(Location location: gameBoardLocations.keySet()){
            map.put(location, new FigureLocationAdaptor(gameBoardLocations.get(location),players ));
        }
        return map;
    }
    /**
     * Creates and returns a list of predefined Civilization Cards.
     *
     * @return a list of Civilization Cards
     */
    private List<CivilizationCard> getCards(){
        List<CivilizationCard> allCards = new ArrayList<>();
        // Dice roll (10 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.WRITING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.SUNDIAL)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.POTTERY)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TRANSPORT)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)));

        // Food (7 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.MEDICINE)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.POTTERY)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.WEAVING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.WEAVING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.BUILDER)));

        // Resource (5 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE, ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.TRANSPORT)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.CLAY), Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.GOLD), Arrays.asList(EndOfGameEffect.SHAMAN)));

        // Resources with dice roll (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowStone), Arrays.asList(EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowGold), Arrays.asList(EndOfGameEffect.ART)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.WOOD), Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));

        // Victory points (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.MUSIC)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.MUSIC)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));

        // Extra tool tile (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Tool), Arrays.asList(EndOfGameEffect.ART)));

        // Agriculture (2 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Field), Arrays.asList(EndOfGameEffect.SUNDIAL)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Field), Arrays.asList(EndOfGameEffect.FARMER)));

        // Civilization card for final scoring (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.CARD), Arrays.asList(EndOfGameEffect.WRITING)));

        // One-use tool (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool2), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool3), Arrays.asList(EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool4), Arrays.asList(EndOfGameEffect.BUILDER)));

        // Any 2 resources (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource), Arrays.asList(EndOfGameEffect.MEDICINE)));
        return allCards;
    }

    /**
     * Creates and returns a list of predefined buildings.
     *
     * @return a list of buildings
     */
    private List<Building> getBuildings(){
        List<Building> buildings = new ArrayList<>();

        // Simple buildings
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.CLAY))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.STONE, Effect.GOLD))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.STONE, Effect.STONE))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.CLAY, Effect.CLAY))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.STONE, Effect.WOOD))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.WOOD))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.STONE, Effect.STONE))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.GOLD))));
        buildings.add(new SimpleBuilding(new ArrayList<>(Arrays.asList(Effect.STONE, Effect.CLAY, Effect.GOLD))));

        // Variable buildings
        for (int i = 0; i < 6; i++) {
            buildings.add(new VariableBuilding(4, 2));
        }
        buildings.add(new VariableBuilding(4, 3));
        buildings.add(new VariableBuilding(4, 3));
        buildings.add(new VariableBuilding(4, 4));

        // Arbitrary buildings
        for (int i = 0; i < 6; i++) {
            buildings.add(new ArbitraryBuilding(i));
        }
        buildings.add(new ArbitraryBuilding(6));
        buildings.add(new ArbitraryBuilding(7));
        buildings.add(new ArbitraryBuilding(7));

        return buildings;
    }

    public final Map<Location, InterfaceFigureLocationInternal> getMapInternal(){
        return gameBoardLocations;
    }
    /**
     * Returns the current state of this throw action as a JSON string.
     *
     * @return a JSON representation of the state
     */
    @Override
    public String state() {
        Map<Location, String> informationFromGameBoard = new HashMap<>();

        for (Location l : gameBoardLocations.keySet()) {
            informationFromGameBoard.put(l, gameBoardLocations.get(l).toString());
        }
        return new JSONObject(informationFromGameBoard).toString();
    }

}