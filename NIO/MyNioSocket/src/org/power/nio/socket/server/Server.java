package org.power.nio.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 服务端
 * @author li.zhang 2014-9-18
 * 
 */
public class Server
{
    private Selector selector;

    public Server()
    {
        init();
    }

    private void init()
    {
        try
        {
            selector = Selector.open();

            int port = 9090;
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //socket就绪事件处理线程.
        new SocketReadyThread(selector).start();
    }
    
    public static void main(String[] args)
    {
        new Server();
        while (true);
    }
}


/**
 * 响应接收线程.
 * 
 * @author li.zhang 2014-9-18
 * 
 */
class SocketReadyThread extends Thread
{
    private Selector selector;

    /**
     * 构造方法
     * @param selector
     */
    SocketReadyThread(Selector selector)
    {
        this.selector = selector;
    }

    public void run()
    {
        while (true)
        {
            try
            {
                if (selector.select() <= 0)
                {
                    continue;
                }
                
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext())
                {
                    SelectionKey key = iter.next();
                    iter.remove();
                    
                    if (key.isValid() && key.isAcceptable())
                    {
                        ServerSocketChannel server = (ServerSocketChannel)key.channel();
                        final SocketChannel client = server.accept();
                        System.out.println("Accept connection from " + client);
                        client.configureBlocking(false);
                        
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    //这里是服务端正式向socket写数据
                    else if (key.isValid() && key.isWritable())
                    {
                        SocketChannel client = (SocketChannel)key.channel();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        if (null != buffer)
                        {
                            buffer.flip();
                            client.write(buffer);
                        }
                    }
                    else if (key.isValid() && key.isReadable())
                    {
                        System.out.println("server key read...");
                        SocketChannel client = (SocketChannel)key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        while ((client.read(buffer)) != 0);
                        buffer.flip();
                        byte[] dst = new byte[buffer.limit()];
                        buffer.get(dst);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
