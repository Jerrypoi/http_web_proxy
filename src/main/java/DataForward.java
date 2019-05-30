import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;
public class DataForward implements Runnable {
    private InputStream in;
    private OutputStream out;
    private Socket remoteSocket;

    public DataForward(InputStream in, OutputStream out, Socket remoteSocket) {
        this.in = in;
        this.out = out;
        this.remoteSocket = remoteSocket;
    }
    private static final Logger logger = Logger.getLogger(DataForward.class.getName());
    public void run() {
        int c;
        byte[] buffer = new byte[1024];
        try {
            while ((c = in.read(buffer)) > 0) {
                out.write(buffer,0,c);
                out.flush();
//                System.out.print((char)c);
            }
        }
        catch (SocketException e) {
//            logger.warning("remote:" + remoteSocket.getInetAddress());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
