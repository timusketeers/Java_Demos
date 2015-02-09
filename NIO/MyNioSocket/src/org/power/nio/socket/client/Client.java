package org.power.nio.socket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;


/**
 * 客户端
 * @author li.zhang
 * 2014-9-18
 *
 */
public class Client
{
    private Selector selector;
    
    private SocketChannel channel;
    
    public Client()
    {
        init();
    }
    
    private void init()
    {
        String hostname = "127.0.0.1";
        int port = 9090;
        SocketAddress remote = new InetSocketAddress(hostname, port);
        try
        {
            selector = Selector.open();
            channel = SocketChannel.open(remote);
            channel.configureBlocking(false);//使用非阻塞模式
            
            //注册channel该渠道感兴趣的事件
            channel.register(selector, SelectionKey.OP_CONNECT);
            
            //socket就绪事件处理线程.
            new SocketReadyThread(selector).start();
            
            //交互线程.
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("【客户端】：");
                    while (scanner.hasNextLine())
                    {
                        System.out.print("【客户端】：");
                        String line = scanner.nextLine();
                        try
                        {
                            /**
                             * 往这个channel中写的数据，会在其对应的socket处于writable状态的时候，执行
                             * 
                             * SocketReadyThread线程中if分支为isWritable()条件中的语句.
                             * 
                             * 这里才真正体现了往channel中写入是非阻塞方式写入的.
                             * 
                             * SelectionKey关联一个message,等到对应的socket是可写状态的时候，将数据真正写入.
                             */
                            ByteBuffer message = ByteBuffer.wrap(line.getBytes());
                            SelectionKey key = channel.register(selector, SelectionKey.OP_WRITE);
                            key.attach(message);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                
            }).start();
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new Client();
        
        while (true);
    }
    
}

/**
 * socket就绪事件处理线程.
 * @author li.zhang
 * 2014-9-18
 *
 */
class SocketReadyThread extends Thread
{
    /** selector **/
    private Selector selector;
    
    /**
     * 线程.
     * @param selector selector
     * @param args 参数列表
     */
    public SocketReadyThread(Selector selector)
    {
        this.selector = selector;
    }
    
    public void run()
    {
        while(true)
        {
            try
            {
                if (selector.select() <= 0)
                {
                    continue;
                }
                
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext())
                {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    
                    if (key.isValid() && key.isConnectable())
                    {
                        System.out.println("client key connect...");
                        SocketChannel client = (SocketChannel)key.channel();
                        client.register(selector, SelectionKey.OP_READ);
                        client.finishConnect();
                    }
                    //Tests whether this key's channel is ready for reading.
                    else if (key.isValid() && key.isReadable())
                    {
                        System.out.println("client key read...");
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel client = (SocketChannel)key.channel();

                        while ((client.read(buffer)) != 0);
                        buffer.flip();
                        
                        byte[] dst = new byte[buffer.limit()];
                        buffer.get(dst);

                        System.out.println("【服务端】：" + new String(dst));
                    }
                    //Tests whether this key's channel is ready for writing.
                    else if (key.isValid() && key.isWritable())
                    {
                        SocketChannel client = (SocketChannel)key.channel();
                        ByteBuffer message = (ByteBuffer)key.attachment();
                        message.flip();
                        client.write(message);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
