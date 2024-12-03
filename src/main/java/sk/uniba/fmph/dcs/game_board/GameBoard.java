package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocation> gameBoardLocations =  new HashMap<>();

    public GameBoard(List<Player> players) {
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

        gameBoardLocations.put(Location.CIVILISATION_CARD1, new FigureLocationAdaptor(civilizationCardPlace1, players));
        gameBoardLocations.put(Location.CIVILISATION_CARD2, new FigureLocationAdaptor(civilizationCardPlace2, players));
        gameBoardLocations.put(Location.CIVILISATION_CARD3, new FigureLocationAdaptor(civilizationCardPlace3, players));
        gameBoardLocations.put(Location.CIVILISATION_CARD4, new FigureLocationAdaptor(civilizationCardPlace4, players));

        ToolMakerHutFields toolMakerHutFields = new ToolMakerHutFields(players.size());

        gameBoardLocations.put(Location.HUT, new FigureLocationAdaptor(new PlaceOnHutAdaptor(toolMakerHutFields), players));
        gameBoardLocations.put(Location.FIELD, new FigureLocationAdaptor(new PlaceOnFieldsAdaptor(toolMakerHutFields), players));
        gameBoardLocations.put(Location.TOOL_MAKER, new FigureLocationAdaptor(new PlaceOnToolMakerAdaptor(toolMakerHutFields), players));

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
                new FigureLocationAdaptor(new ResourceSource("Hunting grounds", Effect.FOOD, Integer.MAX_VALUE, Integer.MAX_VALUE, currentThrow), players
                ));
        gameBoardLocations.put(
                Location.CLAY_MOUND,
                new FigureLocationAdaptor(new ResourceSource("Clay mound", Effect.CLAY, maxFigures, maxFigureColors, currentThrow), players
                ));
        gameBoardLocations.put(
                Location.FOREST,
                new FigureLocationAdaptor(new ResourceSource("Forest", Effect.WOOD, maxFigures, maxFigureColors, currentThrow), players
                ));
        gameBoardLocations.put(
                Location.QUARY,
                new FigureLocationAdaptor(new ResourceSource("Quarry", Effect.STONE, maxFigures, maxFigureColors, currentThrow), players
                ));
        gameBoardLocations.put(
                Location.RIVER,
                new FigureLocationAdaptor(new ResourceSource("River", Effect.GOLD, maxFigures, maxFigureColors, currentThrow), players
                ));



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
            tile3.add(buildings.get(i));
            counter++;
        }


        gameBoardLocations.put(Location.BUILDING_TILE1, new FigureLocationAdaptor(new BuildingTile(tile1), players));
        gameBoardLocations.put(Location.BUILDING_TILE2, new FigureLocationAdaptor(new BuildingTile(tile2), players));
        if(players.size() == 3) {
            gameBoardLocations.put(Location.BUILDING_TILE3, new FigureLocationAdaptor(new BuildingTile(tile3), players));
        }
        if(players.size() == 4) {
            gameBoardLocations.put(Location.BUILDING_TILE4, new FigureLocationAdaptor(new BuildingTile(tile4), players));
        }




    }
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
        Collections.shuffle(allCards);
        return allCards;
    }
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

        Collections.shuffle(buildings);
        return buildings;
    }

    public final Map<Location, InterfaceFigureLocation> getMap(){
        return gameBoardLocations;
    }
    @Override
    public String state() {
        Map<Location, String> informationFromGameBoard = new HashMap<>();

        for (Location l : gameBoardLocations.keySet()) {
            informationFromGameBoard.put(l, gameBoardLocations.get(l).toString());
        }
        return new JSONObject(informationFromGameBoard).toString();
    }
}