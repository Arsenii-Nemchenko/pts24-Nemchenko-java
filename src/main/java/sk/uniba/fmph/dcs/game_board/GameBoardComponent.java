package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameBoardComponent {
    /*private GameBoard gameBoard;
    private List<Player> player;


    public GameBoardComponent(List<Player> players){
        gameBoard = new GameBoard(players);
    }


    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
        for(Player player: player){
            if(player.playerOrder().getOrder() == playerId){
                if(HasAction.NO_ACTION_POSSIBLE == gameBoard.getMap().get(location).tryToPlaceFigures(player, figuresCount)){
                    break;
                }else{
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean makeAction(int playerId, Location location, List<Effect> usedResources, List<Effect> desiredResources) {
        for(Player player: player){
            if(player.playerOrder().getOrder() == playerId){
                if(HasAction.NO_ACTION_POSSIBLE == gameBoard.getMap().get(location).tryToMakeAction(player)){
                    break;
                }else{
                    if(ActionResult.FAILURE == gameBoard.getMap().get(location).makeAction(player, usedResources.toArray(new Effect[0]), desiredResources.toArray(new Effect[0])));
                }
            }
        }
        return false;
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        return false;
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        return false;
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        return false;
    }

    @Override
    public boolean feedTribe(int playerId, List<Effect> resources) {
        return false;
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        return false;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        return false;
    }
     */

}
