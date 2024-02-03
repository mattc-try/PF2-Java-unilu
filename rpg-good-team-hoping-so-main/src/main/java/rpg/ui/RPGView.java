package rpg.ui;

import java.net.URL;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import rpg.RPGController;

/** The view class for the RPG game. */
public class RPGView {
  private TilePane tiles;
  private StackPane[][] sprites;
  private RPGController controller;
  private ImageView playerImage;
  private Label timerLabel;
  private ProgressBar cooldownBar;
  private MediaPlayer mediaPlayer;
  private VBox gameContainer;
  private HBox controlsContainer;

  /**
   * Constructs a new RPGView object.
   *
   * @param tiles the TilePane to display the game tiles
   * @param sprites a 2D array of StackPane objects representing the sprites on the tiles
   * @param controller the RPGController object for game logic
   */
  public RPGView(TilePane tiles, StackPane[][] sprites, RPGController controller) {
    this.tiles = tiles;
    this.sprites = sprites;
    this.controller = controller;
    this.timerLabel = new Label();
    this.cooldownBar = new ProgressBar();
    this.gameContainer = new VBox();
    this.controlsContainer = new HBox();

    // Load the game music file
    URL musicUrl = getClass().getResource("/sounds/song-poopy.mp3");
    Media media = new Media(musicUrl.toString());

    // Create a MediaPlayer object
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.setOnEndOfMedia(
        () -> {
          mediaPlayer.seek(Duration.ZERO);
          mediaPlayer.play();
        });

    playerImage = new ImageView(new Image("sprites/poopy.png"));
    playerImage.setViewport(new Rectangle2D(0, 0, 50, 50));
  }

  /**
   * Sets up the game scene and displays it on the provided stage.
   *
   * @param stage the Stage to display the scene on
   */
  public void setupGameScene(Stage stage) {
    VBox root = new VBox();
    root.getChildren().addAll(tiles, getGameContainer());

    Scene scene = new Scene(root);
    scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

    // Event handler for key releases
    scene.setOnKeyReleased(
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            sprites[controller.getModel().getX()][controller.getModel().getY()]
                .getChildren()
                .remove(1);

            // Perform player action based on the released key
            controller.getModel().actPlayer(event.getCode());
            // Update player position on the game board
            updatePlayerPosition(controller.getModel().getX(), controller.getModel().getY());
          }
        });

    stage.setScene(scene);
    stage.show();
  }

  /**
   * Initializes the RPGView instance by setting up the controls container, adding the menu button,
   * playing the game music, and initializing the sprites.
   */
  public void initialize() {
    setupControlsContainer();
    addMenuButton();
    mediaPlayer.play();
    setupGameContainer();
    initializeSprites();
  }

  /** Sets up the controls container by adding the timer label and cooldown bar. */
  private void setupControlsContainer() {
    controlsContainer.getChildren().add(timerLabel);

    // Set preferred width and style class for cooldown bar
    cooldownBar.setPrefWidth(200);
    cooldownBar.getStyleClass().add("cooldown-bar");
    cooldownBar.setProgress(1.0);
    controlsContainer.getChildren().add(cooldownBar);

    // Set controls container and margins for timer label and cooldown bar
    controlsContainer.setAlignment(Pos.TOP_RIGHT);
    controlsContainer.setMargin(timerLabel, new Insets(10));
    controlsContainer.setMargin(cooldownBar, new Insets(10));
  }

  /**
   * Initializes the sprites by loading and adding the necessary images to the sprite stack panes.
   */
  private void initializeSprites() {
    // Load sprite images
    Image grassImage = new Image("sprites/grass-tile.png");
    Image drugImage = new Image("sprites/drug.png");
    Image obstacleImageRock = new Image("sprites/obstacle-rock.png");
    Image obstacleImagePanel = new Image("sprites/obstacle-panel.png");
    Image houseImage = new Image("sprites/house.gif");

    for (int i = 0; i < sprites.length; ++i) {
      for (int j = 0; j < sprites[i].length; ++j) {
        sprites[i][j] = new StackPane();
        sprites[i][j].setStyle("-fx-background-color: #008000;");
        sprites[i][j].getChildren().add(makeView(grassImage));

        if (i == 0 && j == 5) {
          // Add house image to the specific location
          sprites[i][j].getChildren().clear();
          sprites[i][j].getChildren().add(makeView(houseImage));
        } else {
          if (Math.random() < 0.1) {
            // Add obstacles or drugs randomly
            sprites[i][j].getChildren().add(makeView(obstacleImageRock));
          } else if (Math.random() < 0.1) {
            sprites[i][j].getChildren().add(makeView(obstacleImagePanel));
          } else if (Math.random() < 0.05) {
            sprites[i][j].getChildren().add(makeView(drugImage));
          }
        }

        // Add sprite to the tiles container
        tiles.getChildren().add(sprites[i][j]);
        tiles.setVgap(0);
        tiles.setHgap(0);
        tiles.setPadding(new Insets(0, 0, 0, 0));
      }
    }
  }

  /**
   * Sets up the game container by creating an intermediate container with the controls container
   * and tiles, and adding it to the game container.
   */
  private void setupGameContainer() {
    VBox intermediateContainer = new VBox(controlsContainer, tiles);
    intermediateContainer.setAlignment(Pos.TOP_CENTER);
    intermediateContainer.setSpacing(10);

    gameContainer.getChildren().addAll(intermediateContainer);
  }

  /**
   * Returns the game container.
   *
   * @return the VBox representing the game container
   */
  public VBox getGameContainer() {
    return gameContainer;
  }

  /** Adds the menu button to the controls container and sets its event handler. */
  public void addMenuButton() {
    Button menuButton = new Button("Back to Menu");
    menuButton.getStyleClass().add("button-backtomenu");
    menuButton.setOnAction(
        event -> {
          controller.goToMenu(false, false);
        });

    // Add the menu button to the controls container
    controlsContainer.getChildren().add(menuButton);
  }

  /**
   * Updates the player position in the view based on the provided coordinates.
   *
   * @param x the x-coordinate of the player
   * @param y the y-coordinate of the player
   */
  public void updatePlayerPosition(int x, int y) {
    // Check if the player is on a drug tile
    isDrug(x, y);

    // Clear the current sprite and set the grass tile as the background
    sprites[x][y].getChildren().clear();
    Image grassImage = new Image("sprites/grass-tile.png");
    sprites[x][y].getChildren().add(makeView(grassImage));

    // Add the player's image to the sprite at the updated position
    sprites[x][y].getChildren().add(playerImage);
    System.out.println("playerImage set to " + x + "," + y);
  }

  /**
   * Updates the player state in the view based on the provided state.
   *
   * @param state the state of the player
   */
  public void updatePlayerState(int state) {
    if (state > 2 || state < 0) {
      return;
    }
    playerImage.setViewport(new Rectangle2D(0, state * 50, 50, 50));
  }

  /**
   * Creates and returns an ImageView object with the provided image.
   *
   * @param image the image for the ImageView
   * @return an ImageView object with the provided image
   */
  private ImageView makeView(Image image) {
    ImageView view = new ImageView();
    view.setImage(image);
    return view;
  }

  /**
   * Sets the text of the timer label and applies the specified style based on the panic mode.
   *
   * @param text the text to set on the timer label
   * @param panicMode a flag indicating whether panic mode is active or not
   */
  public void setTimerText(String text, boolean panicMode) {
    timerLabel.setText(text);
    if (panicMode) {
      timerLabel.getStyleClass().add("panic-mode");
    } else {
      timerLabel.getStyleClass().add("timer-text");
    }
  }

  /**
   * Updates the cooldown progress of the cooldown bar based on the provided value.
   *
   * @param cooldownProgress the progress value of the cooldown bar
   */
  public void cooldownProgress(double cooldownProgress) {
    // Check if the fart is on cooldown
    if (controller.getModel().isFartOnCooldown()) {
      // Update the cooldown bar progress
      cooldownBar.setProgress(cooldownProgress);
    } else {
      // Reset the cooldown bar to full progress
      cooldownBar.setProgress(1.0);
    }
  }

  /** Stops the game music. */
  public void stopMusic() {
    mediaPlayer.stop();
  }

  /**
   * Checks if the provided stack pane contains an image with the specified image name.
   *
   * @param pane the stack pane to check
   * @param imageName the name of the image
   * @return true if the stack pane contains the image, false otherwise
   */
  private boolean containsImage(StackPane pane, String imageName) {
    for (Node node : pane.getChildren()) {
      if (node instanceof ImageView) {
        // Check if the image URL contains the specified image name
        Image image = ((ImageView) node).getImage();
        if (image.getUrl().contains(imageName)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if the specified coordinates contain a drug image and calls the controller's takeDrugs()
   * method if true.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   */
  public void isDrug(int x, int y) {
    if (containsImage(sprites[x][y], "drug.png")) {
      playSound("drugs-sound.mp3");
      controller.takeDrugs();
    }
  }

  /**
   * Checks if there is an obstacle at the specified coordinates.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return true if there is an obstacle, false otherwise
   */
  public boolean hasObstacleAt(int x, int y) {
    if (containsImage(sprites[x][y], "obstacle-rock.png")
        || containsImage(sprites[x][y], "obstacle-panel.png")) {
      playSound("colision-sound.mp3");
      return true;
    } else {
      return false;
    }
  }

  /**
   * Plays a sound effect with the specified file name.
   *
   * @param fileName the name of the sound file
   */
  public void playSound(String fileName) {
    Media sound = new Media(getClass().getResource("/sounds/" + fileName).toExternalForm());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.play();
  }

  /**
   * Checks if the specified coordinates are inside a house.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return true if the coordinates are inside a house, false otherwise
   */
  public boolean inHouse(int x, int y) {
    return containsImage(sprites[x][y], "house.gif");
  }
}
