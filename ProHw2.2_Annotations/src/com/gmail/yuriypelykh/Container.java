package com.gmail.yuriypelykh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@SaveTo(path="file.txt")
public class Container {
    private String text = "Text here";

    @Saver
    public void save(String filePath) {
        File file = new File(filePath);
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.write(text);
            System.out.println("Text saved to file");
        } catch (FileNotFoundException e) {
            System.out.println("Writting to file error!");
        }
    }
}
