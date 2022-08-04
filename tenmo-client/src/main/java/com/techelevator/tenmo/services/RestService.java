package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import io.cucumber.java.an.E;
import org.apiguardian.api.API;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This is the client side class that talks to the server it takes in an authenticated user object to ensure the
 * authToken gets passed to the server in the HTTP header
 *
 */
public class RestService { // I talk to the server side!

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser = null;

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    /**
     *
     * This method gets the user's current balance
     * @return
     */
    public BigDecimal viewCurrentBalance() {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "account", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error!");
        }
        return balance;
    }

    /**
     *
     * This method gets detailed information on a specific transfer
     * @param transferID
     * @return
     */
    public DataHolder viewSpecificTransfer(long transferID)
    {
        DataHolder holder = null;
        try
        {
            ResponseEntity<DataHolder> response = restTemplate.exchange(API_BASE_URL + "transfer/"+transferID, HttpMethod.GET, makeAuthEntity(), DataHolder.class);
            holder = response.getBody();
        }catch (ResourceAccessException e)
        {
            System.out.println("Could not get access api");
        }
        catch (RestClientResponseException rce)
        {
            System.out.println("Could not access specific transfer info");
        }
        return holder;
    }

    /**
     *
     * This method returns a list of transfers by status
     * @param status
     * @return
     */
    public Request[] viewPastTransfers(int status) {
        Request[] transferList = null;
        try {
            ResponseEntity<Request[]> response = restTemplate.exchange(API_BASE_URL + "transfer/status/"+status, HttpMethod.GET, makeAuthEntity(), Request[].class);
            transferList = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("You don't have any past transfers to view! Choose another option.");
        }
        return transferList;
    }

    /**
     *
     *
     *
     * @return
     */

    public User[] getAllUsers() {
        User[] userList = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "user", HttpMethod.GET, makeAuthEntity(), User[].class);
            userList = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error!");
        }
        return userList;
    }

    /**
     *
     * Creates an HTTP Entity with the authToken passes into the header, and the Transfer datatype passed in
     * the body
     * @param transfer
     * @return
     */
     private HttpEntity<Transfer> makeATransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<DataHolder> makeADataHolder(DataHolder dataHolder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(dataHolder, headers);
    }

    /**
     *
     * This method allows a user to accept a pending transfer request
     * @param transferID
     * @param dataHolder
     * @return
     */
    public long changePendingToAccepted(long transferID, DataHolder dataHolder)
    {
        Long finishedID = (long)-1;
        try{
            ResponseEntity<Long> response = restTemplate.exchange(API_BASE_URL + "transfer/pending_to_accepted", HttpMethod.POST, makeADataHolder(dataHolder), Long.class);
            finishedID = response.getBody();
            finishedID = (long)1;
        }catch (RestClientResponseException e)
        {
            System.out.println("Could not change database");
        }
        catch (ResourceAccessException rea)
        {
            System.out.println("Could not reach database");
        }
        return finishedID;
    }

    /**
     *
     * This method allows a user to reject a pending transfer reauest
     * @param transferID
     * @param dataHolder
     * @return
     */
    public long changePendingToRejected(long transferID, DataHolder dataHolder)
    {
        Long finishedID = (long)-1;
        try{
            ResponseEntity<Long> response = restTemplate.exchange(API_BASE_URL + "transfer/pending_to_rejected", HttpMethod.PUT, makeADataHolder(dataHolder), Long.class);
            finishedID = response.getBody();
            finishedID = (long)1;
        }catch (RestClientResponseException e)
        {
            System.out.println("Could not change database");
        }
        catch (ResourceAccessException rea)
        {
            System.out.println("Could not reach database");
        }
        return finishedID;
    }

    /**
     *
     *
     * This method sends a transfer request to the API. The status and type ids are not set on the client side, but will
     * be set on the server side to ensure requests are uniform
     * @param accountId
     * @param amount
     * @param status
     * @return
     */
    public long transfer(long accountId, BigDecimal amount, String status) {
        Transfer transfer = new Transfer(authenticatedUser.getUser().getUserId(), accountId, amount);
        Long transferId = (long)-1;
        try {
            ResponseEntity<Long> response = restTemplate.exchange(API_BASE_URL + "transfer/"+status, HttpMethod.POST, makeATransfer(transfer), Long.class);
            transferId = response.getBody();
        } catch (RestClientResponseException rce) {
            System.out.println("Error, could not complete transaction");
        } catch (ResourceAccessException rae)
        {
            System.out.println("Error, could not connect to the database");
        }
        return transferId;
    }

    /**
     *
     * Creates an HTTP Entity with the authToken passed into the header
     * @return
     */
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
}
