package com.assignment;


public class ChunkLinkDemo
{
    public static void main( String[] args )
    {
        ChunkLink chunkLink = new ChunkLink();
        chunkLink.linkChunks("C:\\Users\\dagig\\workspace\\chunk-link\\src\\sample.txt");

        chunkLink.reconstruct("copySample.txt");
    }
}
