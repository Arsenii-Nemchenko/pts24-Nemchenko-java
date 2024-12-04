package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;


public class RewardMenuTest {

    private RewardMenu rewardMenu;
    private Player mockPlayer1;
    private Player mockPlayer2;
    private PlayerBoard mockBoard1;
    private PlayerBoard mockBoard2;
    private Effect mockEffect1;
    private Effect mockEffect2;
    private static class MockPlayerBoard implements PlayerBoard {
        private boolean hasFiguresResponse = true;

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public void giveEffect(Effect[] stuff) {

        }

        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }
    }

    private interface Player{
        PlayerOrder playerOrder();
        InterfacePlayerBoardGameBoard playerBoard();
    }

    private static class MockPlayer implements Player {
        private final PlayerOrder order;
        private final PlayerBoard board;

        public MockPlayer(int orderNum, PlayerBoard board) {
            this.order = new PlayerOrder(orderNum, 2);
            this.board = board;
        }

        @Override
        public PlayerOrder playerOrder() {
            return order;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return (InterfacePlayerBoardGameBoard) board;
        }
    }

    @Before
    public void setUp() {
        mockBoard1 = new MockPlayerBoard();
        mockBoard2 = new MockPlayerBoard();
        mockPlayer1 = new MockPlayer(0, mockBoard1);
        mockPlayer2 = new MockPlayer(1, mockBoard2);

        // Mocking effects
        mockEffect1 = Effect.WOOD; // Dummy effect
        mockEffect2 = Effect.STONE; // Dummy effect

        // Creating a RewardMenu instance
        //rewardMenu = new RewardMenu((Player[]) new Player[]{mockPlayer1, mockPlayer2});
    }

    @Test
    public void testInitiate() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        // Check if the rewards list was correctly initialized
        assertTrue(rewardMenu.State().contains(mockEffect1.toString()));
        assertTrue(rewardMenu.State().contains(mockEffect2.toString()));
    }

    @Test
    public void testTakeRewardSuccess() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        boolean result = rewardMenu.takeReward(mockPlayer1.playerOrder(), mockEffect1);
        assertTrue(result);

        // Check that the reward was removed from the list
        assertFalse(rewardMenu.State().contains(mockEffect1.toString()));

    }

    @Test
    public void testTakeRewardFailInvalidReward() {
        rewardMenu.initiate(new Effect[]{mockEffect2});

        boolean result = rewardMenu.takeReward(mockPlayer1.playerOrder(), mockEffect1);
        assertFalse(result);
    }

    @Test
    public void testTakeRewardFailPlayerNotInQueue() {
        rewardMenu.initiate(new Effect[]{mockEffect1});

        rewardMenu.takeReward(mockPlayer1.playerOrder(), mockEffect1);
        boolean result = rewardMenu.takeReward(mockPlayer1.playerOrder(), mockEffect1);

        assertFalse(result);
    }

    @Test
    public void testTryMakeActionNoActionPossible() {
        HasAction result = rewardMenu.tryMakeAction(mockPlayer1.playerOrder());
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result);
    }

    @Test
    public void testTryMakeActionAutomaticActionDone() {
        rewardMenu.initiate(new Effect[]{mockEffect1});

        HasAction result = rewardMenu.tryMakeAction(mockPlayer1.playerOrder());
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, result);

    }

    @Test
    public void testTryMakeActionWaitingForPlayerAction() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        HasAction result = rewardMenu.tryMakeAction(mockPlayer1.playerOrder());
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, result);
    }

    @Test
    public void testState() {
        rewardMenu.initiate(new Effect[]{mockEffect1, mockEffect2});

        String state = rewardMenu.State();

        // Verify that the state contains the expected elements
        assertTrue(state.contains("rewards"));
        assertTrue(state.contains("players"));
        assertTrue(state.contains("remainingPlayers"));
    }
}
