import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/*
 * valeur spéciale choisir randomly  : vous etes le client particulier
 * on lui applique pas une regle
 * 
 * chaque minute 
 * 
 * si pair alors on divise sa requete / 2
 * si impair val * 3 /2 
 * 
 * 
 * 
 * Deuxieme tache : lui dire que ton quantum est eteint
 */



public class Repartiteur extends Thread {
    
    Socket socket;
    private int numClient;
    private ServeurMultiClient server;
    int chosenClient = 0; 
    String chosenString = ""; 

    // Pour la règle; 
    private boolean applyRule;



    public Repartiteur(Socket socket, int numClient, ServeurMultiClient server) {
        super();
        this.socket = socket;
        this.numClient = numClient;
        this.server = server;

        this.applyRule = true;
    }


    public void sendAnnouncement(String message) {
        try {
            OutputStream output = this.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(output, true);
            pw.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void run() {
        
        try {
            InputStream input = socket.getInputStream();
            InputStreamReader ins = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(ins);
            OutputStream output = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(output, true);

            chosenClient = server.getChosenClient(); 

            if (numClient == chosenClient){
                sendAnnouncement("You are the chosen client !\n");
            }

            //pw.println(chosenString + "Welcome! You are client number: " + numClient);
            sendAnnouncement("Welcome! You are client number " + numClient);
            //pw.println(chosenString + "Guess the secret number.");
            sendAnnouncement("Guess the secret number");
          

            while (true) {
                chosenClient = server.getChosenClient(); 

                if (numClient == chosenClient){
                        
                    chosenString=  "chosen client: ";
                } else {
                    chosenString = ""; 
                }
                
                String guessStr = br.readLine();
                int guess = Integer.parseInt(guessStr);

                // Il peut que avant de taper il etait pas chosen client
                // mais une fois tapé il doit vérifier s'il est chosen ou pas
                chosenClient = server.getChosenClient(); 

                if (numClient == chosenClient){
                        
                    chosenString=  "chosen client: ";
                } else {
                    chosenString = ""; 
                }

                if (! (numClient == chosenClient)) {
                    if (numClient % 2 == 1) {
                        guess = (guess * 3) / 2;
                    } else {
                        guess *= 2;
                    }
                }

                // Printing the numver
                System.out.println("Client " + numClient + " guessed: " + guess);



                if (guess < server.getSecretNumber()) {
                    pw.println(chosenString + "The secret number is higher.");
                } else if (guess > server.getSecretNumber()) {
                    pw.println(chosenString + "The secret number is lower.");
                } else {
                    // Correct
                    pw.println("Congratulations! You have won! The secret number is: "+guess);
                    server.announceWinner(socket.getRemoteSocketAddress().toString());

                    server.closeAllConnections();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


/*
 * Liste des messages . 
 * Un serveur et polusieurs clients. 
 */

/*
 * Lancer un server()
 * Attendre les clients pour se connecter. 
 * 
 * commencer une partie
 */


 /*
  * le jeu: Pacman : le réaliser bien sur. 
  */

  /*
   * client : 
   * server : 
   */



   











