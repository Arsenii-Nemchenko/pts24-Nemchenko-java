package sk.uniba.fmph.dcs.player_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.Map;

public class PlayerFigures implements InterfaceGetState {
private int totalFigures;
private int figures;

    public void addNewFigure(){
        if(totalFigures < 10){
            totalFigures++;
        }
    }

    public boolean hasFigures(int count){
        return figures >= count;
    }

    public int getTotalFigures() {
        return totalFigures;
    }

    public boolean takeFigures(int count){
        if(hasFigures(count)){
            figures = figures - count;
            return true;
        }else{
            return  false;
        }
    }

    public void newTurn (){
        figures = totalFigures;
    }

    @Override
    public String state(){
        Map<String, Object> state = Map.of(
                "figures", figures,
                "totalFigures", totalFigures
        );

        return new JSONObject(state).toString();
    }

}
