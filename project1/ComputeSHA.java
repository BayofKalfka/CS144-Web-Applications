/**
 * Created by huxiongfeng on 13/01/17.
 * This is a Java program that computes the SHA-1 hash
 over the content of an input file.
 *
 */

import java.io.IOException;
import java.util.*;
import java.security.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class ComputeSHA {
    // Computes the SHA code as a string
    public static String computeSHA(String file) throws NoSuchAlgorithmException, IOException {
        byte[] input = getBytes(file);
        // Instantializes class MessageDigest to get the instance md
        MessageDigest md;
            // MessageDigest isntance md works for algorithm SHA-1
            md = MessageDigest.getInstance("SHA-1");
            // Initializes MessageDigest isntance md using update methods
            md.update(input);
            byte[] mdSHA1 = md.digest();
            StringBuilder sb1 = new StringBuilder();
            for (byte bytes : mdSHA1) {
                sb1.append(String.format("%02x", bytes & 0xff));
            }
            return new String(sb1);
    }

    // helper function used to read the Line string from a file
    private static byte[] getBytes(String file) throws IOException {
        // remember to seperate "~/vm-shared/"  and  "file" !!
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            return bytes;
    }
    public static void main(String[] args) {
        try {
            for(String s : args) {
                String res = ComputeSHA.computeSHA(s);
                System.out.println(res);
            }
        } catch(NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
}
