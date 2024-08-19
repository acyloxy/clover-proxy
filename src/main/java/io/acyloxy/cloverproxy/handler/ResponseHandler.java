package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

public interface ResponseHandler {
    boolean canHandle(ObjectNode response);

    Optional<ObjectNode> handle(ObjectNode response) throws Exception;
}
