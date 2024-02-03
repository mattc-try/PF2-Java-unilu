package rpg;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import rpg.ui.RPGMenuView;
import rpg.ui.RPGView;

/** Represents the controller component of an RPG game. */
public class RPGController {
  private RPGModel model;
  private RPGView view;
  private TilePane tiles;
  public boolean menuDisplayed = true;

  /**
   * Constructs an RPGController object with the specified model and view.
   *
   * @param model the RPGModel representing the game's model
   * @param view the RPGView representing the game's view
   */
  public RPGController(RPGModel model, RPGView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Initializes the try not to poop game by setting up the stage, tiles, sprites, and player
   * position.
   *
   * @param stage the Stage representing the game window
   */
  public void initialize(Stage stage) {
    menuDisplayed = false;

    // Setup the timer in the model
    setupTimer();

    // Create the TilePane for displaying the game grid
    tiles = new TilePane();
    tiles.setPrefColumns(model.getSize());
    tiles.setPrefRows(model.getSize());
    tiles.setTileAlignment(Pos.CENTER);

    // Create the grid of StackPane objects representing the sprites
    StackPane[][] sprites = new StackPane[model.getSize()][model.getSize()];
    view = new RPGView(tiles, sprites, this);

    // Initialize the view
    view.initialize();

    // Add the initial player position on the view with the player image
    model.resetPosition();
    view.updatePlayerPosition(model.getX(), model.getY());

    // Set up the game scene and display it on the stage
    view.setupGameScene(stage);
  }

  private void setupTimer() {
    model.startTimer(this::updateTimer);
    int initialTimerSeconds = model.timerSeconds();
    view.setTimerText(formatTimerText(initialTimerSeconds), false);
  }

  /** Redirects the user to the main menu. */
  public void goToMenu(boolean gamePlayed, boolean gameWon) {
    // Stop the music
    view.stopMusic();
    // Stop the timer game logic
    model.stopTimer();
    // Get the current stage from the tiles and its scene, then cast it to a Stage object
    Stage currentStage = (Stage) tiles.getScene().getWindow();
    // Close the game window
    currentStage.close();

    // Create a new stage for the main menu
    Stage primaryStage = new Stage();
    // Create an instance of RPGMenuView, passing the new stage and the current RPGController
    RPGMenuView menuView = new RPGMenuView(primaryStage, this);
    // Display the main menu
    menuView.display(gamePlayed, gameWon);
  }

  /**
   * Formats the timer value into a string representation.
   *
   * @param seconds the remaining seconds of the timer
   * @return the formatted timer text
   */
  private String formatTimerText(int seconds) {
    int minutes = seconds / 60;
    int remainingSeconds = seconds % 60;
    return String.format("%02d:%02d", minutes, remainingSeconds);
  }

  /** Updates the timer and performs game logic based on the remaining timer seconds. */
  private void updateTimer() {
    int timerSeconds = model.timerSeconds();
    timerSeconds--;

    if (timerSeconds <= 0) {
      if (!menuDisplayed) {
        // Redirect the user to the menu if the game is not already displaying the menu
        goToMenu(true, false);
      }
      model.stopTimer();
    } else {
      // Update the timer label in the view
      view.setTimerText(formatTimerText(timerSeconds), (timerSeconds <= 5));

      // Perform game logic based on the remaining timer seconds
      if (timerSeconds >= 15 && model.getPlayerState() > 0) {
        // Reset the player state to 0 if the timer is above or equal to 30 seconds
        view.updatePlayerState(0);
        model.setPlayerState(0);
      } else if ((timerSeconds <= 30 && model.getPlayerState() == 0)
          || (timerSeconds <= 10 && timerSeconds > 5) && model.getPlayerState() == 2) {
        // Update the player state to 1 if the timer is below or equal to 30 seconds and above 15
        // seconds,
        // or if the player state is currently 0
        view.updatePlayerState(1);
        model.setPlayerState(1);
      } else if (timerSeconds <= 5 && model.getPlayerState() == 1) {
        // Update the player state to 2 if the timer is below or equal to 5 seconds and the player
        // state is 1
        view.updatePlayerState(2);
        model.setPlayerState(2);
      }
    }
  }

  /**
   * Retrieves the RPGModel associated with the controller.
   *
   * @return the RPGModel object
   */
  public RPGModel getModel() {
    return model;
  }

  /**
   * Notifies the view to update the cooldown progress.
   *
   * @param cooldownProgress the progress of the cooldown
   */
  public void fartCooldown(double cooldownProgress) {
    view.cooldownProgress(cooldownProgress);
  }

  /**
   * Checks if there is an obstacle at the specified position.
   *
   * @param x the x-coordinate of the position
   * @param y the y-coordinate of the position
   * @return true if there is an obstacle, false otherwise
   */
  public boolean hasObstacleAt(int x, int y) {
    return view.hasObstacleAt(x, y);
  }

  /** Sets the timer to a specific duration of 10 seconds when the player takes drugs. */
  public void takeDrugs() {
    model.timerSeconds(3);
  }

  /**
   * Checks if the game is won based on the player's position.
   *
   * @param x the x-coordinate of the player's position
   * @param y the y-coordinate of the player's position
   * @return true if the game is won, false otherwise
   */
  public boolean gameWon(int x, int y) {
    boolean isGameWon = view.inHouse(x, y);
    if (isGameWon) {
      goToMenu(true, true);
      return true;
    } else {
      return false;
    }
  }
}
