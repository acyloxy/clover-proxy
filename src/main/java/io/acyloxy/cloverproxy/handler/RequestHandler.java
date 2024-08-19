package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

public interface RequestHandler {
    boolean canHandle(ObjectNode request);

    Optional<ObjectNode> handle(ObjectNode request) throws Exception;
}
