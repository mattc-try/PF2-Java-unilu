package rpg.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rpg.RPGController;

/** Represents the view component of the RPG menu. */
public class RPGMenuView {
  private Stage stage;
  private RPGController controller;

  /**
   * Constructs an RPGMenuView object with the specified stage and controller.
   *
   * @param stage the Stage representing the menu window
   * @param controller the RPGController associated with the menu view
   */
  public RPGMenuView(Stage stage, RPGController controller) {
    this.stage = stage;
    this.controller = controller;
  }

  /**
   * Displays the RPG menu.
   *
   * @param gamePlayed indicates if the game has been played
   * @param gameWon indicates if the game has been won
   */
  public void display(boolean gamePlayed, boolean gameWon) {
    // Create the menu layout
    VBox menuLayout = createMenuLayout(gamePlayed, gameWon);

    // Create start and exit buttons
    Button startButton = createStartButton();
    Button exitButton = createExitButton();

    setupButtonActions(startButton, exitButton);
    setButtonDimensions(startButton, exitButton);

    // Apply CSS class names to elements
    menuLayout.getStyleClass().add("menu-layout");
    startButton.getStyleClass().addAll("start-button", gamePlayed ? "game-played" : "");
    exitButton.getStyleClass().addAll("exit-button", gamePlayed ? "game-played" : "");

    // Add buttons to the menu layout
    menuLayout.getChildren().addAll(startButton, exitButton);

    // Create and configure the scene
    Scene scene = new Scene(menuLayout, 800, 600);
    scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

    // Set the scene and stage properties
    stage.setScene(scene);
    stage.setTitle("Try not to poop! v6.9");
    stage.show();
  }

  /**
   * Creates the layout for the RPG menu.
   *
   * @param gamePlayed indicates if the game has been played
   * @param gameWon indicates if the game has been won
   * @return the VBox layout for the menu
   */
  private VBox createMenuLayout(boolean gamePlayed, boolean gameWon) {
    VBox menuLayout = new VBox(10);
    menuLayout.setAlignment(Pos.CENTER);

    // image
    ImageView image = createOutcomeImageView(gamePlayed, gameWon);
    menuLayout.getChildren().add(image);

    // Background image
    menuLayout.setBackground(
        new Background(
            new javafx.scene.layout.BackgroundImage(
                new javafx.scene.image.Image("menu/background.png"),
                null,
                null,
                null,
                new BackgroundSize(
                    BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true))));

    // Display different text based on game outcome and image
    String outcomeText = gamePlayed ? (gameWon ? "Congrats, you won!" : "Sorry, you lost!") : "";
    Label outcomeLabel = new Label(outcomeText);
    outcomeLabel.getStyleClass().add("outcome-label");
    menuLayout.getChildren().add(outcomeLabel);

    return menuLayout;
  }

  /**
   * Creates the start button for the RPG menu.
   *
   * @return the start button
   */
  private Button createStartButton() {
    Button startButton = new Button("Start Pooping");
    startButton.setOnMouseEntered(event -> startButton.setScaleX(1.1));
    startButton.setOnMouseExited(event -> startButton.setScaleX(1.0));
    return startButton;
  }

  /**
   * Creates the exit button for the RPG menu.
   *
   * @return the exit button
   */
  private Button createExitButton() {
    Button exitButton = new Button("Exit Pooping");
    exitButton.setOnMouseEntered(event -> exitButton.setScaleX(1.1));
    exitButton.setOnMouseExited(event -> exitButton.setScaleX(1.0));
    return exitButton;
  }

  /**
   * Creates the image view for the outcome image based on gamePlayed and gameWon parameters.
   *
   * @param gamePlayed indicates if the game has been played
   * @param gameWon indicates if the game has been won
   * @return the image view for the outcome image
   */
  private ImageView createOutcomeImageView(boolean gamePlayed, boolean gameWon) {
    if (!gamePlayed) {
      // Create image view with logo image if the game hasn't been played
      Image logoImage = new Image("menu/logo.png");
      return new ImageView(logoImage);
    }
    // Select the outcome image based on gameWon
    Image outcomeImage = gameWon ? new Image("menu/win.jpg") : new Image("menu/loss.jpg");

    // Create the image view for the outcome image
    ImageView outcomeImageView = new ImageView(outcomeImage);
    outcomeImageView.setVisible(gamePlayed);
    return outcomeImageView;
  }

  /**
   * Sets up the action handlers for the start and exit buttons.
   *
   * @param startButton the start button
   * @param exitButton the exit button
   */
  private void setupButtonActions(Button startButton, Button exitButton) {
    startButton.setOnAction(event -> controller.initialize(stage));
    exitButton.setOnAction(event -> stage.close());
  }

  /**
   * Sets the dimensions for the start and exit buttons.
   *
   * @param startButton the start button
   * @param exitButton the exit button
   */
  private void setButtonDimensions(Button startButton, Button exitButton) {
    double buttonWidth = 200;
    double buttonHeight = 40;
    startButton.setPrefWidth(buttonWidth);
    startButton.setPrefHeight(buttonHeight);
    exitButton.setPrefWidth(buttonWidth);
    exitButton.setPrefHeight(buttonHeight);
  }
}
