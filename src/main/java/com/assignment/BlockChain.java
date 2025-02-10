package com.assignment;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import com.assignment.Block;
import java.util.Scanner;


public class BlockChain {
    private List<Block> chain;

    public BlockChain() {
        this.chain = new ArrayList<>();
        // Create the Genesis Block (First Block)
        chain.add(new Block(0, "Genesis Block", "0"));
    }

    public void addBlock(String data) {
        Block lastBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, lastBlock.getHash());
        chain.add(newBlock);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Validate the hash
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }

            // Ensure previous hash matches
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public void printBlockchain() {
        for (Block block : chain) {
            block.printBlock();
        }
    }

    public static void main(String[] args) {
        com.assignment.BlockChain blockchain = new com.assignment.BlockChain();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Blockchain initialized with a Genesis Block.");
        System.out.println("Do you want to add a block to the blockchain? (yes/no):");

        while (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter data for the new block: ");
            String data = scanner.nextLine();

            blockchain.addBlock(data);
            System.out.println("Block added successfully!");

            System.out.println("Current Blockchain:");
            blockchain.printBlockchain();

            System.out.println("Is Blockchain valid? " + blockchain.isChainValid());
            System.out.println("Do you want to add another block? (yes/no):");
        }

        System.out.println("Final Blockchain:");
        blockchain.printBlockchain();
        scanner.close();
    }
}