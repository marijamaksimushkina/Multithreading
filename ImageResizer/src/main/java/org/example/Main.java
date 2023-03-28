package org.example;

import java.io.File;

public class Main {
    private  static int newWidth = 300;

    public static void main(String[] args) {

        Runtime runtime = Runtime.getRuntime();
        int cores = runtime.availableProcessors();

        String srcFolder = "/users/sortedmap/Desktop/src";
        String dstFolder = "/users/sortedmap/Desktop/dst";

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        int middle = files.length / cores;

        for (int i = 0; i < cores - 1; i++){
            File[] files1 = new File[middle];
            System.arraycopy(files, 0 + (middle * i), files1, 0, middle);
            ImageResizer resizer1 = new ImageResizer(files1, newWidth, dstFolder, start);
            new Thread(resizer1).start();
        }

        File[] files2 = new File[files.length - middle * (cores - 1)];

        System.arraycopy(files, middle * (cores - 1), files2, 0, files2.length);
        ImageResizer resizer2 = new ImageResizer(files2, newWidth, dstFolder, start);
        new Thread(resizer2).start();
    }
}