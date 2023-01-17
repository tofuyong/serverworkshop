package sdf;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        cookie.showCookies();

        //Creating server 
        ServerSocket ss = new ServerSocket(12345);
        Socket s = ss.accept(); //establish connection and wait for client to connect

        try (InputStream is = s.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            String msgReceived = ""; 

            while (!msgReceived.equals("close")){
                msgReceived = dis.readUTF();

                if (msgReceived.equalsIgnoreCase("get-cookie")){
                    String cookieValue = cookie.returnCookie();
                }
            }
        } catch (EOFException ex){
            s.close();
            ss.close();
        }

    

    }
}
