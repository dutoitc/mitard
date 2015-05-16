package ch.mno.talend.mitard.tools;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dutoitc on 10.05.2015.
 */
public class DotHelper {

    public static void generatePNG(String dotPath, String filename, File dotData) throws IOException {
        String command = dotPath + "\\dot.exe -Tpng -o" + filename+" "+dotData.getAbsolutePath();
        System.out.println(command);
        Process process = Runtime.getRuntime().exec(command);
        InputStream is = process.getInputStream();
        InputStream is2 = process.getErrorStream();
        while (process.isAlive()) {
            System.out.println("1");
            try {
                if (is.available()>0) {
                    System.out.println("2");
                    int l = is.available();
                    byte[] b = new byte[l];
                    int l2 = is.read(b);
                    System.out.println(new String(b, 0, l2));

                    l = is.available();
                     b = new byte[l];
                    l2 = is2.read(b);
                    System.out.println(new String(b, 0, l2));
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String error = IOUtils.toString(process.getErrorStream());
        System.out.println(error);
    }

}
