package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.InterfaceGamePhaseController;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.HashMap;

public class GamePhaseControllerComponent {
    public static InterfaceGamePhaseController createController(PlayerOrder startingPlayer){
        return new GamePhaseController(new HashMap<>(),startingPlayer);
    }
}
