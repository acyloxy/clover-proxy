package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestHandlers {
    private static final List<RequestHandler> REQUEST_HANDLERS = new ArrayList<>();

    static {
        init();
    }

    private static void init() {
    }

    public static void register(RequestHandler handler) {
        REQUEST_HANDLERS.add(handler);
    }

    public static boolean handle(JsonNode request) throws Exception {
        boolean handled = false;
        for (RequestHandler handler : REQUEST_HANDLERS) {
            Optional<JsonNode> opt = handler.handle(request);
            if (opt.isPresent()) {
                handled = true;
                request = opt.get();
            }
        }
        return handled;
    }
}
