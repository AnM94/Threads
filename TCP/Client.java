package tcp_server.many_clients__automatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    
    int CurrentMove;
    
    Client(){
       CurrentMove=1;
    }
    
    public static void main(String args[]) throws IOException{
        Client Cl = new Client();
        ClientThread1 C1 = new ClientThread1("First", Cl); ClientThread2 C2 = new ClientThread2("Second", Cl);
        C1.start(); C2.start();
        try {
            C2.join(); C1.join(); 
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("That's All Folks!");
    }
}

class ClientThread1 extends Thread{
    Client C;
    Boolean EndIt;
    InetAddress address;
    Socket s1=null;
    String name, answer, response=null;
    BufferedReader UserInput;
    BufferedReader is=null;
    PrintWriter os=null;

    ClientThread1(String name, Client C) throws UnknownHostException {
        this.name=name;
        this.C=C;
        this.address=InetAddress.getLocalHost();
    }
        
    @Override
    public void run(){
        try {
            s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(ClientThread1.class.getName()).log(Level.SEVERE, null, e);
        }
        
        EndIt=false;
        while(!EndIt){
            synchronized(C){   
                if (C.CurrentMove % 2 != 1){
                    try {
                        C.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientThread1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 } 
            
                os.println(name + ", " + C.CurrentMove);
                os.flush();
                
                try {                        
                    response=is.readLine();
                    System.out.println("Server Response : " + response);
                } catch (IOException ex) {
                    System.out.println("Exception on Server's Response");
                }
                
                C.CurrentMove++;
                C.notify();
            }
             
            if(C.CurrentMove>99){
                os.println("no");
                EndIt = true;
            }
        }
        
        try {
            is.close();os.close();s1.close();
            System.out.println("Connection Closed");
        } catch (IOException ex) {
            Logger.getLogger(ClientThread1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}    

class ClientThread2 extends Thread{
    Client C;
    Boolean EndIt;
    InetAddress address;
    Socket s1=null;
    String name, answer, response=null;
    BufferedReader UserInput;
    BufferedReader is=null; 
    PrintWriter os=null;

    ClientThread2(String name, Client C) throws UnknownHostException {
        this.name=name;
        this.C=C;
        this.address=InetAddress.getLocalHost();
    }
        
    @Override
    public void run(){
        try {
            s1=new Socket(address, 4445); 
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(ClientThread2.class.getName()).log(Level.SEVERE, null, e);
        }
        
        EndIt=false;
        while(!EndIt){
            synchronized(C){
                if (C.CurrentMove % 2 != 0){
                    try {
                        C.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientThread2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 } 
                
                os.println(name + ", " + C.CurrentMove);
                os.flush();
                
                try {                           
                    response=is.readLine();
                    System.out.println("Server Response : " + response);
                } catch (IOException ex) {
                    System.out.println("Exception on Server's Response");
                }
                
                C.CurrentMove++;
                C.notify();
            }
             
            if(C.CurrentMove>100){
                os.println("no");
                EndIt = true;
            }
        }
        
        try {
            is.close();os.close();s1.close();
            System.out.println("Connection Closed");
        } catch (IOException ex) {
            Logger.getLogger(ClientThread2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}    
    

 
