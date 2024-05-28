package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

import static java.lang.Math.pow;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth; 
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(2);
 
 
 /*
  * These two constructors are here for testing purposes. 
  */
 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }

 public static void main(String[] args) {
  Block blockDepth2 = new Block(0,2);
  blockDepth2.updateSizeAndPosition(16, 0, 0);
//  blockDepth2.printBlock();
//  blockDepth2.reflect(1);
//  blockDepth2.printBlock();
  blockDepth2.printColoredBlock();
 }

 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) { //check if this is correct!!
  this.level=lvl;
  this.maxDepth = maxDepth;

  if(lvl < maxDepth){
   if(gen.nextDouble(1) < Math.exp(-0.25 * lvl)){ //check the bounds for the gen random no.
    this.children = new Block[4];
    for(int i = 0; i < 4; i++) {
     Block child = new Block(lvl + 1, maxDepth);
     this.children[i] = child;
    }
   } else {
    this.color=(GameColors.BLOCK_COLORS[gen.nextInt(4)]);
    Block[] childs = {};
    this.children = childs;
   }
  } else {
   this.color=(GameColors.BLOCK_COLORS[gen.nextInt(4)]);
   Block[] childs = {};
   this.children = childs;
  }
 }



 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  if(size <= 0 || (size%2 != 0 && this.level > maxDepth)){
   throw new IllegalArgumentException("wrong size");
  }
  //for the current block
  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;
  //github canadian tech internships
  if(this.children.length == 0){
   return;
  }
  //for the children of the block
  for(int i = 0; i < this.children.length; i++){
   if(i == 0){
    children[i].updateSizeAndPosition(this.size/2,xCoord+this.size/2, yCoord);
   }
   if(i == 1){
    children[i].updateSizeAndPosition(this.size/2,xCoord, yCoord);
   }
   if(i == 2){
    children[i].updateSizeAndPosition(this.size/2,xCoord, this.size/2+yCoord);
   }
   if(i == 3){
    children[i].updateSizeAndPosition(this.size/2,xCoord+this.size/2, this.size/2+yCoord);
   }
  }
 }

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() { //comeback and check
  ArrayList<BlockToDraw> blocksDraw = new ArrayList<BlockToDraw>();
  if(this.children.length == 0){
   blocksDraw.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
   blocksDraw.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord,this.yCoord,this.size,3));
   return blocksDraw;
  }
  for(int i = 0; i < 4; i++){
   blocksDraw.addAll(this.children[i].getBlocksToDraw());
  }
  return blocksDraw;
 }

 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) { //fix and debug!!! Wrong blocks and positions
  //input validation
  if(lvl < this.level){
   throw new IllegalArgumentException("Provided level larger than Block level!");
  }
  if(lvl > maxDepth){
   throw new IllegalArgumentException("Provided level larger than maxDepth!");
  }

  if(x <= (xCoord + size) && x >= xCoord && y <= (yCoord + size) && y >= yCoord){
   if(this.color == null && lvl > this.level) {
    if (x < (xCoord + size)/2 && y < (yCoord + size)/2) return children[1].getSelectedBlock(x, y, lvl);
    if (x < (xCoord + size)/2 && y >= (yCoord + size)/2) return children[2].getSelectedBlock(x, y, lvl);
    if (x >= (xCoord + size)/2 && y < (yCoord + size)/2) return children[0].getSelectedBlock(x, y, lvl);
    if (x >= (xCoord + size)/2 && y >= (yCoord + size)/2) return children[3].getSelectedBlock(x, y, lvl);
   }
  }
  if(x <= (xCoord + size) && x >= xCoord && y <= (yCoord + size) && y >= yCoord && lvl >= this.level){
   return this;
  }

  //base case
//  if(lvl == this.level || children.length == 0){
//   return this;
//  }
//
//if(this.color == null) {
// if (x <= (xCoord + size) / 2) { //check x coordinates
//  if (y <= (yCoord + size) / 2) { //check y coordinate
//   return children[1].getSelectedBlock(x, y, lvl);
//  } else if (y >= (yCoord + size) / 2) {
//   return children[2].getSelectedBlock(x, y, lvl);
//  }
// }
//
// if (x >= (xCoord + size) / 2 && x <= xCoord + size) {
//  if (y <= (yCoord + size) / 2) {
//   return children[0].getSelectedBlock(x, y, lvl);
//  } else if (y >= (yCoord + size) / 2) {
//   return children[3].getSelectedBlock(x, y, lvl);
//  }
// }
//}
  return null;
 }

 
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {
  if(direction > 1 || direction < 0){
   throw new IllegalArgumentException("Direction not valid");
  }

  if(children.length == 0){
   return;
  }

  if(direction == 0){ //reflection around x-axis
   Block tempBlock= children[0];
   children[0] = children[3];
   children[3] = tempBlock;

   tempBlock = children[1];
   children[1] = children[2];
   children[2] = tempBlock;

   for(int i = 0; i < 4; i++){
    children[i].reflect(0);
   }
   return;
  }

  if(direction == 1){ //reflection around y-axis
   Block tempBlock= children[0];
   children[0] = children[1];
   children[1] = tempBlock;

   tempBlock = children[2];
   children[2] = children[3];
   children[3] = tempBlock;

   for(int i = 0; i < 4; i++){
    children[i].reflect(1);
   }
   return;
  }
 }
 

 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  if(direction > 1 || direction < 0){
   throw new IllegalArgumentException("Direction not valid");
  }

  if(children.length == 0){
   return;
  }

  if(direction == 0){ //rotate counter-clockwise
   Block tempBlock1 = children[0];
   Block tempBlock2 = children[1];
   children[0] = children[3];
   children[1] = tempBlock1;
   tempBlock1 = children[2];
   children[2] = tempBlock2;
   children[3] = tempBlock1;

   for(int i = 0; i < 4; i++){
    children[i].rotate(direction);
   }
   return;
  }

  if(direction == 1){ //rotate clockwise
   Block tempBlock1 = children[3];
   children[3] = children[0];
   children[0] = children[1];
   children[1] = children[2];
   children[2] = tempBlock1;

   for(int i = 0; i < 4; i++){
    children[i].rotate(direction);
   }
   return;
  }
 }
 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  if(this.level == maxDepth){
   return false;
  }
  if(this.level != 0 || this.level < this.maxDepth){
   this.color = null;
   this.children = new Block[4];
   for(int i = 0; i < 4; i++) {
    Block child = new Block(this.level + 1, maxDepth);
    this.children[i] = child;
   }
   this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);
   return true;
  }
  return false;
 }
 
 
 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  * 
  * Return and array arr where, arr[i] represents the unit cells in row i, 
  * arr[i][j] is the color of unit cell in row i and column j.
  * 
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  int size = ((int)pow(2.0,((double)(maxDepth-level))));
  Color[][] board = new Color[size][size];

  //base case
  if(children.length == 0){
   for(int i = 0; i<size;i++){ //check conditions
    for(int j = 0; j<size;j++){
     board[i][j] = this.color;
    }
   }
   return board;
  }

  Color[][][] childs = new Color[4][][]; //comeback
  for(int i = 0; i <4 ; i++){
   childs[i] = children[i].flatten();
  }

  for(int i = 0; i < size; i++) {
   for (int j = 0; j < size; j++) {
    if(i < size/2 && j < size/2) board[i][j] = childs[1][i][j];
    if(i < size/2 && j >= size/2) board[i][j] = childs[0][i][j-(size/2)];
    if(i >= size/2 && j < size/2) board[i][j] = childs[2][i-(size/2)][j];
    if(i >= size/2 && j >= size/2) board[i][j] = childs[3][i-(size/2)][j-(size/2)];
   }
  }

//  for(int i = 0; i < size/2; i++) {
//   //for top left block
//   for(int j = 0;j <size/2; j++){
//    Color[][] child1 = children[1].flatten();
//    board[i][j] = child1[i][j];
//
//    Color[][] child2 = children[2].flatten();
//    board[i][(board.length/2)+j] = child2[i][j];
//   }
//  }
//
//  for(int i = size/2; i < size; i++) {
//   for(int j = 0;j <size/2; j++){
//    Color[][] child0 = children[0].flatten();
//    board[i][j] = child0[i-(size/2)][j];
//
//    Color[][] child3 = children[3].flatten();
//    board[i][(size/2)+j] = child3[i-(size/2)][j];
//   }
//  }
  return board;
 }

 
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
