package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionBattlecryInterface;
import com.hearthsim.event.effect.CardEffectCharacter;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class FlameImp extends Minion implements MinionBattlecryInterface {

    public FlameImp() {
        super();
    }

    /**
     * Battlecry: Deal 3 damage to your hero
     */
    @Override
    public CardEffectCharacter<Minion> getBattlecryEffect() {
        return (PlayerSide originSide, Minion origin, PlayerSide targetSide, int minionPlacementIndex, HearthTreeNode boardState) -> {
            HearthTreeNode toRet = boardState;
            PlayerModel currentPlayer = toRet.data_.modelForSide(PlayerSide.CURRENT_PLAYER);
            toRet = currentPlayer.getHero().takeDamageAndNotify((byte) 3, PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, toRet, false, false);
            return toRet;
        };
    }

}
