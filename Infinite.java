package javaapplication40;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Infinite
{
    int CurrentMove;

    Infinite()
    {
        CurrentMove = 1;
    } 

    public static void main (String[] args)
    {
        System.out.println("Give the number of players: ");
        Scanner keyboard = new Scanner(System.in);
        Integer N = keyboard.nextInt();
        
        System.out.println("Give the number of loops: ");
        Integer Loops = keyboard.nextInt();
        
        Infinite BG = new Infinite ();
        ArrayList<Thread> players = new ArrayList<>();
        
        int count=1;
        for(int i=0; i<N; i++){
            Thread Player = new MyThread(BG, N, Loops, count);
            players.add(Player);
            count++;
        }
        
        players.forEach((p) -> {
            p.start();
        });
        
        players.forEach((p) -> {
            try {
                p.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Infinite.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
       
        System.out.println ("Thats All");
    }

}


class MyThread extends Thread
{
    Infinite Board;
    Integer N;
    Integer player;
    Integer turn;
    Integer Loops;
    public MyThread (Infinite B, int N, int Loops , int player)
    {
        this.turn = player;
        Board = B;  this.N=N; this.player=player; this.Loops=Loops;
    }
    
    
    
    @Override
    public void run ()
    {
        boolean EndIt = false;
        
        while (!EndIt)
        {
            synchronized (Board)//κάνω synchronized τον πόρο που δεν θέλω να χρησιμοποιηθεί από κάποιο άλλο thread
            {
                while (Board.CurrentMove != turn)//αν το υπόλοιπο της διαίρεσης είναι διάφορο του 1
                {                            //αν είναι διάφορο του 1, σημαίνει ότι ειναι η σειρά του BlackPlayer να παίξει
                    try                     //Οσο δεν είναι η σειρά μου να παίξω
                    {
                        Board.wait ();    //Κοιμάμαι μέχρι να με ξυπνήσει κάποιος (notify)
                    }
                    catch (InterruptedException ex)
                    {
                        
                    }
                }
                System.out.println ("Do Move : " + Board.CurrentMove + " Player: " + player);
                Board.CurrentMove++;
                turn=turn+N;
                Board.notifyAll ();
            }
            //System.out.println(turn );
        if ((Board.CurrentMove + N) > (Loops+1)) {EndIt = true;}
                
        }
    }
}
