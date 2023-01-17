package sdf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String dirPath = "data2";

        File newDirectory = new File(dirPath);

        if (newDirectory.exists()){
            System.out.println("Directory already exists");
        } else {
            newDirectory.mkdir();
        }

        Cookie cookie = new Cookie();
        cookie.readCookieFile();
        // cookie.showCookies();

        //Creating server 
        ServerSocket ss = new ServerSocket(12345);
        System.out.println("Waiting for incoming connection");
        Socket s = ss.accept(); //establish connection and wait for client to connect
        System.out.println("Got a connection");

        try (InputStream is = s.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            String msgReceived = ""; 

            try(OutputStream os = s.getOutputStream()){
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                while (!msgReceived.equals("close")){
                    msgReceived = dis.readUTF();
    
                    if (msgReceived.equalsIgnoreCase("get-cookie")){
                        String cookieValue = cookie.returnCookie();
                        System.out.println(cookieValue);

                        dos.writeUTF(cookieValue);
                        dos.flush();
                    }
                }
                dos.close();
                bos.close();
                os.close();
            } catch (EOFException ex){  //can put in one try catch block instead of nested 
                ex.printStackTrace();
            }  
        } catch (EOFException ex){
            s.close();
            ss.close();
        }
    }
}
