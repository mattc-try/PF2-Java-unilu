package rpg;

import javafx.application.Application;
import javafx.stage.Stage;
import rpg.ui.RPGMenuView;
import rpg.ui.RPGView;

/** The main class for starting the RPG game. */
public class RPG extends Application {

  /**
   * The main entry point for the application.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * The start method called by the JavaFX runtime.
   *
   * @param primaryStage the primary stage for the application
   */
  @Override
  public void start(Stage primaryStage) {
    // Create null instances of the Model and Game View for the controller
    RPGModel model = new RPGModel();
    RPGView view = new RPGView(null, null, null);

    // Create an instance of RPGController with the model and view
    RPGController controller = new RPGController(model, view);
    // Set the controller to the model so it is not null
    model.setController(controller);

    // Create an instance of RPGMenuView with the primaryStage and controller
    RPGMenuView menuView = new RPGMenuView(primaryStage, controller);
    // Display the menuView with initial values for gameWon and menuDisplayed set to false
    menuView.display(false, false);
  }
}
