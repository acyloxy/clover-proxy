package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.HandleResult;
import io.acyloxy.cloverproxy.handler.RequestHandlers;
import io.acyloxy.cloverproxy.handler.ResponseHandlers;
import io.acyloxy.cloverproxy.util.HttpClientUtils;
import io.acyloxy.cloverproxy.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
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
    public ObjectNode handle(@RequestBody ObjectNode request) throws Exception {
        // request
        String before = request.toPrettyString();
        HandleResult result = RequestHandlers.handle(request);
        request = result.getResult();
        if (result.isHandled()) {
            log.info("[RequestHandle]\n----------before----------\n{}\n----------after----------\n{}", before, request.toPrettyString());
        }
        HttpPost post = new HttpPost("https://140.143.214.183/p.f");
        post.setEntity(HttpEntities.create(request.toString(), ContentType.APPLICATION_JSON));

        // response
        ObjectNode response = (ObjectNode) JsonUtils.toNode(client.execute(post, resp -> EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8)));
        before = response.toPrettyString();
        result = ResponseHandlers.handle(response);
        response = result.getResult();
        if (result.isHandled()) {
            log.info("[RequestHandle]\n----------before----------\n{}\n----------after----------\n{}", before, response.toPrettyString());
        }
        return response;
    }
}
