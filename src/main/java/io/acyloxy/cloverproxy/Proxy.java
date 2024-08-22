package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.HandleResult;
import io.acyloxy.cloverproxy.handler.Preset;
import io.acyloxy.cloverproxy.handler.RequestHandlers;
import io.acyloxy.cloverproxy.handler.ResponseHandlers;
import io.acyloxy.cloverproxy.util.CryptoUtils;
import io.acyloxy.cloverproxy.util.HttpClientUtils;
import io.acyloxy.cloverproxy.util.JsonUtils;
import io.acyloxy.cloverproxy.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class Proxy implements DisposableBean {
    private final CloseableHttpClient client = HttpClientUtils.createClient();

    public Proxy() throws Exception {
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }

    @PostMapping("/p.f")
    public byte[] handle(@RequestBody byte[] encryptedRequest) throws Exception {
        // request
        Pair<ObjectNode, Integer> plaintext = decrypt(encryptedRequest);
        ObjectNode request = plaintext.getLeft();
        int sequence = plaintext.getRight();
        String before = request.toPrettyString();
        HandleResult result = RequestHandlers.handle(request);
        request = result.getResult();
        if (result.isHandled() && Preset.SHOW_DIFF) {
            log.info("[RequestHandle]\n{}", StringUtils.diff(before, request.toPrettyString(), "before", "after"));
        }
        HttpPost post = new HttpPost("https://140.143.214.183/p.f");
        post.setEntity(HttpEntities.create(encrypt(request, sequence), ContentType.APPLICATION_OCTET_STREAM));

        // response
        plaintext = decrypt(client.execute(post, resp -> EntityUtils.toByteArray(resp.getEntity())));
        ObjectNode response = plaintext.getLeft();
        sequence = plaintext.getRight();
        before = response.toPrettyString();
        result = ResponseHandlers.handle(response);
        response = result.getResult();
        if (result.isHandled() && Preset.SHOW_DIFF) {
            log.info("[RequestHandle]\n{}", StringUtils.diff(before, response.toPrettyString(), "before", "after"));
        }
        return encrypt(response, sequence);
    }

    private byte[] encrypt(ObjectNode message, int sequence) {
        return CryptoUtils.encrypt(message.toString().getBytes(StandardCharsets.UTF_8), sequence);
    }

    private Pair<ObjectNode, Integer> decrypt(byte[] encryptedMessage) throws Exception {
        Pair<byte[], Integer> result = CryptoUtils.decrypt(encryptedMessage);
        byte[] message = result.getLeft();
        int sequence = result.getRight();
        return Pair.of((ObjectNode) JsonUtils.toNode(message), sequence);
    }
}
