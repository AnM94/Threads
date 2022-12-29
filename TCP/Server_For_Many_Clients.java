package tcp_server.many_clients__automatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server_For_Many_Clients {
    public static void main(String args[]){

        Socket s=null;
        ServerSocket ss2=null;
        System.out.println("Server Listening......");
        try{
            ss2 = new ServerSocket(4445); // can also use static final PORT_NUM , when defined
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");
        }

        while(true){
            try{
                s= ss2.accept();
                System.out.println("connection Established");
                System.out.println(ss2.getInetAddress());
                System.out.println(ss2.getLocalPort());
                System.out.println(ss2.getInetAddress().getHostName());

                ServerThread st=new ServerThread(s);
                st.start();
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }
    }
}

class ServerThread extends Thread{  

    String line;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;
    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public ServerThread(Socket s){
        this.s=s;
    }

    public void run() {
        try{
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os=new PrintWriter(s.getOutputStream());

        }catch(IOException e){
            System.out.println("IO error in server thread");
        }

        try {
            line=is.readLine();

            while(line.compareTo("no")!=0){
                os.println(line);
                os.flush();
                System.out.println("Response to Client  :  " + line);
                line=is.readLine();
            }
        } catch (IOException e) {
            line=this.getName(); 
            System.out.println("IO Error/ Client "+line+" terminated abruptly");
        }
        catch(NullPointerException e){
            line=this.getName(); 
            System.out.println("Client "+line+" Closed");
        }

        finally{    
            try{
                System.out.println("Connection Closing..");
                if (is!=null){ is.close();  System.out.println(" Socket Input Stream Closed"); }

                if(os!=null){os.close(); System.out.println("Socket Output Stream Closed"); }
                
                if (s!=null){ s.close(); System.out.println("Socket Closed"); }
            }
            catch(IOException ie){ System.out.println("Socket Close Error"); }
        }//end finally
        System.out.println(dtf.format(now));
    }
}
