package com.ning;

import org.apache.commons.codec.binary.Base64;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        byte[] client_fingerprint = Base64.decodeBase64("9CtptObfABIi8-s");
        System.out.println(client_fingerprint);
    }
}
