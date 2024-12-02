package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.GameBoard;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoneAge {
    private GameBoard gameBoard;

    private GamePhaseController controller;

    private PlayerBoard playerBoardComponent;

    private List<Player> players;

    public StoneAge(int playerCount){
        players = new ArrayList<>();
        playerBoardComponent = new playerBoardComponent();

        for(int i = 0; i<playerCount; i++){
            PlayerOrder order = new PlayerOrder(i, playerCount);
            InterfacePlayerBoardGameBoard facade = playerBoardComponent.create();
            players.add(new Player() {
                @Override
                public PlayerOrder playerOrder() {
                    return order;
                }

                @Override
                public InterfacePlayerBoardGameBoard playerBoard() {
                    return facade;
                }
            });
        }
        gameBoard = new GameBoard();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StoneAge stoneAge = new StoneAge(scanner.nextInt());
    }
}
