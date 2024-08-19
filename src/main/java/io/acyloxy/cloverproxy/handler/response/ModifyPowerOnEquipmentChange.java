package io.acyloxy.cloverproxy.handler.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.acyloxy.cloverproxy.handler.AbstractResponseHandler;
import io.acyloxy.cloverproxy.handler.Command;
import io.acyloxy.cloverproxy.handler.HandlerException;

import java.util.Optional;
import java.util.regex.Pattern;

public class ModifyPowerOnEquipmentChange extends AbstractResponseHandler {
    private static final Pattern PATTERN = Pattern.compile("ZL:\\d+");

    @Override
    public int getCmdId() {
        return Command.CHANGE_EQUIPMENT_RESP;
    }

    @Override
    public Optional<ObjectNode> handle(ObjectNode response) {
        try {
            ObjectNode head = (ObjectNode) response.get("head");
            String properties = head.get("properties").asText();
            properties = PATTERN.matcher(properties).replaceAll("ZL:114514");
            head.set("properties", TextNode.valueOf(properties));
            return Optional.of(response);
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }
}
