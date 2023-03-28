package org.example;

import java.util.HashMap;
import java.util.Random;

import static java.lang.Integer.valueOf;

public class Bank {

    private HashMap<Integer, Account> accounts;
    private final Random random = new Random();
    {
        accounts = completedAccounts();
    }

    public long getTotalBalance(){
        return accounts.values().stream()
                .mapToLong(Account::getBalance)
                .sum();
    }

    public synchronized boolean isFraud(int fromAccountNum, int toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(int fromAccountNum, int toAccountNum, int amount) throws InterruptedException {
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);

        Account firstLock, secondLock;
        if (valueOf(fromAccount.getAccNumber()) < valueOf(toAccount.getAccNumber())) {
            firstLock = fromAccount;
            secondLock = toAccount;
        }else {
            firstLock = toAccount;
            secondLock = fromAccount;
        }
        synchronized (firstLock){
            synchronized (secondLock){
                if (fromAccount.getBalance() >= amount && fromAccount.isBlock() || toAccount.isBlock()){}
                transaction(amount, toAccount, fromAccount);
                if(amount > 50_000){
                    if (isFraud(fromAccountNum, toAccountNum, amount)){
                        transaction(amount, toAccount, fromAccount);
                        fromAccount.blockAccount();
                        toAccount.blockAccount();
                    }
                }
            }
        }
    }

    private void transaction(long amount, Account fromAccount, Account toAccount) {
        if (fromAccount.withdrawMoney(amount)){
            toAccount.putMoney(amount);
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(int accountNum){
        Account account = accounts.get(accountNum);
        return account.getBalance();
    }

    public void setAccounts(HashMap<Integer, Account> accounts){
        this.accounts = accounts;
    }

    private static HashMap<Integer, Account> completedAccounts(){
        HashMap<Integer, Account> accountHashMap = new HashMap<>();
        for (int i = 0; i < 100; i++){
            long initialValue = (long) (80_000 + 20_000 * Math.random());
            Account account = new Account(i, initialValue);
            accountHashMap.put(i, account);
        }
        return accountHashMap;
    }
}
