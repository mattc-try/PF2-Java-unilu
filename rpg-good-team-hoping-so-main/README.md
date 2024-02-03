[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/D7hFx6hN)
# RPG Laboratory

To compile, run `mvn compile` and to execute, run `mvn javafx:run -q`.

Try not too poop simulator
Main features to make:
The game as a menu, the game play is a character poopy which is in the street that starts wanting too poop really badly, so he is running to his house too poop on a timer. On the way there are rocks and pannels trying to slow him down. The poop representing the timer gets bigger on the screen as time passes. The character can relieve the stress by farting when pressing F but this could make him release or doing drugs.

- Building the MVC architecture /
- Check respect to MVC /
- Manage packages /
- Make a very simple game menu /
- Update the menu styling /
- Add stupid game music /

- Creating the character /
- Make a pooping timer /
- Move the pooping timer to the Model /
- Display the button better, and fix tiles bug /
- Display the pooping timer better, with a poop that gets dif /
- Add levels by making them selectable in the menu, they just set the timer to dif beginning times (no time ig)

- Making objects that disappear when the character appears on it /
- Make obstacles, rock and pannel making the character waste time -3s on general timer /
- Make a bonus, drug object that can be taken by the character pressing 1 key if he hit it +3s /
- Sound effects for taking drugs and hitting an obstacle

- Make a fart feature where he can either be relieved or want to poop even more, pressing F /
- Add fart sound effect /
- Add a cool-down timer on the fart /
- Display the cool-down timer with a poop colored bar /
- Link the progress bar to model cool down timer /
- Increase probability of pooping when the timer is under 10 seconds /

- Win condition and house /
- Bug Fixing appearing 1/2 times /
- Add a game ending when there is no time left, or when poopy wins /
- Menu layout bug on newer javafx version /

- Document

/ means task is completed


# Class reponsibilities

// RPG Package (main package) //
The "rpg" package consists of the "RPG," "RPGController," and "RPGModel" classes, which drive the RPG game.

- RPG:

  1. The main class for starting the RPG game.
  2. Launch the JavaFX application.
  3. Create and initialize the model, view, and controller.
  4. Display the RPG menu.

- RPGController:

  1. Represents the controller component of the game.
  2. Initialize the game and handle game flow.
  3. Set up the game timer.
  4. Create and configure the game view.
  5. Handle player movement and position updates.
  6. Handle menu navigation and stage initialization.

- RPGModel:

  1. Represents the model component of the game.
  2. Manages the game logic and state of the RPG game.
  3. Tracks the player's position on the game grid.
  4. Handles player movement based on input keys.
  5. Manages cooldown for fart().
  6. Plays sound effects, such as a fart sound.
  7. Provides methods to start, stop, and update the game timer, and also allows to modify it.
  8. Keeps track of the player's state.


// UI Package (sub of rpg) //
The "ui" package was separated from the main "rpg" package to maintain a clear separation of concerns and adhere to the principles of modularity and encapsulation. By organizing the user interface components in a separate package, it becomes easier to manage and modify the visual aspects of the RPG game independently from the core game logic. This separation allows for better code organization, maintainability, and facilitates future enhancements or modifications to the user interface without impacting the underlying game functionality.

- RPGView:

  1. Represents the view component of the game.
  2. Initialize the game view, including controls container, menu button, game container, and sprites.
  3. Update player position and state.
  4. Handle key events for player movement.
  5. Update timer text and cooldown progress.
  6. Stop the game music.
  7. Check for drugs, obstacles, and house in the game grid.

- RPGMenuView:

  1. Display the RPG menu.
  2. Create and configure menu layout, start button, and exit button.
  3. Apply CSS styles to elements.
  4. Sets up button actions and dimensions.
  5. Set the scene on the stage and show the menu window.

# Contributions
STEINBACH Matteo:
- MVC
- Packages
- Simple game Menu
- Music and sounds
- Poooping timer
- Go to Menu button
- Pooping Timer display
- Fart feature
- Styles sheet
- Cooldown timer and ProgressBar display
- Bonus/Malusses on obstacle
- Cleaning code, following principles
- Documentation and Readme.md
- Presentation everything

LE DREZEN Aymeric:
- Image resources
- Poopy (main charachter)
- Changes overtime
- Obstacles
- Colisions
- Win condition
- Changing Menu on win conditions win or lose
- Bug fixing

JACK Oliver:
- Menu looks
- UI updates
