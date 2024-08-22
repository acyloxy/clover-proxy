package io.acyloxy.cloverproxy.handler.request;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.AbstractRequestHandler;
import io.acyloxy.cloverproxy.handler.Command;
import io.acyloxy.cloverproxy.handler.HandleException;
import io.acyloxy.cloverproxy.handler.Preset;

import java.util.Optional;

public class ModifyBattleResult extends AbstractRequestHandler {
    @Override
    public int getCmdId() {
        return Command.FINISH_BATTLE_REQ;
    }

    @Override
    public Optional<ObjectNode> handle(ObjectNode request) {
        try {
            request.set("bossReward", IntNode.valueOf(Preset.BOX_COUNT));
            request.set("isRandomBossKill", IntNode.valueOf(Preset.PILOT_FRAGMENT ? 1 : 0));
            request.set("engineDmgMax", IntNode.valueOf(2000));
            request.set("hpMax", IntNode.valueOf(20000));
            request.set("weaponDmgMax", IntNode.valueOf(2000));
            request.set("wingmanDmgMax", IntNode.valueOf(2000));
            request.set("planeHpBase", IntNode.valueOf(20000));
            request.set("planeHurtMax", IntNode.valueOf(20000));
            request.set("planeHurtMin", IntNode.valueOf(20000));
            request.set("planeHurtNum", IntNode.valueOf(1));
            request.set("planeHurtTotal", IntNode.valueOf(20000));
            return Optional.of(request);
        } catch (Exception e) {
            throw new HandleException(e);
        }
    }
}
