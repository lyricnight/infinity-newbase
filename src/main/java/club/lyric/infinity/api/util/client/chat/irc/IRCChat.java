package club.lyric.infinity.api.util.client.chat.irc;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.events.irc.IRCEvent;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * @author chelldev/finz0
 */
public class IRCChat implements IMinecraft {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final WebSocket.Builder builder = client.newWebSocketBuilder();
    private static WebSocket ws = null;
    private static final Gson gson = new Gson();
    private static final String nick = mc.getSession().getUsername();
    private static final String channel = "infinityukclient";

    public static boolean connected = false;

    public static void join() {
        try {
            ws = builder.buildAsync(URI.create("wss://hack.chat/chat-ws"), new Listener()).get();
            ws.sendText(gson.toJson(new JoinCommand(channel, nick)), true);
            connected = true;
        } catch (Exception e) {
            Infinity.LOGGER.atError();
        }
    }

    public static void chat(String text) {
        if (ws != null) {
            ws.sendText(gson.toJson(new ChatCommand(text)), true);
        }
    }

    public static void disconnect() {
        if (ws != null) {
            ws.sendText(gson.toJson(new DisconnectCommand()), true);
            try {
                ws.sendClose(1000, "Disconnected").get(3, TimeUnit.SECONDS);
                connected = false;
            } catch (Exception e) {
                Infinity.LOGGER.atError();
            }
            ws = null;
        }
    }

    private static class Listener implements WebSocket.Listener {
        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            if (data.toString().startsWith("{\"cmd\":\"chat\"")) {
                ReceivedChat message = gson.fromJson(data.toString(), ReceivedChat.class);
                EventBus.getInstance().post(new IRCEvent(message.nick, message.text.replace("\n", " ")));
            }
            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            Infinity.LOGGER.atError();
            WebSocket.Listener.super.onError(webSocket, error);
        }
    }

    private static class JoinCommand {
        private final String cmd = "join";
        private final String channel;
        private final String nick;

        JoinCommand(String channel, String nick) {
            this.channel = channel;
            this.nick = nick;
        }
    }

    private static class ChatCommand {
        private final String cmd = "chat";
        private final String text;

        ChatCommand(String text) {
            this.text = text;
        }
    }

    private static class DisconnectCommand {
        private final String cmd = "disconnect";
    }

    private static class ReceivedChat {

        private final String nick;
        private final String text;
        private final long time;

        ReceivedChat(String nick, String text, long time) {
            this.nick = nick;
            this.text = text;
            this.time = time;
        }
    }
}