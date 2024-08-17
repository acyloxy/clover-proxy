package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

@FunctionalInterface
public interface RequestHandler {
    Optional<JsonNode> handle(JsonNode request) throws Exception;
}
