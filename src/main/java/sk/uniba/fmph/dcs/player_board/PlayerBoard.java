package sk.uniba.fmph.dcs.player_board;

import java.util.*;

public class PlayerBoard {
    private int points;
    private int houses; 
    private final int playerFigures;

    public PlayerBoard() {
        this.points = 0;
        this.houses = 0;
        this.playerFigures = 5;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addHouse() {

    }

    public void addEndGamePoints() {

    }

    public String state() {
        return "";
    }
}
