package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseController;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseControllerComponent;
import sk.uniba.fmph.dcs.player_board.PlayerBoardComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoneAge {
    private GameBoard gameBoard;

    private InterfaceGamePhaseController controller;

    private List<Player> players;

    private PlayerOrder startingPlayer;


    public StoneAge(int playerCount) {
        players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            PlayerOrder order = new PlayerOrder(i, playerCount);
            if(i == 0){
                startingPlayer = order;
            }
            InterfacePlayerBoardGameBoard facade = PlayerBoardComponent.createBoard();
            players.add(new Player(order, facade));
        }

        gameBoard = new GameBoard(players);

        controller = GamePhaseControllerComponent.createController(startingPlayer);
    }
}
