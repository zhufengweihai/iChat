package de.cgrotz.kademlia.config;

import java.util.ArrayList;
import java.util.List;

import de.cgrotz.kademlia.exception.UnknownListenerType;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Christoph on 30.09.2016.
 */
@Data
@Builder
public class Listener {
    private ListenerType type;

    public static Listener fromUrl(String url) {
        if (url.startsWith(ListenerType.UDP.prefix())) {
            return UdpListener.from(url);
        } else {
            throw new UnknownListenerType(url);
        }
    }

    public static List<Listener> fromUrls(String urls) {
        String[] urlSplits = urls.split(",");
        List<Listener> listeners = new ArrayList<>();
        for (int i = 0; i < urlSplits.length; i++) {
            listeners.add(Listener.fromUrl(urlSplits[i]));
        }
        return listeners;
    }
}
