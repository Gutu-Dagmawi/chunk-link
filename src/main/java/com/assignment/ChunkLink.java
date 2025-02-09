package com.assignment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;


public class ChunkLink {
    private Node metaNode;
    private Node tailNode;

    void linkChunks(String path){
        try (BufferedInputStream bufferInputStream = new BufferedInputStream(Files.newInputStream(Paths.get(path)))) {
            byte[] tempBuffer = new byte[1024 * 1024];
            int bytesRead;
            while ((bytesRead = bufferInputStream.read(tempBuffer)) != -1) {
                String chunk = new String(tempBuffer, 0, bytesRead);
                add(chunk);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }   

    void add(String data){
        Node newNode = new Node(data);
        if(metaNode == null){
            metaNode = tailNode = newNode;
            return;
        }

        tailNode.setNextCheckSum(newNode);
        tailNode.setNextNode(newNode);
        tailNode = newNode;
    }

    void deleteLink() {
        while (metaNode != null) {
            Node temp = metaNode;
            metaNode = metaNode.getNextNode();
            temp.setNextNode(null);
        }
        tailNode = null;
        System.out.println("Chunk-Link has been cleared and is empty.");
    }


    public void reconstruct(String newFilePath) {
        Node currentNode = metaNode;
        Path filePath = Paths.get(newFilePath);

        try {
            Files.deleteIfExists(filePath); // Ensure fresh reconstruction
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (currentNode != null) {
            if (currentNode.getNextNode() != null && !currentNode.validateCheckSum(currentNode.getNextNode())) {
                System.out.println("Checksum mismatch detected. Reconstruction aborted.");
                return;
            }

            try {
                Files.write(filePath, currentNode.getData().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            currentNode = currentNode.getNextNode();
        }
    }




}
