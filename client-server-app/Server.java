import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
  @Override
  public void start(Stage primaryStage) {
    // Text area for displaying contents
    TextArea ta = new TextArea();

    // Scene Creation
    Scene scene = new Scene(new ScrollPane(ta), 450, 200);
    primaryStage.setTitle("Prime Number Server");
    primaryStage.setScene(scene);
    primaryStage.show();

    new Thread(() -> {
      try {
        // Create a server socket
        ServerSocket serverSocket = new ServerSocket(8000);
        Platform.runLater(() ->
                ta.appendText("Server started at " + new Date() + '\n'));

        // Listen for a connection request
        Socket socket = serverSocket.accept();

        // Create data input and output streams
        DataInputStream inputFromClient = new DataInputStream(
                socket.getInputStream());
        DataOutputStream outputToClient = new DataOutputStream(
                socket.getOutputStream());

        while (true) {
          // Receive number from the client
          int number = inputFromClient.readInt();

          // Check if the number is prime
          boolean isPrime = isPrime(number);

          // Send result back to the client
          outputToClient.writeBoolean(isPrime);

          Platform.runLater(() -> {
            ta.appendText("Number received from client: "
                    + number + '\n');
            ta.appendText("Is prime: " + isPrime + '\n');
          });
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }).start();
  }

  /**
   * Check if a number is prime
   */
  private boolean isPrime(int number) {
    if (number <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(number); i++) {
      if (number % i == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * The main method is only needed for the IDE with limited JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
