package io.acyloxy.cloverproxy.handler;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class HandleResult {
    private final boolean handled;

    private final ObjectNode result;
}
