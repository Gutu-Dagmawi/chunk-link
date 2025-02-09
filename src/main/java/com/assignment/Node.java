package com.assignment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Node {
    private String data;
    private String nextCheckSum;
    private Node nextNode;

    Node(String data)  {
        this.data = data;
    }

    Node(String data, String nextCheckSum){
        try {
            this.data = stringToHash(data);
            this.nextCheckSum = stringToHash(nextCheckSum);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());;
        }
    }

    // Class Methods
    private static String stringToHash(String data) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] digestedByte = digester.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hashedHex = new StringBuilder();

        for (byte b : digestedByte) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hashedHex.append('0');

            hashedHex.append(hex);
        }
        return hashedHex.toString();
    }

    // Getters and Setters
    public String getData(){
        return data;
    }

    public Node getNextNode(){
        return nextNode;
    }
    public String getNextCheckSum(){
        return nextCheckSum;
    }
    public void setNextNode(Node node){
        nextNode = node;
    }

    public void setNextCheckSum(Node node) {
        if (node == null) return;
        try {
            nextCheckSum = stringToHash(node.getData()); // Hash the actual data of the next node
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    // Public helpers
    public boolean validateCheckSum(Node node) {
        if (node == null) return false; // Prevent null pointer exception
        try {
            return nextCheckSum.equals(stringToHash(node.getData())); // Compare hash of next node's data
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error in checksum validation", e);
        }
    }


}
