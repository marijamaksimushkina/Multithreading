package org.example;

public class Account {
    private long balance;
    private int accNumber;
    private boolean isBlock = false;

    public Account(int accNumber,long balance) {
        this.accNumber = accNumber;
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public synchronized boolean withdrawMoney(long money) {
        if (balance >= money) {
            balance -= money;
            return true;
        }
        return false;
    }

    public synchronized void putMoney(long money) {
        balance += money;
    }

    public synchronized void blockAccount() {
        isBlock = true;
    }

    public int getAccNumber() {
        return accNumber;
    }
}

