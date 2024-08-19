package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.util.JsonUtils;

public abstract class AbstractRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(ObjectNode request) {
        return JsonUtils.resolveCmdId(request).map(cmdId -> cmdId == getCmdId()).orElse(false);
    }

    public abstract int getCmdId();
}
