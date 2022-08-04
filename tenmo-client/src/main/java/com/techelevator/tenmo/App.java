package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

/**
 *
 * This is the control flow portion of the client. This is the class that will delegate tasks to the service classes
 *
 */
public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final int ACCEPTED_STATUS = 2;
    private final int PENDING_STATUS = 1;
    private final int REJECTED_STATUS = 3;
    private ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    /**
     *
     * Control flow portion
     *
     */
    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    /**
     *
     * Loops through the longin menu allowing the user to register or login
     *
     */
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    /**
     *
     * Directs the client to the service to allow a user to register
     *
     */
    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    /**
     *
     * Directs the client to the service to allow a user to login
     *
     */
    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        consoleService.setAuthenticatedUser(currentUser);
    }

    /**
     *
     *
     * Loops through the main menu, and directs the client to the console service for each choice
     *
     */
    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                //viewCurrentBalance();
                consoleService.handleAccountBalance();
            } else if (menuSelection == 2) {
                //viewTransferHistory();
                consoleService.handlePastTransfers(ACCEPTED_STATUS);
            } else if (menuSelection == 3) {
                //viewPendingRequests();
                consoleService.handlePastTransfers(PENDING_STATUS);
            } else if (menuSelection == 4) {
                //sendBucks();
                consoleService.handleSendRequest();
            } else if (menuSelection == 5) {
                //requestBucks();
                consoleService.handleReceiveRequest();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


}
