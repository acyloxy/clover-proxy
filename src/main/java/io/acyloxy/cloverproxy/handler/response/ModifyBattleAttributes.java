package io.acyloxy.cloverproxy.handler.response;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.AbstractResponseHandler;
import io.acyloxy.cloverproxy.handler.Command;
import io.acyloxy.cloverproxy.handler.HandleException;
import io.acyloxy.cloverproxy.handler.Preset;

import java.util.Optional;

public class ModifyBattleAttributes extends AbstractResponseHandler {
    @Override
    public int getCmdId() {
        return Command.CREATE_BATTLE_RESP;
    }

    @Override
    public Optional<ObjectNode> handle(ObjectNode response) {
        try {
            response.set("engineDmg", IntNode.valueOf(Preset.ATTACK));
            response.set("hp", IntNode.valueOf(Preset.HP));
            response.set("weaponDmg", IntNode.valueOf(Preset.ATTACK_WEAPON));
            response.set("wingmanDmg", IntNode.valueOf(Preset.ATTACK_WINGMAN));
            return Optional.of(response);
        } catch (Exception e) {
            throw new HandleException(e);
        }
    }
}
