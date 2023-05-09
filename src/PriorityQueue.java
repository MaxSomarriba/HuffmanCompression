/*  Student information for assignment:
 *
 *  On MY honor, Max Somarriba, this programming assignment is MY own work
 *  and I have not provided this code to any other student.
 *
 *  Number of slip days used: 1
 *
 *  Student 1 (Student whose turnin account is being used)
 *  UTEID: mms6224
 *  email address: maxmsomarriba@gmail.com
 *  Grader name: Lilly
 *
 *  Student 2
 *  UTEID:
 *  email address:
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

public class PriorityQueue {
    // Internal storage container
    private ArrayList<TreeNode> con;

    // Public constructor
    public PriorityQueue(){
        con = new ArrayList<>();
    }

    // Public constructor given a bitInventory array
    public PriorityQueue(int[] bitInventory){
        con = new ArrayList<>();
        for(int currentIndex = 0; currentIndex < bitInventory.length; currentIndex++){
            if(bitInventory[currentIndex] > 0){
                enqueue(new TreeNode(currentIndex, bitInventory[currentIndex]));
            }
        }
    }

    // Public constructor given a premade tree
    public PriorityQueue(TreeNode tN){
        con = new ArrayList<>();
        con.add(tN);
    }


    // Enqueues the given TreeNode based on it's frequency
    private void enqueue(TreeNode tN){
        if(tN == null){
            throw new IllegalArgumentException("tN cannot be null");
        }
        int compareBorder = 0;
        boolean placed = false;
        // If the queue is already empty
        if(con.size() == 0){
            con.add(tN);
            placed = true;
        }
        int currentIndex = 0;
        placeNode(tN, compareBorder, placed, currentIndex);
    }

    // Private helper method to place node in the queue
    private void placeNode(TreeNode tN, int compareBorder, boolean placed, int currentIndex) {
        while(!placed && currentIndex < con.size()){
            TreeNode currentTreeNode = con.get(currentIndex);
            if(currentTreeNode.compareTo(tN) > compareBorder){
                con.add(currentIndex, tN);
                placed = true;
            } else if(currentTreeNode.compareTo(tN) == compareBorder){
                // If at the end of the list then add to the end of the list
                if(currentIndex == con.size() - 1){
                    con.add(tN);
                    placed = true;
                } else if(con.get(currentIndex + 1).getFrequency() !=
                        currentTreeNode.getFrequency()){
                    con.add(currentIndex +1, tN);
                    placed = true;
                }
                currentIndex++;
            } else if(currentTreeNode.compareTo(tN) < compareBorder){
                // move down the list if you need to node priority is less than currentNode
                if(currentIndex == con.size() - 1){
                    con.add(tN);
                    placed = true;
                }
                currentIndex++;
            }
        }
    }

    // Combines the priority TreeNodes into one tree and returns it
    public TreeNode makeTree(){
        int combineNodeNewValue = -1;
        int firstNodeIndex = 0;
        int secondNodeIndex = 1;
        // Enqueue EOF value
        this.enqueue(new TreeNode(IHuffConstants.PSEUDO_EOF,1));
        // Keep shrinking tree until only one node remains
        while(con.size() > 1){
            TreeNode combinedNode = new
                    TreeNode(con.get(firstNodeIndex),combineNodeNewValue,con.get(secondNodeIndex));
            // Remove both combined nodes
            con.remove(firstNodeIndex);
            con.remove(firstNodeIndex);
            // Enqueue new combined node
            this.enqueue(combinedNode);
        }
        // Return final combined node
        return con.get(firstNodeIndex);
    }

    // Public method to create and return an encoding table for the needed characters
    public HashMap<Integer,String> makeEncodingTable(){
        HashMap<Integer,String> encodingTable = new HashMap<>();
        makeEncodingTableHelper(encodingTable, con.get(0), "");
        return encodingTable;
    }

    // Recursive method that creates encoding table
    private void makeEncodingTableHelper(HashMap<Integer,String> encodingTable,
                                         TreeNode currentNode, String currentCode){
        int defaultValue = -1;
        if(currentNode != null){
            String code = currentCode;
            makeEncodingTableHelper(encodingTable, currentNode.getLeft(), code + "0");
            if(currentNode.getValue() != defaultValue) {
                encodingTable.put(currentNode.getValue(),currentCode);
            }
            makeEncodingTableHelper(encodingTable, currentNode.getRight(), code + "1");
        }
    }

    // Public method to create and return an decoding table for the needed characters
    public HashMap<String,Integer> makeDecodingTable(){
        HashMap<String,Integer> decodingTable = new HashMap<>();
        makeDecodingTableHelper(decodingTable, con.get(0), "");
        return decodingTable;
    }

    // Recursive method that creates decoding table
    private void makeDecodingTableHelper(HashMap<String,Integer> decodingTable,
                                         TreeNode currentNode, String currentCode){
        int defaultValue = -1;
        if(currentNode != null){
            String code = currentCode;
            makeDecodingTableHelper(decodingTable, currentNode.getLeft(), code + "0");
            if(currentNode.getValue() != defaultValue) {
                decodingTable.put(currentCode,currentNode.getValue());
            }
            makeDecodingTableHelper(decodingTable, currentNode.getRight(), code + "1");
        }
    }

    // Recursive method that counts the number of bits it will take to represent the tree header
    public int getTreeBitSize(TreeNode currentNode){
        int preLeafMarkerDigit = 1;
        // If at leaf node count 9 bits
        if(currentNode.isLeaf()){
            int digitsPerLeaf = IHuffConstants.BITS_PER_WORD + 1;
            return digitsPerLeaf + preLeafMarkerDigit;
        }
        // If not at leaf then keep moving down and add one bit to represent the 0
        return preLeafMarkerDigit + getTreeBitSize(currentNode.getLeft())
                + getTreeBitSize(currentNode.getRight());
    }
}