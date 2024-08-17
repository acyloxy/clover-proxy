package io.acyloxy.cloverproxy;

import com.fasterxml.jackson.databind.JsonNode;
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
    public JsonNode handle(@RequestBody JsonNode request) throws Exception {
        // request
        String before = request.toString();
        if (RequestHandlers.handle(request)) {
            log.info("[RequestHandle] before: {}, after: {}", before, request);
            return request;
        }
        HttpPost post = new HttpPost("https://140.143.214.183/p.f");
        post.setEntity(HttpEntities.create(request.toString(), ContentType.APPLICATION_JSON));

        // response
        JsonNode response = JsonUtils.toNode(client.execute(post, resp -> EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8)));
        before = response.toString();
        if (ResponseHandlers.handle(response)) {
            log.info("[ResponseHandle] before: {}, after: {}", before, response);
        }
        return response;
    }
}
