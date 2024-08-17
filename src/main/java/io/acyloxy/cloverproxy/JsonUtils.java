package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static JsonNode toNode(String json) throws JsonProcessingException {
        return MAPPER.readTree(json);
    }

    public static JsonNode toNode(Object o) {
        return MAPPER.valueToTree(o);
    }

    public static int resolveCmdId(JsonNode message) {
        ObjectNode root = (ObjectNode) message;
        ObjectNode head = (ObjectNode) root.get("head");
        return head.get("cmdId").asInt();
    }
}
