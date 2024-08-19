package io.acyloxy.cloverproxy.handler.response;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.acyloxy.cloverproxy.handler.AbstractResponseHandler;
import io.acyloxy.cloverproxy.handler.Command;
import io.acyloxy.cloverproxy.handler.HandlerException;
import io.acyloxy.cloverproxy.util.StreamUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModifyEquipments extends AbstractResponseHandler {
    @Override
    public int getCmdId() {
        return Command.GET_BASIC_INFO_RESP;
    }

    @Override
    public Optional<ObjectNode> handle(ObjectNode response) {
        try {
            ArrayNode equipments = (ArrayNode) response.get("equips");
            for (EquipmentAdapter equipment : currentEquipments(equipments)) {
                equipment.setLevel(99);
                equipment.setQuality(505);
                equipment.setSdId(5);
            }
            response.set("zhanli", IntNode.valueOf(114514));
            return Optional.of(response);
        } catch (Exception e) {
            throw new HandlerException(e);
        }
    }

    private EquipmentAdapter[] currentEquipments(ArrayNode equipments) {
        EquipmentAdapter[] current = new EquipmentAdapter[4];
        StreamUtils.asStream(equipments.elements())
                .map(node -> (ObjectNode) node)
                .map(EquipmentAdapter::new)
                .collect(Collectors.groupingBy(EquipmentAdapter::getType))
                .forEach((type, equipmentsOfType) -> {
                    EquipmentAdapter equipment = null;
                    for (EquipmentAdapter e : equipmentsOfType) {
                        if (e.getState() == 1) {
                            equipment = e;
                            break;
                        }
                    }
                    current[type] = Objects.requireNonNull(equipment);
                });
        return current;
    }

    private EquipmentAdapter[] worstEquipments(ArrayNode equipments) {
        EquipmentAdapter[] worst = new EquipmentAdapter[4];
        StreamUtils.asStream(equipments.elements())
                .map(node -> (ObjectNode) node)
                .map(EquipmentAdapter::new)
                .collect(Collectors.groupingBy(EquipmentAdapter::getType))
                .forEach((type, equipmentsOfType) -> {
                    equipmentsOfType.sort(EquipmentAdapter.QUALITY_COMPARATOR);
                    worst[type] = equipmentsOfType.getFirst();
                });
        return worst;
    }
}
