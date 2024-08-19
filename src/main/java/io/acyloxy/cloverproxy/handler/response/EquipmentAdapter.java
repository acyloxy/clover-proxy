package io.acyloxy.cloverproxy.handler.response;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

@RequiredArgsConstructor
public final class EquipmentAdapter {
    public static final Comparator<EquipmentAdapter> QUALITY_COMPARATOR = Comparator.comparingInt(EquipmentAdapter::getSdId)
            .thenComparingInt(EquipmentAdapter::getQuality)
            .thenComparingInt(EquipmentAdapter::getLevel)
            .thenComparingInt(EquipmentAdapter::getExp);

    private final ObjectNode node;

    public int getExp() {
        return node.get("exp").asInt();
    }

    public void setExp(int exp) {
        node.set("exp", IntNode.valueOf(exp));
    }

    public int getLevel() {
        return node.get("level").asInt();
    }

    public void setLevel(int level) {
        node.set("level", IntNode.valueOf(level));
    }

    public int getQuality() {
        return node.get("quality").asInt();
    }

    public void setQuality(int quality) {
        node.set("quality", IntNode.valueOf(quality));
    }

    public int getSdId() {
        return node.get("sd_id").asInt();
    }

    public void setSdId(int sdId) {
        node.set("sd_id", IntNode.valueOf(sdId));
    }

    public int getState() {
        return node.get("state").asInt();
    }

    public void setState(int state) {
        node.set("state", IntNode.valueOf(state));
    }

    public int getType() {
        return node.get("type").asInt();
    }

    public void setType(int type) {
        node.set("type", IntNode.valueOf(type));
    }
}
