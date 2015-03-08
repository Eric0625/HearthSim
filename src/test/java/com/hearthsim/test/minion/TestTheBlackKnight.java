package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestTheBlackKnight {

    private HearthTreeNode board;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        board = new HearthTreeNode(new BoardModel(deck, deck));

        Minion minion0_0 = new StormwindChampion();
        Minion minion0_1 = new RaidLeader();
        Minion minion0_2 = new HarvestGolem();

        minion0_1.setTaunt(true); //give him Taunt... he should be left untouched by the Black Knight

        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();
        Minion minion1_2 = new Abomination();
        Minion minion1_3 = new LootHoarder();

        board.data_.getCurrentPlayer().placeCardHand(minion0_0);
        board.data_.getCurrentPlayer().placeCardHand(minion0_1);
        board.data_.getCurrentPlayer().placeCardHand(minion0_2);

        board.data_.getWaitingPlayer().placeCardHand(minion1_0);
        board.data_.getWaitingPlayer().placeCardHand(minion1_1);
        board.data_.getWaitingPlayer().placeCardHand(minion1_2);
        board.data_.getWaitingPlayer().placeCardHand(minion1_3);


        board.data_.getCurrentPlayer().setMana((byte)20);
        board.data_.getWaitingPlayer().setMana((byte)20);

        board.data_.getCurrentPlayer().setMaxMana((byte)10);
        board.data_.getWaitingPlayer().setMaxMana((byte)10);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        board.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);
        board.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);
        board.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();

        Minion fb = new TheBlackKnight();
        board.data_.getCurrentPlayer().placeCardHand(fb);

    }


    @Test
    public void test0() throws HSException {
        Card theCard = board.data_.getCurrentPlayer().getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, deck);

        assertNull(ret);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 10);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);
    }


    @Test
    public void test1() throws HSException {
        Card theCard = board.data_.getCurrentPlayer().getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 3, board, deck, deck);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 4);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);
        assertEquals(currentPlayer.getMinions().get(3).getTotalHealth(), 6);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 6);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);


        assertEquals(board.numChildren(), 1);
        HearthTreeNode c0 = board.getChildren().get(0);
        currentPlayer = c0.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        waitingPlayer = c0.data_.modelForSide(PlayerSide.WAITING_PLAYER);
        assertEquals(c0.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 1);
        assertEquals(c0.data_.getCurrentPlayer().getMana(), 4);
        assertEquals(c0.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(c0.data_.getCurrentPlayer().getHero().getHealth(), 28);
        assertEquals(c0.data_.getWaitingPlayer().getHero().getHealth(), 28);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(3).getTotalHealth(), 4);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 5);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 6);

        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 6);
    }
}
