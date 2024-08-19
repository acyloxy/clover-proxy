package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.request.ModifyBoxCount;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class RequestHandlers {
    private static final List<RequestHandler> REQUEST_HANDLERS = new ArrayList<>();

    static {
        init();
    }

    private static void init() {
        register(new ModifyBoxCount());
    }

    public static void register(RequestHandler handler) {
        REQUEST_HANDLERS.add(handler);
    }

    public static HandleResult handle(ObjectNode request) throws Exception {
        boolean handled = false;
        for (RequestHandler handler : REQUEST_HANDLERS) {
            if (!handler.canHandle(request)) {
                continue;
            }
            try {
                Optional<ObjectNode> opt = handler.handle(request);
                if (opt.isPresent()) {
                    handled = true;
                    request = opt.get();
                }
            } catch (HandlerException e) {
                log.error("[ResponseHandleError]", e);
            }
        }
        return new HandleResult(handled, request);
    }
}
