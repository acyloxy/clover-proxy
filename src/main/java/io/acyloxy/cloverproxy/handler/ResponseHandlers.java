package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.response.ModifyBattleAttributes;
import io.acyloxy.cloverproxy.handler.response.ModifyEquipments;
import io.acyloxy.cloverproxy.handler.response.ModifyPowerOnEquipmentChange;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ResponseHandlers {
    private static final List<ResponseHandler> RESPONSE_HANDLERS = new ArrayList<>();

    static {
        init();
    }

    private static void init() {
        register(new ModifyBattleAttributes());
        register(new ModifyEquipments());
        register(new ModifyPowerOnEquipmentChange());
    }

    public static void register(ResponseHandler handler) {
        RESPONSE_HANDLERS.add(handler);
    }

    public static HandleResult handle(ObjectNode response) throws Exception {
        boolean handled = false;
        for (ResponseHandler handler : RESPONSE_HANDLERS) {
            if (!handler.canHandle(response)) {
                continue;
            }
            try {
                Optional<ObjectNode> opt = handler.handle(response);
                if (opt.isPresent()) {
                    handled = true;
                    response = opt.get();
                }
            } catch (HandlerException e) {
                log.error("[ResponseHandleError]", e);
            }
        }
        return new HandleResult(handled, response);
    }
}
