package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

public class PlayerBoardComponent {
    public static InterfacePlayerBoardGameBoard createBoard(){
        return new PlayerBoardGameBoardFacade(new PlayerBoard());
    }
}
