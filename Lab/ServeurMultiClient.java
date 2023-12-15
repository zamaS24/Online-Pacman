import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class ServeurMultiClient extends Thread {



    private int numClient = 0;
    private int secretNumber;
    private List<Repartiteur> clients = new ArrayList<>();
    private int chosenClient = 0; 


    public ServeurMultiClient() {
        // Generating a secret number
        secretNumber = (new Random().nextInt(100)) * 100;
        System.out.println("Server has chosen a secret number: " + secretNumber);
    }

   
    @Override
    public void run() {

        try {
            ServerSocket s_server = new ServerSocket(1234);

            // choisir un nombre aleatoir
            Timer timer = new Timer();
            List <Repartiteur> previousChosenClient = new ArrayList<>(); 

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    
                    chooseRandomClient();                  
                    int chosen = getChosenClient(); 
                    chosenClient = getChosenClient(); 

                    System.out.println("chosen client is :  " +chosen);
                }
            }, 0, 20000); 


            while (true) {
                
                Socket s = s_server.accept();
                ++numClient;
                Repartiteur repartiteur = new Repartiteur(s, numClient, this);
                clients.add(repartiteur);
                repartiteur.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public int getSecretNumber() {
        return secretNumber;
    }



    public void announceWinner(String winnerIp) {
        for (Repartiteur repartiteur : clients) {
            repartiteur.sendAnnouncement("Winner's IP: " + winnerIp);
        }
    }


    public void closeAllConnections() {
        for (Repartiteur repartiteur : clients) {
            repartiteur.sendAnnouncement("Game has ended! Server is closing. Goodbye!");
            try {
                //repartiteur.join();
                repartiteur.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }


    public void chooseRandomClient(){
     this.chosenClient = new Random().nextInt(5) + 1;
    }



    public int getChosenClient(){
        return  this.chosenClient; 
    }


    public static void main(String[] args) {
        new ServeurMultiClient().start();
    }




}


