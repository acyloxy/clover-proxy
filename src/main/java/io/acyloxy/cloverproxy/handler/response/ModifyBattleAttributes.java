package io.acyloxy.cloverproxy.handler.response;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.AbstractResponseHandler;
import io.acyloxy.cloverproxy.handler.Command;
import io.acyloxy.cloverproxy.handler.HandlerException;

import java.util.Optional;

public class ModifyBattleAttributes extends AbstractResponseHandler {
    @Override
    public int getCmdId() {
        return Command.CREATE_BATTLE_RESP;
    }

    @Override
    public Optional<ObjectNode> handle(ObjectNode response) {
        try {
            response.set("engineDmg", IntNode.valueOf(44444)); // 战机
            response.set("hp", IntNode.valueOf(88888)); // 装甲
            response.set("weaponDmg", IntNode.valueOf(4444)); // 副武器
            response.set("wingmanDmg", IntNode.valueOf(4444)); // 僚机
            return Optional.of(response);
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }
}
