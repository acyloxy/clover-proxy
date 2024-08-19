package io.acyloxy.cloverproxy.handler.request;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.AbstractRequestHandler;
import io.acyloxy.cloverproxy.handler.Command;
import io.acyloxy.cloverproxy.handler.HandlerException;

import java.util.Optional;

public class ModifyBoxCount extends AbstractRequestHandler {
    @Override
    public int getCmdId() {
        return Command.FINISH_BATTLE_REQ;
    }

    @Override
    public Optional<ObjectNode> handle(ObjectNode request) {
        try {
            request.set("bossReward", IntNode.valueOf(0));
            return Optional.of(request);
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }
}
