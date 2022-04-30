import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerMain {

    private static final int PORT = 6666;

    private static final Map<SocketChannel, String> ClientMap = new HashMap<>();

    private static final String HELLO = "Server Hello";

    public static void main(String[] args) {
        try {
            System.out.println("正在初始化服务器...");
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            System.out.println("服务器初始化完毕，正在监听" + PORT + "端口...");

            // 创建Selector并注册ServerSocketChannel
            Selector selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            // 启动一个线程去让Selector执行监听
            Thread workThread = new Thread(() -> {
                while (true) {
                    try {
                        int r = selector.select();
                        System.out.println("Selector监听到" + r + "个Channel事件...");
                        if (r == 0)
                            continue;
                        // 获取可以进行IO的Channel的SelectionKey集合
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        // 获得集合的迭代器对象
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            iterator.remove();
                            if (selectionKey.isAcceptable()) {
                                SocketChannel socketChannel = serverSocketChannel.accept();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                int i = socketChannel.read(byteBuffer);
                                byte[] array = byteBuffer.array();
                                String clientName = new String(array, 0, i, StandardCharsets.UTF_8);
                                ClientMap.put(socketChannel, clientName);
                                System.out.println(clientName + "已连接...");
                                byteBuffer.clear();
                                byteBuffer.put(HELLO.getBytes(StandardCharsets.UTF_8));
                                byteBuffer.flip();
                                socketChannel.write(byteBuffer);
                                socketChannel.configureBlocking(false);
                                socketChannel.register(selector, SelectionKey.OP_READ);
                            }
                            // 不管客户端是正常还是非正常断开连接，都会产生一个read事件
                            if (selectionKey.isReadable()) {
                                // 创建非直接缓冲区
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                // 从SelectionKey获取就绪的Channel
                                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                                try {
                                    int i = socketChannel.read(byteBuffer);
                                    // 客户端正常断开连接，说明通道中的数据结束了，读通道只会返回-1
                                    // 如果客户端非正常断开连接，则读通道会抛出异常
                                    if (i == -1) {
                                        System.out.println(ClientMap.get(socketChannel) + "正常断开连接...");
                                        ClientMap.remove(socketChannel);
                                        socketChannel.close();
                                    } else {
                                        String clientName = ClientMap.get(socketChannel);
                                        byte[] array = byteBuffer.array();
                                        String content = new String(array, 0, i, StandardCharsets.UTF_8);
                                        System.out.println("<====== " + clientName + " 发来消息: " + content);
                                    }
                                } catch (IOException e) {
                                    System.out.println(ClientMap.get(socketChannel) + "非正常断开连接...");
                                    ClientMap.remove(socketChannel);
                                    socketChannel.close();
                                    e.printStackTrace();
                                }
                            }
                            System.out.println("系统当前在线人数：" + ClientMap.size());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            workThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}