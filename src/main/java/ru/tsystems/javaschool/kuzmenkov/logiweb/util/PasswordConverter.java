package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import org.springframework.security.authentication.encoding.BasePasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Nikolay Kuzmenkov.
 */
public class PasswordConverter extends BasePasswordEncoder {

    public static String getMD5Hash(String userPassword) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        byte[] array = md.digest(userPassword.getBytes());
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }

        return sb.toString();
    }

    @Override
    public String encodePassword(String s, Object o) {
        try {
            return getMD5Hash(s);
        }
        catch (NoSuchAlgorithmException ex) {
            return s;
        }
    }

    @Override
    public boolean isPasswordValid(String s, String s1, Object o) {
        try {
            return (s.equals(getMD5Hash(s1)));

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
