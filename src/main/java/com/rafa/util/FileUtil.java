package com.rafa.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
public class FileUtil {
    
    public static File multipartFileToFile(MultipartFile file, String prefix) throws IOException {
        String[] stringList = file.getOriginalFilename().split("\\.");
        File convFile = File.createTempFile(prefix + "_", "." + stringList[stringList.length - 1]);
        convFile.createNewFile();
        FileOutputStream fos = null;
        fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        convFile.deleteOnExit();
        return convFile;
    }

    public static File createSampleFile(String fileName, String body) {

        //Creating a sample file
        File sourceFile = null;
        Writer output = null;
        try {
            sourceFile = File.createTempFile(fileName, ".html");
            System.out.println("Creating a sample file at: " + sourceFile.toString());
            output = new BufferedWriter(new FileWriter(sourceFile));
            output.write(body);
            output.close();
        } catch (Exception e) {
            log.info("Failure creating file to Azure Storage");
            throw new RuntimeException("Error creating sample file");
        }

        return sourceFile;
    }
}
