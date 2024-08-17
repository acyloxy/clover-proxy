package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

@FunctionalInterface
public interface ResponseHandler {
    Optional<JsonNode> handle(JsonNode response) throws Exception;
}
