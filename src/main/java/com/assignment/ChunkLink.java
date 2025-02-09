package com.assignment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ChunkLink {
    private Node metaNode;
    private Node tailNode;

    public void linkChunks(String path){
        try (BufferedInputStream bufferInputStream = new BufferedInputStream(Files.newInputStream(Paths.get(path)))) {
            byte[] tempBuffer = new byte[1024 * 1024];
            int bytesRead;
            while ((bytesRead = bufferInputStream.read(tempBuffer)) != -1) {
                String chunk = new String(tempBuffer, 0, bytesRead);
                add(chunk);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException(e);
        }
    }   

    public void add(String data) throws NoSuchAlgorithmException{
        Node newNode = new Node(data);
        if(metaNode == null){
            metaNode = tailNode = newNode;
        }

        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] digestedByte = digester.digest(data.getBytes());
        StringBuilder hashedHex = new StringBuilder();

        for (byte b : digestedByte) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hashedHex.append('0');

            hashedHex.append(hex);
        }

        tailNode.setNextCheckSum(String.valueOf(hashedHex));
        tailNode.setNextNode(newNode);
        tailNode = newNode;
    }

    public File reconstruct(){
        Node currentNode = metaNode;
	    return null;
    }

    private boolean isValidCheckSum(Node node){
       String checkSum = node.getNextCheckSum();
    }
}
