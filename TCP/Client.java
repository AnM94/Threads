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
            C2.join(); C1.join(); //Αναμονή τερματισμού και των 2 νημάτων
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("That's All Folks!");
    }
    
}

class ClientThread1 extends Thread{
    Client Cl;
    Boolean EndIt;
    InetAddress address;
    Socket s1=null;
    String name, answer, response=null;
    BufferedReader UserInput;
    BufferedReader is=null; //Για το response του Server
    PrintWriter os=null;

    ClientThread1(String name, Client Cl) throws UnknownHostException {
        this.name=name;
        this.Cl=Cl;
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
            synchronized(Cl){   
                if (Cl.CurrentMove % 2 != 1){
                    try {
                        Cl.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientThread1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 } 
            
                os.println(name + ", " + Cl.CurrentMove);//Request
                os.flush();
                
                try {                           //Response
                    response=is.readLine();
                    System.out.println("Server Response : " + response);
                } catch (IOException ex) {
                    System.out.println("Exception on Server's Response");
                }
                
                Cl.CurrentMove++;
                Cl.notify();
            }
             
            if(Cl.CurrentMove>99){
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

    private void Request(PrintWriter os, int CurrentMove) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}    

class ClientThread2 extends Thread{
    Client Cl;
    Boolean EndIt;
    InetAddress address;
    Socket s1=null;
    String name, answer, response=null;
    BufferedReader UserInput;
    BufferedReader is=null; //Για το response του Server
    PrintWriter os=null;

    ClientThread2(String name, Client Cl) throws UnknownHostException {
        this.name=name;
        this.Cl=Cl;
        this.address=InetAddress.getLocalHost();
    }
        
    @Override
    public void run(){
        try {
            s1=new Socket(address, 4445); // You can use static final constant PORT_NUM
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            Logger.getLogger(ClientThread2.class.getName()).log(Level.SEVERE, null, e);
        }
        
        EndIt=false;
        while(!EndIt){
            synchronized(Cl){
                if (Cl.CurrentMove % 2 != 0){
                    try {
                        Cl.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientThread2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 } 
                
                os.println(name + ", " + Cl.CurrentMove);//Request
                os.flush();
                
                try {                           //Response
                    response=is.readLine();
                    System.out.println("Server Response : " + response);
                } catch (IOException ex) {
                    System.out.println("Exception on Server's Response");
                }
                
                Cl.CurrentMove++;
                Cl.notify();
            }
             
            if(Cl.CurrentMove>100){
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
    

 