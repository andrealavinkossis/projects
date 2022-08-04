package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 *
 *    This is the user interface portion of the client. This class will handle whenever the client needs to write or
 *    read from the machine. This class is not in charge of the control flow of the client
 *
 */

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final RestService restService = new RestService();
    private AuthenticatedUser authenticatedUser = null;

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }


    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }


    //Methods I have made vvv


    /**
     *
     * This method saves the authenticated user to remember the auth token, and to ensure that the user does not have
     * access to anything they shouldn't have access to
     * @param authenticatedUser
     */
    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        restService.setAuthenticatedUser(authenticatedUser);
    }

    public void printDivider()
    {
        System.out.println();
        System.out.println("==============================================================");
    }

    /**
     *
     * This method is responsible for printing the account balance out to the screen
     *
     */
    public void handleAccountBalance()
    {
        BigDecimal balance = restService.viewCurrentBalance();
        printDivider();
        System.out.println("Your current account balance = $"+balance);
    }

    /**
     *
     *
     * This method handles printing a list of previous transfers to the screen. In addition, this method allows the user
     * to view a more detailed version of a specific transaction. Going into more depth, the String transferStatus
     * just keeps track of if the accepted or pending transfers are shown. The direction variable is meant to show whether
     * the user initiated or received a transfer. PastTransfers starts out listing all the records of transfers the user
     * sent, then when those run out, the method will show all the transfers that have been received.
     * @param status
     */
    public void handlePastTransfers(int status)
    {
        Set<Long> acceptableRequests = new HashSet<>();
        Request[] pastTransfers = restService.viewPastTransfers(status);
        String direction = "To";
        printDivider();
        String transferStatus = "Accepted Request:";
        if(status == 1)
        {
            transferStatus = "Pending Requests:";
        }
        if( pastTransfers != null && pastTransfers.length > 1)
        {
            System.out.println(transferStatus);
            printDivider();
            for(Request pastTransfer : pastTransfers)
            {
                if(pastTransfer.getUsername().equalsIgnoreCase("---FROM---"))
                {
                    direction = "From";
                    continue;
                }
                if(direction.equalsIgnoreCase("to"))
                {
                    acceptableRequests.add(pastTransfer.getTransferStatusId());
                }
                System.out.println("Transfer ID -> " + pastTransfer.getTransferStatusId()+ " - "+direction+" -> " + pastTransfer.getUsername() + " - Amount -> " + pastTransfer.getAmount());
            }
        }
        else {
            System.out.println("No transfers to show");
        }
        if(pastTransfers != null && pastTransfers.length > 0)
        {
            handleSpecificTransfer(pastTransfers, status, acceptableRequests);
        }
        printDivider();
    }

    /**
     *
     * This method drills down further into the transfer list, and shows the details of a specific transfer
     * @param pastTransfers
     */
    public void handleSpecificTransfer(Request[] pastTransfers, int status, Set<Long> acceptableList)
    {
        printDivider();
        long transferID = validateTransferID(pastTransfers);
        if(transferID == -1)
        {
            return;
        }
        if(transferID > 0) {
            DataHolder holder = restService.viewSpecificTransfer(transferID);
            printDivider();
            System.out.println();
            System.out.println("Transfer Details");
            printDivider();
            System.out.println("ID: " + holder.getTransfer_id());
            System.out.println("From: " + holder.getAccountFrom());
            System.out.println("To: " + holder.getAccountTo());
            System.out.println("Type: " + holder.getTypeDescription());
            System.out.println("Status: " + holder.getStatusDescription());
            System.out.println("Amount: " + holder.getAmount());
            printDivider();
            if(status == 1 && acceptableList.contains(holder.getTransfer_id()))
            {
                handlePendingTransfers(holder.getTransfer_id(), holder);
            }
        }
        else
        {
            System.out.println("Could not get transfer details");
        }
    }

    /**
     *
     * This method allows a user to approve or reject a pending transfer
     * @param transferID
     */
    public void handlePendingTransfers(long transferID, DataHolder dataHolder)
    {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("X: Exit");
        String userInputAsAString = scanner.nextLine().trim();
        while(true){
            if (userInputAsAString.equalsIgnoreCase("x")) {
                return;
            }
            try {
                int userInputAsInt = Integer.parseInt(userInputAsAString.trim());
                if(userInputAsInt == 1)
                {
                    long transfer = restService.changePendingToAccepted(transferID, dataHolder);
                    if(transfer != -1)
                    {
                        printDivider();
                        System.out.println("Your pending transfer is now accepted!");
                    }
                    return;
                }
                else if(userInputAsInt == 2)
                {
                    long transfer = restService.changePendingToRejected(transferID, dataHolder);
                    if(transfer != -1)
                    {
                        printDivider();
                        System.out.println("Your pending transfer it now rejected!");
                    }
                    return;
                }
                else
                {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a valid option");
                userInputAsAString = scanner.nextLine().trim();
            }
        }
    }

    /**
     *
     *
     * This is the data validate for handleSpecificTransfers(). This method will ensure that the only values returned
     * correspond to the transfer ID, or the option to quit out of this menu
     * @param requests
     * @return
     */
    public long validateTransferID(Request[] requests)
    {
        Set<Long> transferSet = new HashSet<>();
        for(Request request : requests)
        {
            transferSet.add(request.getTransferStatusId());
        }
        System.out.print("Select a Transfer ID, or type x to return to main menu: ");
        String idAsString = scanner.nextLine().trim();
        long validLong = -1;
        while(validLong < 0)
        {
            try
            {
                if(idAsString.equalsIgnoreCase("x"))
                {
                    break;
                }
                if(!transferSet.contains(Long.parseLong(idAsString)))
                {
                    throw new NumberFormatException();
                }
                validLong = Long.parseLong(idAsString);
            }catch (NumberFormatException nfe) {
                System.out.println("That was not a valid User ID, try again!");
                idAsString = scanner.nextLine().trim();
            }
        }
        return validLong;
    }

    /**
     *
     * This is the method that will initiate an accepted transfer. It will check to make sure that the user does not
     * transfer to themselves, and that they do not transfer more money than they have.
     *
     */
    public void handleSendRequest()
    {
        long userID = promptForUserID();
        BigDecimal amount = null;
        if(userID > 0)
        {
            amount = promptForAmount();
        }
        if(amount != null)
        {
            long transferID = restService.transfer(userID, amount, "accepted");
            if(transferID != -1)
            {
                printDivider();
                System.out.println("Your transfer was sent!");
            }
        }
    }

    /**
     *
     * This is the method that will initiate an accepted transfer. It will check to make sure that the user does not
     * transfer to themselves.
     *
     */
    public void handleReceiveRequest()
    {
        long userID = promptForUserID();
        BigDecimal amount = null;
        if(userID > 0)
        {
            amount = promptForReceiveAmount();
        }
        if(amount != null)
        {
            long transferID = restService.transfer(userID, amount, "pending");
            if(transferID != -1)
            {
                printDivider();
                System.out.println("Your transfer request was sent!");
            }
        }
    }

    /**
     *
     * This method is the data validator for the transfer amount in handleSendRequest(). It ensures that only BigDecimal
     * values are returned, and that the user does not enter more than they have. In addition, this also offers a way
     * to exit back to the main menu
     * @return
     */
    public BigDecimal promptForAmount()
    {
        BigDecimal amountAsBigDecimal;
        BigDecimal accountBalance = restService.viewCurrentBalance();
        while(true)
        {
            System.out.println("Enter the amount you would like to transfer, or type x to return to main menu: ");
            String amountAsAString = scanner.nextLine().trim();
            if(amountAsAString.equalsIgnoreCase("x"))
            {
                return null;
            }
            try
            {
                amountAsBigDecimal = new BigDecimal(amountAsAString);
                if(amountAsBigDecimal.compareTo(accountBalance) == 1)
                {
                    throw new Exception();
                }
                if(amountAsBigDecimal.compareTo(new BigDecimal("0")) == -1)
                {
                    throw new Exception();
                }
                return amountAsBigDecimal;
            }catch (NumberFormatException nfe) {
                System.out.println("The number you entered was not valid");
            }
            catch (Exception e)
            {
                System.out.println("Enter an amount greater than 0 but less than or equal to your account balance ($"+accountBalance+"): ");
            }
        }
    }

    /**
     *
     * This method is the data validator for the transfer amount in handleSendRequest(), it ensures that only BigDecimal
     * values are returned. In addition, this also offers a way to exit back to the main menu
     * @return
     */
    public BigDecimal promptForReceiveAmount()
    {
        BigDecimal amountAsBigDecimal;
        while(true)
        {
            System.out.println("Enter the amount you would like to transfer, or type x to return to main menu: ");
            String amountAsAString = scanner.nextLine().trim();
            if(amountAsAString.equalsIgnoreCase("x"))
            {
                return null;
            }
            try
            {
                amountAsBigDecimal = new BigDecimal(amountAsAString);
                if(amountAsBigDecimal.compareTo(new BigDecimal("0")) == -1)
                {
                    throw new Exception();
                }
                return amountAsBigDecimal;
            }catch (NumberFormatException nfe) {
                System.out.println("The number you entered was not valid");
            }
            catch (Exception e)
            {
                System.out.println("Enter a positive amount");
            }
        }
    }

    /**
     *
     * This method is the data validator for the transfer ID in handleSendRequest(), it ensures that the user can only
     * send money to users that are in the userID set values are returned. In addition, this also offers a way to
     * exit back to the main menu
     * @return
     */
    public long validateUserID(Set<Long> userIDs)
    {
        System.out.print("Select a User ID, or type x to return to main menu: ");
        String idAsString = scanner.nextLine().trim();
        long validLong = -1;
        while(validLong < 0)
        {
            try
            {
                if(idAsString.equalsIgnoreCase("x"))
                {
                    break;
                }
                if(!userIDs.contains(Long.parseLong(idAsString)))
                {
                    throw new NumberFormatException();
                }
                validLong = Long.parseLong(idAsString);
            }catch (NumberFormatException nfe) {
                System.out.println("That was not a valid User ID, try again!");
                idAsString = scanner.nextLine().trim();
            }
        }
        return validLong;
    }

    /**
     *
     * This method prints out the list of users before the authenticated user can make a transfer. This list will
     * omit the user id and username of the signed-in user to ensure that the user cannot send money to themselves in
     * the client
     * @return
     */
    public long promptForUserID()
    {
        User[] listOfUsers = restService.getAllUsers();
        long personalUserID = authenticatedUser.getUser().getUserId();
        Set<Long> setOfUserIDs = new HashSet<>();
        if(listOfUsers != null && listOfUsers.length > 0)
        {
            for(User user : listOfUsers)
            {
                if(user.getUserId() != personalUserID)
                {
                    setOfUserIDs.add(user.getUserId());
                    System.out.println("User ID -> " + user.getUserId() + " Username -> " + user.getUsername());
                }
            }
        }
        else
        {
            System.out.println("No users available to interact with");
            return -1;
        }
        return validateUserID(setOfUserIDs);
    }


}
