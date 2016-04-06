package software_project.simulationapp;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * Created by garrettuner on 3/30/16.
 */
public class SimServer extends WebSocketServer
{

    private String msg;

    //Code from https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ChatServer.java
    public SimServer(int port) throws UnknownHostException
    {
        super( new InetSocketAddress(port));
        msg = "";
    }

    public SimServer(InetSocketAddress address)
    {
        super(address);
        msg = "";
    }
    //End of borrowed code



    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println(conn + ": Connected");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + ": Disconnected");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        msg = message;
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error");
    }

    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }

    public String getString()
    {
        return msg;
    }
}
