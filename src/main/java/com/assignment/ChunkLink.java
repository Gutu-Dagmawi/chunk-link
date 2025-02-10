package com.assignment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * A linked list implementation for handling large files in chunks with checksum validation.
 * Each chunk is stored in a Node with a checksum of the next chunk for data integrity.
 * This structure allows for efficient file processing and integrity verification.
 */
public class ChunkLink {
    /** Head node of the chunk link */
    private Node metaNode;
    /** Tail node for efficient additions */
    private Node tailNode;

    /**
     * Reads a file in chunks and creates a linked structure with checksums.
     * Each chunk is approximately 1MB in size.
     * @param path Path to the source file to be processed
     */
    void linkChunks(String path){
        try (BufferedInputStream bufferInputStream = new BufferedInputStream(Files.newInputStream(Paths.get(path)))) {
            byte[] tempBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferInputStream.read(tempBuffer)) != -1) {
                String chunk = new String(tempBuffer, 0, bytesRead);
                add(chunk);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }   

    /**
     * Adds a new chunk of data to the link.
     * Updates the previous node's checksum and maintains the tail reference.
     * @param data String content to be added as a new node
     */
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

    /**
     * Clears the entire chunk link and releases memory.
     * Properly nullifies all references for garbage collection.
     */
    void deleteLink() {
        while (metaNode != null) {
            Node temp = metaNode;
            metaNode = metaNode.getNextNode();
            temp.setNextNode(null);
        }
        tailNode = null;
        System.out.println("Chunk-Link has been cleared and is empty.");
    }

    /**
     * Reconstructs the original file from chunks while validating checksums.
     * If a checksum validation fails, the reconstruction is aborted.
     * @param newFilePath Path where the reconstructed file will be saved
     */
    public void reconstruct(String newFilePath) {
        Node currentNode = metaNode;
        Path filePath = Paths.get(newFilePath);

        try {
            Files.deleteIfExists(filePath); // Ensure fresh reconstruction
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Handle the case whereby the current node isn't null but the checksum validation fails
        while (currentNode != null) {
            if (currentNode.getNextNode() != null && !currentNode.validateCheckSum(currentNode.getNextNode())) {
                System.out.println("Checksum mismatch detected. Reconstruction aborted.");
                return;
            }

            try {
                // Appending, since we don't want the file to be overwritten with every iteration
                Files.write(filePath, currentNode.getData().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            currentNode = currentNode.getNextNode();
        }
    }
}
