package com.assignment;

public class Node {
    private final String data;
    private String nextCheckSum;
    private Node nextNode;

    Node(String data){
        this.data = data;
    }

    Node(String data, String nextCheckSum){
        this.data = data;
        this.nextCheckSum = nextCheckSum;
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
    public void setNextCheckSum(String checkSum){
        nextCheckSum = checkSum;
    }
}
