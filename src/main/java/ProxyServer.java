
import javax.activation.DataHandler;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ProxyServer {
    public static final int port = 444;
    ServerSocket socket;
    public ProxyServer() {
        try {
            this.socket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (true) {
            try {
                Socket client = socket.accept();
                logger.info("new socket bind from addr:" + client.getInetAddress());
                new Thread(new SocketHandle(client)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String []args) {
        ProxyServer server = new ProxyServer();
        server.run();
    }
    private static final Logger logger = Logger.getLogger(ProxyServer.class.getName());
}

class SocketHandle implements Runnable {
    private static final Logger logger = Logger.getLogger(SocketHandle.class.getName());
    private Socket socket;
    public SocketHandle(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        OutputStream clientOutput = null;
        InputStream clientInput = null;
        Socket proxySocket = null;
        InputStream proxyInput = null;
        OutputStream proxyOutput = null;
        try {
            clientInput = socket.getInputStream();
            clientOutput = socket.getOutputStream();
            int c;
            StringBuilder header = new StringBuilder();
            while ((c = clientInput.read()) != -1) {
                header.append((char) c);
                System.out.print((char) c);
                int len = header.length();
                if (header.charAt(len - 1) == '\n'
                        && header.charAt(len - 2) == '\r'
                        && header.charAt(len - 3) == '\n'
                        && header.charAt(len - 4) == '\r') {
                    break;
                }
            }

            String head = new String(header);
            if (head.length() == 0) {
                return;
            }
            logger.info(head);
            String loc = getLoc(head);
            int port = 80;
            if (loc.contains(":")) {
                String[] fields = loc.split(":");
                port = Integer.parseInt(fields[1]);
                loc = fields[0];
                // TODO: Error finding the port
            }
            proxySocket = new Socket(loc,port);
            proxyInput = proxySocket.getInputStream();
            proxyOutput = proxySocket.getOutputStream();
            byte[] data = head.getBytes();
            proxyOutput.write(data);
            while ((c = proxyInput.read()) != -1) {
                clientOutput.write(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            proxyInput.close();
            proxyOutput.close();
            clientInput.close();
            clientOutput.close();
            socket.close();
            proxySocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private static String getLoc(String header) {
        int host_index = header.indexOf("Host: ");
        String[]fields = header.split("( |\\r\\n)");
        try {
            return fields[4];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            logger.warning(header);

        }
        return null;
    }
}