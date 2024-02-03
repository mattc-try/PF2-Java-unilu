package rpg;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** Represents the model component of an RPG game. */
public class RPGModel {
  private static final int SIZE = 10;
  private int x = 8;
  private int y = 0;
  private static final int TIMER_DURATION = 17; // 15s takes around 2 to appear
  private int timerSeconds;
  private Timeline timer;
  private Runnable timerUpdateCallback;
  private RPGController controller;
  private boolean fartOnCooldown; // New field to track the cooldown
  private Timeline cooldownTimer; // New field to track the cooldown timer
  private double elapsedTime;

  public RPGModel() {
    // Initialize the cooldown as false
    this.fartOnCooldown = false;
  }

  public void setController(RPGController controller) {
    this.controller = controller;
  }

  /**
   * Gets the size of the game grid.
   *
   * @return the size of the game grid
   */
  public int getSize() {
    return SIZE;
  }

  /**
   * Gets the current x-coordinate of the player's position.
   *
   * @return the x-coordinate of the player's position
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the current y-coordinate of the player's position.
   *
   * @return the y-coordinate of the player's position
   */
  public int getY() {
    return y;
  }

  /**
   * Acts on the player based on the specified KeyCode. Moving or Farting !
   *
   * @param keyCode the KeyCode representing the direction in which to move the player or the fart !
   */
  public void actPlayer(KeyCode keyCode) {
    switch (keyCode) {
      case UP:
        // Move the player up if within the grid bounds and no obstacle
        if (x > 0 && !controller.hasObstacleAt(x - 1, y)) {
          controller.gameWon(x - 1, y);
          --x;
        } else {
          timerSeconds(-2);
        }
        break;
      case DOWN:
        // Move the player down if within the grid bounds and no obstacle
        if (x < SIZE - 1 && !controller.hasObstacleAt(x + 1, y)) {
          controller.gameWon(x + 1, y);
          ++x;
        } else {
          timerSeconds(-2);
        }
        break;
      case LEFT:
        // Move the player to the left if within the grid bounds and no obstacle
        if (y > 0 && !controller.hasObstacleAt(x, y - 1)) {
          controller.gameWon(x, y - 1);
          --y;
        } else {
          timerSeconds(-2);
        }
        break;
      case RIGHT:
        // Move the player to the right if within the grid bounds and no obstacle
        if (y < SIZE - 1 && !controller.hasObstacleAt(x, y + 1)) {
          controller.gameWon(x, y + 1);
          ++y;
        } else {
          timerSeconds(-2);
        }
        break;
      case F:
        // Perform the fart action if not on cooldown
        if (!fartOnCooldown) {
          fart();
        } else {
          System.out.println("Fart on cooldown! Wait for 5 seconds.");
        }
        break;
      default:
        break;
    }
  }

  /** Performs the fart action. */
  private void fart() {
    // Play the fart sound
    playFartSound();

    // Start a cooldown
    fartOnCooldown = true;
    System.out.println("Fart! Cooldown activated for 5 seconds.");
    startCooldownTimer(5.0);

    if (timerSeconds > 0) {
      if (Math.random() < 0.8) {
        // Add time to the timer (4/5 probability)
        timerSeconds(5);
        System.out.println("Timer increased by 10 seconds.");
      } else {
        // End the game (1/5 probability)
        timerSeconds = 0;
        System.out.println("Uh-oh! You couldn't hold it in. Game over!");
        // Check if the controller is not null before invoking the goToMenu() method
        if (controller != null && !controller.menuDisplayed) {
          controller.goToMenu(true, false);
        }
      }

      if (timerUpdateCallback != null) {
        timerUpdateCallback.run();
      }
    } else {
      System.out.println("Time's up! You can't fart anymore.");
    }
  }

  /**
   * Starts the cooldown timer for the fart action.
   *
   * @param cooldownDuration the duration of the cooldown in seconds
   */
  private void startCooldownTimer(double cooldownDuration) {
    double updateInterval = 0.5;
    // Reset elapsedTime to 0
    elapsedTime = 0.0;

    // Create and play the cooldown progress timer
    Timeline cooldownProgressTimer =
        new Timeline(
            new KeyFrame(
                Duration.seconds(updateInterval),
                event -> {
                  elapsedTime += updateInterval;
                  double cooldownProgress = getCooldownProgress();
                  if (controller != null) {
                    // Update the progress bar
                    controller.fartCooldown(cooldownProgress);
                  }
                }));
    cooldownProgressTimer.setCycleCount((int) (cooldownDuration / updateInterval));
    cooldownProgressTimer.play();

    // Create and play the cooldown timer
    cooldownTimer =
        new Timeline(
            new KeyFrame(
                Duration.seconds(cooldownDuration),
                event -> {
                  // Reset the cooldown after 5 seconds
                  fartOnCooldown = false;
                  System.out.println("Fart cooldown expired.");
                }));
    cooldownTimer.play();
  }

  /** Plays the fart sound. */
  private void playFartSound() {
    Media fartSound = new Media(getClass().getResource("/sounds/fart-sound.wav").toExternalForm());
    MediaPlayer mediaPlayer = new MediaPlayer(fartSound);
    mediaPlayer.play();
  }

  /**
   * Starts the timer for the game.
   *
   * @param callback the callback function to be executed every second
   */
  public void startTimer(Runnable callback) {
    timerSeconds = TIMER_DURATION;

    timer =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  timerSeconds--;
                  callback.run();
                  if (timerUpdateCallback != null) {
                    timerUpdateCallback.run();
                  }
                }));
    timer.setCycleCount(TIMER_DURATION + 1); // Add +1 to include the initial value
    timer.play();
  }

  /** Stops the timer for the game. */
  public void stopTimer() {
    timer.stop();
  }

  /**
   * Gets the remaining time on the timer.
   *
   * @return the remaining time on the timer
   */
  public int timerSeconds() {
    return timerSeconds;
  }

  /**
   * Gets the modified remaining time on the timer by adding or subtracting the specified amount.
   *
   * @param secondsToAddOrSubtract the number of seconds to add (if positive) or subtract (if
   *     negative)
   */
  public int timerSeconds(int secondsToAddOrSubtract) {
    timerSeconds += secondsToAddOrSubtract;
    return timerSeconds;
  }

  /**
   * Sets the callback function to be executed on each timer update.
   *
   * @param callback the callback function to be executed on each timer update
   */
  public void setTimerUpdateCallback(Runnable callback) {
    timerUpdateCallback = callback;
  }

  /**
   * Gets the progress of the fart cooldown.
   *
   * @return the progress of the fart cooldown, a value between 0.0 and 1.0
   */
  public double getCooldownProgress() {
    return fartOnCooldown ? (elapsedTime / 5.0) : 0.0;
  }

  /**
   * Checks if the fart action is on cooldown.
   *
   * @return true if the fart action is on cooldown, false otherwise
   */
  public boolean isFartOnCooldown() {
    return fartOnCooldown;
  }

  /**
   * Gets the state of the player.
   *
   * @return the state of the player
   */
  private int playerState = 0;

  public int getPlayerState() {
    return playerState;
  }

  /**
   * Sets the state of the player.
   *
   * @param playerState the state of the player
   */
  public void setPlayerState(int playerState) {
    this.playerState = playerState;
  }

  /** Resets the position of the player to the initial position. */
  public void resetPosition() {
    this.x = 8;
    this.y = 0;
  }
}
