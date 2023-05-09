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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class SimpleHuffProcessor implements IHuffProcessor {
    private IHuffViewer myViewer;
    // Compression format of header
    private int compressionFormat;
    // Inventory of all character frequencies
    private int[] bitInventory;
    // Priority Queue that stores values from bitInventory
    private PriorityQueue bitQueue;
    // Tree Node that represents the entire huffman tree
    private TreeNode huffTree;
    // Encoding table with codes for each needed character
    private HashMap<Integer,String> encodingTable;
    // Amount of total bits written to compressed file
    private int totalBitsWritten;
    // Difference between totalBitsWritten and the file's uncompressed bit size
    private int bitDifference;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int INTERNAL_NODE = 0;
    private final int LEAF = 1;

    /**
     * Preprocess data so that compression is possible ---
     * count characters/create tree/store state so that
     * a subsequent call to compress will work. The InputStream
     * is <em>not</em> a BitInputStream, so wrap it int one as needed.
     * @param in is the stream which could be subsequently compressed
     * @param headerFormat a constant from IHuffProcessor that determines what kind of
     * header to use, standard count format, standard tree format, or
     * possibly some format added in the future.
     * @return number of bits saved by compression or some other measure
     * Note, to determine the number of
     * bits saved, the number of bits written includes
     * ALL bits that will be written including the
     * magic number, the header format number, the header to
     * reproduce the tree, AND the actual data.
     * @throws IOException if an error occurs while reading from the input file.
     */
    public int preprocessCompress(InputStream in, int headerFormat) throws IOException {
        compressionFormat = headerFormat;
        BitInputStream bIS = new BitInputStream(in);
        bitInventory = new int[ALPH_SIZE];
        int uncompressedBitSize = 0;
        // Create the bitInventory and track how many bits are uncompressed
        uncompressedBitSize = makeBitInventory(bIS, uncompressedBitSize);
        createCompressionDataStructures();
        totalBitsWritten = 0;
        // Count Magic Number and header format number
        totalBitsWritten += BITS_PER_INT * 2;
        // Count bits from header depending on format
        if(headerFormat == STORE_COUNTS){
            countStoreCountsHeaderBits();
        }else if(headerFormat == STORE_TREE){
            countStoreTreeHeaderBits();
        }
        // Count the length of bits for the PSEUDO_EOF value
        totalBitsWritten += encodingTable.get(PSEUDO_EOF).length();
        bIS.close();
        // Return saved number of bits
        bitDifference = uncompressedBitSize - totalBitsWritten;
        return bitDifference;
    }

    // Creates the bitQueue, huffTree, and encodingTable
    private void createCompressionDataStructures() {
        bitQueue = new PriorityQueue(bitInventory);
        huffTree = bitQueue.makeTree();
        encodingTable = bitQueue.makeEncodingTable();
    }

    // Counts the number of bits for the store tree header
    private void countStoreTreeHeaderBits() {
        // Add the initial bit representation of the size of the tree
        // Add the total number of bits to represent the tree through getTreeBitSize
        totalBitsWritten += BITS_PER_INT + bitQueue.getTreeBitSize(huffTree);
        for(int currentChar = 0; currentChar < ALPH_SIZE; currentChar++){
            // Count bits to be in actual data
            String currentCode = encodingTable.get(currentChar);
            if(currentCode != null) {
                totalBitsWritten += (bitInventory[currentChar] * currentCode.length());
            }
        }
    }

    // Counts the number of bits for the store counts header
    private void countStoreCountsHeaderBits() {
        // Loop through every possible char in bitInventory
        for(int currentChar = 0; currentChar < ALPH_SIZE; currentChar++){
            totalBitsWritten += BITS_PER_INT;
            // Count bits to be in actual data
            String currentCode = encodingTable.get(currentChar);
            if(currentCode != null) {
                totalBitsWritten += (bitInventory[currentChar] * currentCode.length());
            }
        }
    }

    // Adds frequencies of every character into a frequency array and returns the amount
    // of uncompressed bits
    private int makeBitInventory(BitInputStream bIS, int uncompressedBitSize) throws IOException {
        int currentBit = bIS.readBits(IHuffConstants.BITS_PER_WORD);
        // Read until the end of the uncompressed file
        while(currentBit != -1){
            uncompressedBitSize += BITS_PER_WORD;
            bitInventory[currentBit]++;
            currentBit = bIS.readBits(IHuffConstants.BITS_PER_WORD);
        }
        return uncompressedBitSize;
    }


    /**
     * Compresses input to output, where the same InputStream has
     * previously been pre-processed via <code>preprocessCompress</code>
     * storing state used by this call.
     * <br> pre: <code>preprocessCompress</code> must be called before this method
     * @param in is the stream being compressed (NOT a BitInputStream)
     * @param out is bound to a file/stream to which bits are written
     * for the compressed file (not a BitOutputStream)
     * @param force if this is true create the output file even if it is larger than the input file.
     * If this is false do not create the output file if it is larger than the input file.
     * @return the number of bits written.
     * @throws IOException if an error occurs while reading from the input file or
     * writing to the output file.
     */
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {
        if(bitDifference > 0 || force){
            // Initialize the bit input and output streams
            BitInputStream inStream = new BitInputStream(in);
            BitOutputStream outStream = new BitOutputStream(out);
            // Write magic number
            outStream.writeBits(BITS_PER_INT,MAGIC_NUMBER);
            // Write compression format
            outStream.writeBits(BITS_PER_INT, compressionFormat);
            // Write header according to format
            writeHeader(outStream);
            // Write the actual data
            writeData(inStream, outStream);
            // Write the PSEUDO_EOF value
            String currentCode = encodingTable.get(PSEUDO_EOF);
            writeBitsFromString(outStream,currentCode);
            outStream.close();
            inStream.close();
        } else {
            // Error if it would be more space to compress a file (won't occur if force compression
            // is true)
            myViewer.showError("Compressed file has " + (bitDifference * -1) + " more bits" +
                    " than uncompressed file. Select \"force compression\" option to compress.");
        }
        return totalBitsWritten;
    }

    // Writes the compressed header based on the compression format
    private void writeHeader(BitOutputStream outStream) {
        // Store counts format
        if(compressionFormat == STORE_COUNTS){
            // write the bit frequency of every character
            for(int currentChar = 0; currentChar < ALPH_SIZE; currentChar++){
                outStream.writeBits(BITS_PER_INT,bitInventory[currentChar]);
            }
        // Store tree format
        } else if(compressionFormat == STORE_TREE){
            // Write the size of the huff tree
            outStream.writeBits(BITS_PER_INT,bitQueue.getTreeBitSize(huffTree));
            // Write out the tree itself
            writeTree(outStream,huffTree);
        }
    }

    // Writes the actual data to the compression file
    private void writeData(BitInputStream inStream, BitOutputStream outStream) throws IOException {
        int currentBit = inStream.readBits(BITS_PER_WORD);
        // Parse until reaching the end of the file
        while (currentBit != -1){
            // Get the respective code from encoding
            String currentCode = encodingTable.get(currentBit);
            // Send the code to writeBitsFromString to write the individual 1's or 0's
            writeBitsFromString(outStream, currentCode);
            currentBit = inStream.readBits(BITS_PER_WORD);
        }
    }

    // Write the individual bits based on a given string
    private void writeBitsFromString(BitOutputStream outStream, String currentCode) {
        // While there is still bits to write
        while(currentCode.length() > 0){
            // Write the first character (either 1 or 0)
            outStream.writeBits(1,Character.getNumericValue(currentCode.charAt(0)));
            // Take out the first character and repeat
            currentCode = currentCode.substring(1);
        }
    }

    // Recursively writes the huff tree itself
    private void writeTree(BitOutputStream outStream, TreeNode currentNode){
        // If the node is internal
        if(!currentNode.isLeaf()){
            // Write a singular 0
            outStream.writeBits(1,INTERNAL_NODE);
            // Recurse down the tree through a preorder traversal
            writeTree(outStream, currentNode.getLeft());
            writeTree(outStream, currentNode.getRight());
        } else { // If node is a leaf
            // Write a singular 1 to show there is a leaf
            outStream.writeBits(1,LEAF);
            // Write out the code for the current node
            outStream.writeBits(BITS_PER_WORD + 1, currentNode.getValue());
        }
    }

    /**
     * Uncompress a previously compressed stream in, writing the
     * uncompressed bits/data to out.
     * @param in is the previously compressed data (not a BitInputStream)
     * @param out is the uncompressed file/stream
     * @return the number of bits written to the uncompressed file/stream
     * @throws IOException if an error occurs while reading from the input file or
     * writing to the output file.
     */
    public int uncompress(InputStream in, OutputStream out) throws IOException {
        // Initialize the bit input and output streams
        BitInputStream inStream = new BitInputStream(in);
        BitOutputStream outStream = new BitOutputStream(out);
        int potentialMagicNumber = inStream.readBits(BITS_PER_INT);
        // Check to see if the first number is the magic number
        if(potentialMagicNumber != MAGIC_NUMBER){
            // If it isn't return an error and stop the uncompressing
            myViewer.showError("Error! File did not start with huff magic number");
            return -1;
        }
        // Initialize a new PriorityQueue and tree node
        createUncompressDataStructures();
        // Read the code format
        compressionFormat = inStream.readBits(BITS_PER_INT);
        // Read the header
        readerHeader(inStream);
        // Create decoding and encoding tables
        HashMap<String,Integer> decodingTable = bitQueue.makeDecodingTable();
        encodingTable = bitQueue.makeEncodingTable();
        // Read data from file and write it to new file
        readAndWriteData(inStream, outStream, decodingTable, false);
        inStream.close();
        outStream.close();
        return totalBitsWritten;
    }

    // Creates a new PriorityQueue, huffman tree, and initializes totalBitsWritten
    private void createUncompressDataStructures() {
        bitQueue = new PriorityQueue();
        huffTree = new TreeNode(-1,0);
        totalBitsWritten = 0;
    }

    // Reads the file and writes out the uncompressed version
    private void readAndWriteData(BitInputStream inStream, BitOutputStream outStream, HashMap<String,
            Integer> decodingTable, boolean doneReading) throws IOException {
        int currentBit = inStream.readBits(1);
        TreeNode currentTreeNode = huffTree;
        String currentCode = "";
        while(!doneReading) {
            // Hitting end of the final means there was no PSEUDO_EOF value
            if (currentBit == -1) {
                throw new IOException("Error! No PSEUDO_EOF value detected");
            } else {
                // If currentBit goes to left then move left down the tree and add 0 to currentCode
                if (currentBit == LEFT) {
                    if (currentTreeNode.getLeft() != null) {
                        currentTreeNode = currentTreeNode.getLeft();
                        currentCode += "0";
                    }
                //If currentBit goes to right then move right down the tree and add 1 to currentCode
                } else if (currentBit == RIGHT) {
                    if (currentTreeNode.getRight() != null) {
                        currentTreeNode = currentTreeNode.getRight();
                        currentCode += "1";
                    }
                }
                // If the currentCode is a know code then add write out its corresponding character
                // If the code happens to be PSEUDO_EOF then stop the method
                if (decodingTable.containsKey(currentCode)) {
                    if(currentCode.equals(encodingTable.get(PSEUDO_EOF))){
                        doneReading = true;
                    }else {
                        outStream.writeBits(BITS_PER_WORD, decodingTable.get(currentCode));
                        totalBitsWritten += BITS_PER_WORD;
                        currentCode = "";
                        currentTreeNode = huffTree;
                    }
                }
                currentBit = inStream.readBits(1);
            }
        }
    }

    // Reads the header of the compressed file and creates needed data structures
    private void readerHeader(BitInputStream inStream) throws IOException {
        if(compressionFormat == STORE_COUNTS){
            // Fills in the bit inventory based on values from compressed file
            bitInventory = new int[ALPH_SIZE];
            for(int currentChar = 0; currentChar < ALPH_SIZE; currentChar++){
                int currentFrequency = inStream.readBits(BITS_PER_INT);
                bitInventory[currentChar] = currentFrequency;
            }
            // Creates the bitQueue and huffman tree from the bit inventory
            bitQueue = new PriorityQueue(bitInventory);
            huffTree = bitQueue.makeTree();
        } else if(compressionFormat == STORE_TREE){
            // Get size number out of the way
            inStream.readBits(BITS_PER_INT);
            // Initialize huffman tree
            huffTree = new TreeNode(-1,0);
            // Creates huff tree using preorder traversal of given bits and priority Queue from
            // the huffman tree
            huffTree = preOrderTraversal(inStream,huffTree);
            bitQueue = new PriorityQueue(huffTree);
        }
    }

    // Recursive method that runs a preorder traversal compressed bit representation of the tree
    private TreeNode preOrderTraversal(BitInputStream inStream, TreeNode currentNode)
            throws IOException{
        int fillerFreq = 0;
        int sizeOfCharValues = BITS_PER_WORD + 1;
        int currentBit = inStream.readBits(1);
        // If node is internal
        if (currentBit == INTERNAL_NODE) {
            // Create node that with unneeded value and recurse
            currentNode = new TreeNode(-1, fillerFreq);
            currentNode.setLeft(preOrderTraversal(inStream, currentNode.getLeft()));
            currentNode.setRight(preOrderTraversal(inStream, currentNode.getRight()));
        } else {
            // If node is a leaf node then create a leaf node on the tree
            currentNode = new TreeNode(inStream.readBits(sizeOfCharValues), fillerFreq);
        }
        return currentNode;
    }

    public void setViewer(IHuffViewer viewer) {
        myViewer = viewer;
    }



    private void showString(String s){
        if(myViewer != null)
            myViewer.update(s);
    }
}