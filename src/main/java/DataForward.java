import com.sun.tools.internal.ws.wsdl.document.Output;
import jdk.internal.util.xml.impl.Input;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

public class DataForward implements Runnable{
//    private Socket sourceSocket;
//    private Socket remoteSocket;
//
//    public DataForward(Socket sourceSocket, Socket remoteSocket) {
//        this.sourceSocket = sourceSocket;
//        this.remoteSocket = remoteSocket;
//    }
//    public void run() {
//        try {
//            InputStream source_in;
//            InputStream remote_in;
//            OutputStream source_out;
//            OutputStream remote_out;
//            source_in = sourceSocket.getInputStream();
//            source_out = sourceSocket.getOutputStream();
//            remote_in = remoteSocket.getInputStream();
//            remote_out = remoteSocket.getOutputStream();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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
        try {
            while ((c = in.read()) != -1) {
                out.write(c);
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
