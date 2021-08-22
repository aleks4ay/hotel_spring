package org.aleks4ay.hotel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public final class FileReader {
    private static final Logger log = LoggerFactory.getLogger(FileReader.class);

    public static byte[] file2byteArray(String fileName) {
        File file = new File(fileName);
        try {
            InputStream fis = new FileInputStream(file);
            byte[] fileInArray = new byte[(int) file.length()];
            try {
                fis.read(fileInArray);
                fis.close();
                return fileInArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
