package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class PlayerCivilizationCardsTest {
    private PlayerCivilizationCards playerCivilizationCards;
    @Test
    public void addEndOfGameEffects(){
        playerCivilizationCards = new PlayerCivilizationCards();

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.ART});
        assertEquals(Optional.of(1), playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.ART));

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.ART, EndOfGameEffect.ART});
        assertEquals(Optional.of(3),playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.ART));

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.FARMER, EndOfGameEffect.FARMER});
        assertEquals(Optional.of(2), playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.FARMER));
        assertEquals(Optional.of(3),playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.ART));
    }

    @Test
    public void countEndOfGamePoints(){
        playerCivilizationCards = new PlayerCivilizationCards();

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.FARMER, EndOfGameEffect.FARMER, EndOfGameEffect.FARMER});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN,EndOfGameEffect.SHAMAN});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.TOOL_MAKER});

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.ART, EndOfGameEffect.ART});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.MUSIC, EndOfGameEffect.MUSIC});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.POTTERY});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.SUNDIAL});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.TRANSPORT});

        assertEquals(82, playerCivilizationCards.calculateEndOfGameCivilizationCardPoints(3,8,4,9));


    }


}