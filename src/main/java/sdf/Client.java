package sdf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket clientConn = new Socket("localhost", 12345); 
        System.out.println("Connected to server on localhost:12345");

        try(OutputStream os = clientConn.getOutputStream()){
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            Console cons = System.console();
            String readInput = "", receivedMessage = "";

            try(InputStream is = clientConn.getInputStream()) {
                BufferedInputStream bis = new BufferedInputStream(is);
                DataInputStream dis = new DataInputStream(bis);

                while(!readInput.equalsIgnoreCase("close")){
                    readInput = cons.readLine("Enter a command: ");
                    dos.writeUTF(readInput);
                    dos.flush();

                    receivedMessage = dis.readUTF();
                    System.out.println(receivedMessage);
                } 
                dis.close();
                bis.close();
                is.close();
            } catch (EOFException ex) {
                clientConn.close();
            }
            dos.close();
            bos.close();
        } catch (EOFException ex) {
            clientConn.close();
        }
        
    }
    
}
