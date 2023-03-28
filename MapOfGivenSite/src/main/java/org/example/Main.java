package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите адрес сайта: ");
        System.out.println("Пример ввода: https://skillbox.ru/");
        String url = scanner.nextLine();

        System.out.println("Укажите количество создаваемых потоков: ");
        int countThread = scanner.nextInt();

        System.out.println("Начинаем сканирование...");
        long start = System.currentTimeMillis();
        LinkExecutor linkExecutor = new LinkExecutor(url, url);
        String mapOfGivenSite = countThread == 0 ? new ForkJoinPool().invoke(linkExecutor)
                : new ForkJoinPool(countThread).invoke(linkExecutor);

        System.out.println("Сканирование завершено!");
        System.out.println("Время сканирования: " + ((System.currentTimeMillis() - start) / 1000) + "сек.");
        createFiles(mapOfGivenSite);
    }

    private static void createFiles(String map) {
        String filePath = "SiteMap.txt";

        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Карта успешно создана!");
    }
}