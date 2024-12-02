package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Map;

public class ToolMakerHutFields{
    private PlayerOrder toolMakerFigures;
    private PlayerOrder hutFigures;
    private PlayerOrder fieldsFigures;

    private final int restriction;


    public ToolMakerHutFields(int numberOfPlayers){
        if(numberOfPlayers<4){
            this.restriction = 2;
        }else{
            this.restriction = 3;
        }
    }

    private boolean checkRestriction(){
        int result = 0;
        if(toolMakerFigures != null) {
            result++;
        }
        if(fieldsFigures != null){
            result++;
        }
        if(hutFigures != null){
            result++;
        }

        return result < restriction;
    }



    public boolean placeOnToolMaker(Player player){
        if(!canPlaceOnToolMaker(player)){
            return false;
        }

        toolMakerFigures = player.playerOrder();
        return true;
    }

    public boolean actionToolMaker(Player player){
        if(!player.playerOrder().equals(toolMakerFigures)){
            return false;
        }
        ArrayList<Effect> list = new ArrayList<>();
        list.add(Effect.TOOL);
        player.playerBoard().giveEffect(list);
        return true;
    }
    //
    public boolean canPlaceOnToolMaker(Player player){
        return toolMakerFigures == null && checkRestriction();
    }

    public boolean placeOnHut(Player player){
        if(!canPlaceOnHut(player)){
            return false;
        }

        hutFigures = player.playerOrder();
        return true;
    }

    public boolean canPlaceOnHut(Player player){
        return hutFigures == null && checkRestriction();
    }

    public boolean actionHut(Player player){
        if(!player.playerOrder().equals(hutFigures)){
            return false;
        }

        player.playerBoard().giveFigure();
        return true;
    }

    public boolean placeOnFields(Player player){
        if(!canPlaceOnFields(player)){
            return false;
        }

        fieldsFigures = player.playerOrder();
        return true;

    }

    public boolean actionFields(Player player){
        if(!player.playerOrder().equals(fieldsFigures)){
            return false;
        }
        ArrayList<Effect> list = new ArrayList<>();
        list.add(Effect.FIELD);
        player.playerBoard().giveEffect(list);
        return true;
    }

    public boolean canPlaceOnFields(Player player){
        return fieldsFigures == null && checkRestriction();
    }
    public boolean newTurn(){
        toolMakerFigures = null;
        hutFigures = null;
        fieldsFigures = null;
        return true;
    }

    public String state(){
        Map<String, String> state = Map.of(
                "toolMakerFigures", toolMakerFigures.toString(),
                "hutFigures", hutFigures.toString(),
                "fieldsFigures", fieldsFigures.toString());
        return new JSONObject(state).toString();
    }


}
