package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class PlayerCivilizationCardsTest {
    private PlayerCivilizationCards playerCivilizationCards;
    @Test
    public void addEndOfGameEffects(){
        playerCivilizationCards = new PlayerCivilizationCards();

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Art});
        assertEquals(Optional.of(1), playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.Art));

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Art, EndOfGameEffect.Art});
        assertEquals(Optional.of(3),playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.Art));

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Farmer, EndOfGameEffect.Farmer});
        assertEquals(Optional.of(2), playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.Farmer));
        assertEquals(Optional.of(3),playerCivilizationCards.getEndOfGameEffectMap().get(EndOfGameEffect.Art));
    }

    @Test
    public void countEndOfGamePoints(){
        playerCivilizationCards = new PlayerCivilizationCards();

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Farmer, EndOfGameEffect.Farmer, EndOfGameEffect.Farmer});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Builder, EndOfGameEffect.Builder});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Shaman, EndOfGameEffect.Shaman,EndOfGameEffect.Shaman});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.ToolMaker});

        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Art, EndOfGameEffect.Art});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Music, EndOfGameEffect.Music});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Pottery});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Sundial});
        playerCivilizationCards.addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.Transport});

        assertEquals(82, playerCivilizationCards.calculateEndOfGameCivilizationCardPoints(3,8,4,9));


    }


}