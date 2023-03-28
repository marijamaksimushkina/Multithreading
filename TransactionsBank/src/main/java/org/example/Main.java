package org.example;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final int THREADS = 10;
    private static final int THREAD_COUNT = 100;
    private static final CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);


    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        System.out.println("Баланс: " + bank.getTotalBalance() + " руб.");
        ExecutorService service = Executors.newFixedThreadPool(THREADS);
        long start = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++){
            service.execute(() -> {
                try{
                    for (int j = 1; j < 100; j++){
                        int amount = (int) (10_000 + 45_000 * Math.random());
                        bank.transfer(j, j + 1, amount);
                        if (j == 99){
                            break;
                        }
                    }
                    countDownLatch.countDown();
                }catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            });
        }
        countDownLatch.await();
        service.shutdown();
        System.out.println(("Время работы: " + (System.currentTimeMillis() - start) / 1000) + " сек.");
        System.out.println("Баланс: " + bank.getTotalBalance() + " руб.");
    }
}