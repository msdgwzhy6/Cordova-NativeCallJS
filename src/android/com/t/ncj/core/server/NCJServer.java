package com.t.ncj.core.server;

import android.util.Log;

import com.t.ncj.core.callback.NCJEventCallback;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * Created by Taro on 2016/8/26.
 */
public class NCJServer extends WebSocketServer {

    public static final String TAG = NCJServer.class.getSimpleName();
    private NCJEventCallback callback ;

    public NCJServer(int port) throws UnknownHostException{
        this(new InetSocketAddress(port));
    }

    public NCJServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        if( callback != null ){
            if( message.equals("nm") ){
                Log.e(TAG,"没有找到需要执行的JavaScript方法!");
            }
            callback.back(message);
            callback = null ;
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        String exString = e.getMessage();
    }

    public void sendToClient(String message, NCJEventCallback callback){
        this.callback = callback;
        Collection<WebSocket> wss = this.connections();
        if( wss.size() == 0 ){
            Log.e(TAG,"没有客户端连接上服务器,请检查是否调用NCJ.init方法!");
            return;
        }
        synchronized (wss){
            for ( WebSocket conn : wss ) {
                if( conn != null ) conn.send(message);
            }
        }
    }
}