package com.assignment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a node in the ChunkLink structure containing file data and checksum validation.
 * Each node stores a portion of file data and maintains SHA-256 hash of the next node's data
 * to ensure data integrity across the chain.
 */
public class Node {
    /** The chunk data stored in this node */
    private final String data;
    /** SHA-256 hash of the next node's data for integrity verification */
    private String nextCheckSum;
    /** Reference to the next node in the chain */
    private Node nextNode;

    /**
     * Creates a node with raw, un-hashed data.
     * @param data The chunk data to store
     */
    Node(String data)  {
        this.data = data;
    }

    /**
     * Converts a string to its SHA-256 hash representation.
     * @param data String to be hashed
     * @return Hexadecimal string representation of the SHA-256 hash
     * @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
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

    /**
     * @return The stored data in this node
     */
    public String getData(){
        return data;
    }

    /**
     * @return Reference to the next node in the chain
     */
    public Node getNextNode(){
        return nextNode;
    }
    /**
     * @return The SHA-256 hash of the next node's data
     */
    public String getNextCheckSum(){
        return nextCheckSum;
    }
    /**
     * Sets the reference to the next node in the chain.
     * @param node The node to be set as next
     */
    public void setNextNode(Node node){
        nextNode = node;
    }

    /**
     * Updates the checksum for the next node.
     * Computes and stores the SHA-256 hash of the provided node's data.
     * @param node The node whose data will be hashed
     */
    public void setNextCheckSum(Node node) {
        if (node == null) return;
        try {
            nextCheckSum = stringToHash(node.getData()); // Hash the actual data of the next node
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the integrity of the link between this node and the next.
     * Compares the stored checksum with a fresh hash of the next node's data.
     * @param node The next node to validate against stored checksum
     * @return true if the checksum matches, false if null or mismatch
     */
    public boolean validateCheckSum(Node node) {
        if (node == null) return false; // Prevent null pointer exception
        try {
            return nextCheckSum.equals(stringToHash(node.getData())); // Compare hash of next node's data
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error in checksum validation", e);
        }
    }

}
