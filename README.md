This code includes several Java classes that serve different purposes. The overall purpose of the program is to create a program that retrieves the current 12 month inflation rate and allows the user to create a grocery list that will be multiplied by that inflation rate. Below is a brief description of each class and its purpose.

createAccount:
This class provides functionality for creating a new user account. The class contains a method for adding a new user to a database and a method for displaying a JavaFX scene. The addUser method first checks if the password and confirm password match, and then checks if the username already exists in the database. If the username is not in the database, it hashes the password and inserts the username and hashed password into the database. The displayCreateAccountScene method loads an FXML file, sets the controller for the scene, and creates a new window to display the scene. It adds an event handler to the "Create" button that calls the addUser method, and if the user is successfully added to the database, displays an information message and closes the create account window.
After the user enters their desired username and password and clicks the "Create" button, the addUser method is called to add the user to the database. If the user is successfully added, an information message is displayed and the create account window is closed. Otherwise, an error message is displayed and the user is prompted to try again.

createAccountController:
This class is the controller for the account creation screen implemented in the createAccount class. It defines the behavior of the UI elements in the createAccount  It has a few getter methods to access the UI elements for event handling.

Login:
This class is an implementation of a simple login screen using JavaFX. It connects to a MySQL database to validate user credentials and to add new users. It has an overridden method called start() which is responsible for setting up the JavaFX user interface, including the login screen layout, title, and size. It also has a few private helper methods to validate user credentials, add new users to the database, and hash passwords using SHA-256 algorithm.

The start() method sets up the login screen using a JavaFX scene graph that is created from an FXML file called "login.fxml". It loads the FXML file, sets up a LoginController instance to handle the UI interactions, and creates the parent container for the UI elements. It then sets the title, scene, and stage dimensions for the login screen, and displays it on the user's screen.

LoginController:
This class is the controller for the login screen implemented in the Login class. It defines the behavior of the UI elements in the login screen, including the username and password text fields, the login and create account buttons, and the alert dialogs that display messages to the user. It has a few getter methods to access the UI elements for event handling in the Login class.

InflationAPI:
This class retrieves the inflation rate for the previous month from an external API. It calculates the inflation rate based on the consumer price index (CPI) in the United States. The API call is made using the URL of the API endpoint, which includes the start and end dates of the period for which the inflation rate is being calculated. It returns the inflation rate as a double.

MatchSearchController:
This class is the controller for the match search screen implemented in the MatchSearch class. It defines the behavior of the UI elements in the match search screen, including the search text field, the search and add buttons, the results and selected products list views, and the total cost label. It has a few getter methods to access the UI elements for event handling in the MatchSearch class.

MatchSearch:
This class implements a simple match search screen using JavaFX. It connects to a MySQL database to search for products based on user input. It has an overridden method called start() which sets up the JavaFX user interface, including the match search screen layout, title, and size.

The start() method creates a MatchSearchController instance to handle the UI interactions, and sets up the parent container for the UI elements. It then sets the title, scene, and stage dimensions for the match search screen, and displays it on the user's screen. It also establishes a connection to the MySQL database using JDBC, and retrieves the inflation rate from the InflationAPI class.

The MatchSearch class implements event handling for the search, add, and remove buttons in the MatchSearchController class. When the user enters a search term and clicks the search button, the MatchSearch class searches the MySQL database for products that match the search term, and displays them in a list view. When the user selects a product from the list view and clicks the add button, the MatchSearch class adds the selected product to a selected products list view, and updates the total cost label to include the selected product's price multiplied by the inflation rate. When the user selects a product from the selected products list view and clicks the remove button, the MatchSearch class removes the selected product from the selected products list view, and updates the total cost label to exclude the removed product's price multiplied by the inflation rate.

passwordUtils:
This class provides a utility method for hashing passwords using the SHA-256 algorithm with a fixed salt. The class contains a single static method:

hashPassword(String password, String fixedSalt): This method takes two parameters, password and fixedSalt, and returns a hashed representation of the password as a Base64-encoded string. The password parameter is the plain text password to be hashed, and the fixedSalt parameter is a string that is used as a fixed salt for the hashing process. The method first converts the fixed salt to a byte array and then uses it to update a MessageDigest instance initialized with the SHA-256 algorithm. The password is then converted to a byte array and used to update the MessageDigest. Finally, the MessageDigest is used to generate a salted hash of the password, which is then encoded as a Base64 string and returned.

If an error occurs while hashing the password, the method returns an empty string and prints an error message to the console.

EmailSceneController:
The provided code is a Java class named "EmailSceneController" that serves as the controller for an email scene in a JavaFX application. It allows the user to send a grocery report via email using the Apache Commons Email library.
The code utilizes the Apache Commons Email library to send the email. It sets up an HtmlEmail object, configures the SMTP server details, adds the recipient, sender, subject, and email body, and finally sends the email.

If the email is sent successfully, an information dialog is displayed. Otherwise, an error dialog is shown with the corresponding error message.
