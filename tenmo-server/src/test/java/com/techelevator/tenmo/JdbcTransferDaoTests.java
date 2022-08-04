package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.DataHolder;
import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
public class JdbcTransferDaoTests extends BaseDaoTests {



    // SET FAKE DATA, per Transfer model requires Long transferStatusId, Long transferTypeId, Long account_from, Long account_to, BigDecimal amount
    private static final Transfer TRANSFER_1 = new Transfer((long) 1, (long) 1, (long) 2001, (long) 2002, BigDecimal.valueOf(100)); //transferStatus: Pending, transferType Request
    private static final Transfer TRANSFER_2 = new Transfer((long) 1, (long) 1, (long) 1002, (long) 1001, BigDecimal.valueOf(-100)); //transferStatus: Pending, transferType Request
    private static final Transfer TRANSFER_5 = new Transfer(null, null, null, null, null); //transferStatus: Pending, transferType Send
    private static final Transfer TRANSFER_6 = new Transfer((long) 1, null, null, null, BigDecimal.valueOf(100)); //transferStatus: Pending, transferType Send
    private static final Transfer TRANSFER_7 = new Transfer((long) 2, (long) 1, (long) 1001, (long) 1002, BigDecimal.valueOf(100)); //transferStatus: Approved, transferType Request
    private static final Transfer TRANSFER_8 = new Transfer((long) 2, (long) 1, (long) 1002, (long) 1001, BigDecimal.valueOf(100)); //transferStatus: Approved, transferType Request
    private static final Transfer TRANSFER_9 = new Transfer((long) 2, (long) 2, (long) 1002, (long) 1001, BigDecimal.valueOf(100)); //transferStatus: Approved, transferType Send
    private static final Transfer TRANSFER_10 = new Transfer((long) 2, (long) 2, (long) 1001, (long) 1002, BigDecimal.valueOf(100));//transferStatus: Approved, transferType Send
    private static final Transfer TRANSFER_11 = new Transfer((long) 3, (long) 2, (long) 1002, (long) 1001, BigDecimal.valueOf(100)); //transferStatus: Rejected, transferType Send
    private static final Transfer TRANSFER_12 = new Transfer((long) 3, (long) 2, (long) 1001, (long) 1002, BigDecimal.valueOf(100)); //transferStatus: Rejected, transferType Send
    private static final Transfer TRANSFER_13 = new Transfer((long) 3, (long) 1, (long) 1002, (long) 1001, BigDecimal.valueOf(100)); //transferStatus: Rejected, transferType Request
    private static final Transfer TRANSFER_14 = new Transfer((long) 3, (long) 1, (long) 1001, (long) 1002, BigDecimal.valueOf(100)); //transferStatus: Rejected, transferType Request

    private JdbcTransferDao sut;
    private Transfer testTransfer;

    @Before
    public void setup() {
        sut = new JdbcTransferDao(new JdbcTemplate(dataSource));
    }

    @Test
    public void findAllTransfersByUser_returns_all_transfers_for_account_id(){
        List<Request> allTransfers = sut.findAllTransfersByUser((long)2001, 1); //list all transfers for acct 2001 with transferStatus of 1
        System.out.println(allTransfers.get(0).getTransferStatusId());
        System.out.println(allTransfers.get(1).getTransferStatusId());
        System.out.println(allTransfers.get(2).getTransferStatusId());
        Assert.assertEquals(3, allTransfers.size()); //expected 3, actual 3 - 2 transfers, then one more record for --FROM--

    }

    @Test
    public void findAllTransfersByUser_returns_null_for_account_id_no_exist(){
        List<Request> allTransfers = sut.findAllTransfersByUser((long)1001, 1);
        Assert.assertEquals(1, allTransfers.size()); // expected 1, actual 1
    }
    @Test
    public void findAllTransfersByType_returns_all_transfers_for_status(){
        List<Request> transfersByType = sut.findAllTransfersByType("Pending");
        Assert.assertEquals(3, transfersByType.size()); //expected 4, actual 0

    }
    @Test
    public void findAllTransfersByType_returns_empty_for_status_not_found(){
        List<Request> transfersByType = sut.findAllTransfersByType("Aaargh");
        Assert.assertEquals(0, transfersByType.size());
    }

    @Test
    public void pending_to_rejected_returns_transfer_with_id_and_expected_values(){
        long transferID = 3001;
        sut.changePendingToRejected(3001);
        DataHolder requestList = sut.findByTransferId(transferID);
        Assert.assertEquals("Rejected", requestList.getStatusDescription());
    }

    @Test
    public void add_transfer_records_correct_transfer_for_correct_info()
    {
        long transferID = sut.addTransfer(TRANSFER_1);
        boolean bob = transferID > 0;
        Assert.assertTrue(bob);
    }

    @Test
    public void add_transfer_records_fails_for_incorrect_info()
    {
        long transferID = sut.addTransfer(TRANSFER_2);
        boolean bob = transferID == -2;
        Assert.assertTrue(bob);
    }

    @Test
    public void add_transfer_records_fails_for_incorrect_info1()
    {
        long transferID = sut.addTransfer(TRANSFER_6);
        boolean bob = transferID == -1;
        Assert.assertTrue(bob);
    }

    @Test
    public void add_transfer_records_fails_for_incorrect_info2()
    {
        long transferID = sut.addTransfer(TRANSFER_5);
        boolean bob = transferID == -2;
        Assert.assertTrue(bob);
    }

    @Test
    public void find_by_transfer_id_happy_path()
    {
        DataHolder dH = sut.findByTransferId((long)3001);
        Assert.assertEquals(dH.getAccountTo(), "User 2");
        Assert.assertEquals(dH.getAccountFrom(), "User 1");
        Assert.assertEquals(dH.getTypeDescription(), "Send");
        Assert.assertEquals(dH.getStatusDescription(), "Approved");
        Assert.assertEquals(dH.getAmount(), new BigDecimal("50.00"));
    }

    @Test
    public void find_by_transfer_id__not_so_happy_path()
    {
        DataHolder dH = sut.findByTransferId((long)5);
        Assert.assertNull(dH);
    }
}


