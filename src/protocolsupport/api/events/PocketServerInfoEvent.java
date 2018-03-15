package protocolsupport.api.events;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Event;

import java.util.Objects;

public class PocketServerInfoEvent extends Event {

    private String welcomeMessage;
    private int online;
    private int serverSize;

    public PocketServerInfoEvent(String welcomeMessage, int online, int serverSize) {
        this.welcomeMessage = Objects.requireNonNull(welcomeMessage);
        this.online = online;
        this.serverSize = serverSize;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = Objects.requireNonNull(welcomeMessage);
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getServerSize() {
        return serverSize;
    }

    public void setServerSize(int serverSize) {
        this.serverSize = serverSize;
    }

    public static PocketServerInfoEvent call(String welcome) {
        BungeeCord bungee = BungeeCord.getInstance();
        PocketServerInfoEvent event = new PocketServerInfoEvent(welcome, bungee.getOnlineCount(), bungee.config.getPlayerLimit());
        bungee.getPluginManager().callEvent(event);
        return event;
    }

}
