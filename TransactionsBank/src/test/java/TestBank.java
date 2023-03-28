import junit.framework.TestCase;
import org.example.Account;
import org.example.Bank;
import org.junit.After;
import org.junit.Before;

import java.util.HashMap;

public class TestBank extends TestCase {
    private Bank bank;
    private HashMap<Integer, Account> accountHashMap = new HashMap<>();
    private Account Client1;
    private Account Client2;
    private Account Client3;
    private Account dd;

    @Override
    @After
    public void setUp(){

        bank = new Bank();
        Client1 = new Account(1, 50_000);
        Client2 = new Account(2, 50_000);
        Client3 = new Account(3, 50_000);
        dd = new Account(4, 60_000);

        accountHashMap.put(1, Client1);
        accountHashMap.put(2, Client2);
        accountHashMap.put(3, Client3);
        accountHashMap.put(4, dd);

        bank.setAccounts(accountHashMap);
    }

    @Before
    public void clearBank(){
        bank = null;
    }

    public void testOneThread() throws InterruptedException {
        bank.transfer(1, 2, 1000);
        long actualFrom = Client1.getBalance();
        long expectedFrom = 51_000;
        long actualTo = Client2.getBalance();
        long expectedTo = 49_000;

        assertEquals(expectedFrom, actualFrom);
        assertEquals(expectedTo, actualTo);
    }

    public void testManyThread() throws InterruptedException {
        for (int i = 0; i < 10; i++){
            new Thread(() -> {
                    try {
                        bank.transfer(1, 2, 1000);
                        bank.transfer(1, 3, 1000);
                        bank.transfer(3, 1, 1000);
                        bank.transfer(3, 2, 1000);
                        bank.transfer(2, 1, 1000);
                        bank.transfer(2, 3, 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }).start();
        }
        Thread.sleep(100);
        long actualClient1 = Client1.getBalance();
        long expectedClient1 = 40_000;
        long actualClient2 = Client2.getBalance();
        long expectedClient2 = 40_000;
        long actualClient3 = Client3.getBalance();
        long expectedClient3 = 70_000;

        assertEquals(expectedClient1, actualClient1);
        assertEquals(expectedClient2, actualClient2);
        assertEquals(expectedClient3, actualClient3);
    }
}
