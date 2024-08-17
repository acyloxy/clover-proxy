package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseHandlers {
    private static final List<ResponseHandler> RESPONSE_HANDLERS = new ArrayList<>();

    static {
        init();
    }

    private static void init() {
        registerModifyEquips();
    }

    public static void register(ResponseHandler handler) {
        RESPONSE_HANDLERS.add(handler);
    }

    public static boolean handle(JsonNode response) throws Exception {
        boolean handled = false;
        for (ResponseHandler handler : RESPONSE_HANDLERS) {
            Optional<JsonNode> opt = handler.handle(response);
            if (opt.isPresent()) {
                handled = true;
                response = opt.get();
            }
        }
        return handled;
    }

    private static void registerModifyEquips() {
        // modify equips to the best
        register(response -> {
            int cmdId = JsonUtils.resolveCmdId(response);
            if (cmdId != 1001) {
                return Optional.empty();
            }
            ObjectNode root = (ObjectNode) response;
            ArrayNode equips = (ArrayNode) root.get("equips");
            equips.elements().forEachRemaining(_equip -> {
                ObjectNode equip = (ObjectNode) _equip;
                equip.set("level", IntNode.valueOf(99));
                equip.set("quality", IntNode.valueOf(505));
                equip.set("sd_id", IntNode.valueOf(5));
            });
            root.set("zhanli", IntNode.valueOf(114514));
            return Optional.of(response);
        });

        // adjust attributes when creating attack
        register(response -> {
            int cmdId = JsonUtils.resolveCmdId(response);
            if (cmdId != 1048) {
                return Optional.empty();
            }
            ObjectNode root = (ObjectNode) response;
            root.set("engineDmg", IntNode.valueOf(4444)); // 战机
            root.set("hp", IntNode.valueOf(88888)); // 装甲
            root.set("weaponDmg", IntNode.valueOf(4444)); // 副武器
            root.set("wingmanDmg", IntNode.valueOf(4444)); // 僚机
            return Optional.of(response);
        });
    }
}
