package com.assignment;

/**
 * Demonstrates the usage of ChunkLink for file processing and reconstruction.
 */
public class ChunkLinkDemo
{
    /**
     * Main entry point demonstrating ChunkLink functionality.
     * @param args Command line arguments (not used)
     */
    public static void main( String[] args )
    {
        ChunkLink chunkLink = new ChunkLink();
        chunkLink.linkChunks("C:\\Users\\dagig\\workspace\\chunk-link\\src\\sample.txt");

        chunkLink.reconstruct("copySample.txt");
    }
}
