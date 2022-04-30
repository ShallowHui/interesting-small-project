import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client2Main {

    private static final String CLIENT_NAME = "夜兰";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 6666);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(CLIENT_NAME.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int i = inputStream.read(bytes);
            System.out.println(new String(bytes, 0, i, StandardCharsets.UTF_8));
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("请输入要发送给服务端的内容：");
                String s = scanner.nextLine();
                if ("END".equals(s))
                    break;
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
