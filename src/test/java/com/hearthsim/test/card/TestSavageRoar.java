package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.SavageRoar;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSavageRoar {


    private HearthTreeNode board;
    private Deck deck;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());

        Minion minion0_0 = new BoulderfistOgre();
        Minion minion0_1 = new RaidLeader();
        Minion minion1_0 = new BoulderfistOgre();
        Minion minion1_1 = new RaidLeader();

        board.data_.getCurrentPlayer().placeCardHand(minion0_0);
        board.data_.getCurrentPlayer().placeCardHand(minion0_1);

        board.data_.getWaitingPlayer().placeCardHand(minion1_0);
        board.data_.getWaitingPlayer().placeCardHand(minion1_1);

        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        Card fb = new SavageRoar();
        board.data_.getCurrentPlayer().placeCardHand(fb);

        board.data_.getCurrentPlayer().setMana((byte)10);
        board.data_.getWaitingPlayer().setMana((byte)10);

        board.data_.getCurrentPlayer().setMaxMana((byte)10);
        board.data_.getWaitingPlayer().setMaxMana((byte)10);

        HearthTreeNode tmpBoard = new HearthTreeNode(board.data_.flipPlayers());
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);
        tmpBoard.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, tmpBoard.data_.getCurrentPlayer().getHero(), tmpBoard, deck, null);

        board = new HearthTreeNode(tmpBoard.data_.flipPlayers());
        board.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);
        board.data_.getCurrentPlayer().getHand().get(0).useOn(PlayerSide.CURRENT_PLAYER, board.data_.getCurrentPlayer().getHero(), board, deck, null);

        board.data_.resetMana();
        board.data_.resetMinions();

    }

    @Test
    public void test2() throws HSException {
        Card theCard = board.data_.getCurrentPlayer().getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, deck, null);

        assertFalse(ret == null);
        PlayerModel currentPlayer = board.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
        PlayerModel waitingPlayer = board.data_.modelForSide(PlayerSide.WAITING_PLAYER);

        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 7);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 9);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertEquals(board.data_.getCurrentPlayer().getHero().getExtraAttackUntilTurnEnd(), 2);
        assertEquals(board.data_.getWaitingPlayer().getHero().getExtraAttackUntilTurnEnd(), 0);
        assertEquals(currentPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 0);

        Minion target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
        ret = currentPlayer.getMinions().get(0).attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);

        assertFalse(ret == null);
        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 7);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 30);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 3);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 8);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertEquals(board.data_.getCurrentPlayer().getHero().getExtraAttackUntilTurnEnd(), 2);
        assertEquals(board.data_.getWaitingPlayer().getHero().getExtraAttackUntilTurnEnd(), 0);
        assertEquals(currentPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 0);

        target = board.data_.getCharacter(PlayerSide.WAITING_PLAYER, 2);
        ret = board.data_.getCurrentPlayer().getHero().attack(PlayerSide.WAITING_PLAYER, target, board, deck, null, false);

        assertFalse(ret == null);
        assertEquals(board.data_.getNumCards_hand(), 0);
        assertEquals(currentPlayer.getNumMinions(), 1);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(board.data_.getCurrentPlayer().getMana(), 7);
        assertEquals(board.data_.getWaitingPlayer().getMana(), 10);
        assertEquals(board.data_.getCurrentPlayer().getHero().getHealth(), 23);
        assertEquals(board.data_.getWaitingPlayer().getHero().getHealth(), 30);
        assertEquals(currentPlayer.getMinions().get(0).getHealth(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 8);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 7);

        assertEquals(board.data_.getCurrentPlayer().getHero().getExtraAttackUntilTurnEnd(), 2);
        assertEquals(board.data_.getWaitingPlayer().getHero().getExtraAttackUntilTurnEnd(), 0);
        assertEquals(currentPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getExtraAttackUntilTurnEnd(), 0);
        assertEquals(waitingPlayer.getMinions().get(1).getExtraAttackUntilTurnEnd(), 0);

    }

}
