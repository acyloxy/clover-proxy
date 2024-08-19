package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.util.JsonUtils;

public abstract class AbstractResponseHandler implements ResponseHandler {
    @Override
    public boolean canHandle(ObjectNode response) {
        return JsonUtils.resolveCmdId(response).map(cmdId -> cmdId == getCmdId()).orElse(false);
    }

    public abstract int getCmdId();
}
