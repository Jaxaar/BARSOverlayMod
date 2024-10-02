package Jaxaar.BARSOverlay.HttpRequests;

import net.hypixel.api.reply.AbstractReply;

import java.util.UUID;

public class MojangPlayerData extends AbstractReply {

    public String id;
    public String name;

    public UUID getUUID(){
        return UUID.fromString(id.substring(0,8) + "-" + id.substring(8,12) + "-" + id.substring(12, 16) + "-" + id.substring(16,20) + "-" + id.substring(20));

    }

    @Override
    public String toString() {
        return "MojangPlayerData{" +
                "uuid=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
