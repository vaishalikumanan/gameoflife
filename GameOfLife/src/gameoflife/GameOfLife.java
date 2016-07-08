
package gameoflife;

import java.io.*;
//import java.util.Scanner;
import java.awt.*; //needed for graphics
import javax.swing.*; //needed for graphics
import static javax.swing.JFrame.EXIT_ON_CLOSE; //needed for graphics

public class GameOfLife extends JFrame {

    //FIELDS
    int numGenerations = 100;
    int currGeneration = 1;
    
    Color aliveColor = Color.cyan;
    Color deadColor = Color.black;
    
    //String fileName = "Initial cells.txt";

    int width = 800; //width of the window in pixels
    int height = 800;
    int borderWidth = 50;

    int numCellsX = 30; //width of the grid (in cells)
    int numCellsY = 30;
    
    boolean alive[][] = new boolean[numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION 
    boolean aliveNext[][] = new boolean[numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION
    
    
    int cellWidth = (width - 2*borderWidth)/numCellsX; //replace with the correct formula that uses width, borderWidth and numCellsX
    
    int labelX = width / 2;
    int labelY = borderWidth;
 
    
    //METHODS
    public void plantFirstGeneration() throws IOException {
        makeEveryoneDead();
        //small explosion thing
        alive[14][15] = true;
        alive[15][14] = true;
        alive[15][15] = true;
        alive[15][16] = true;
        alive[16][14] = true;
        alive[16][16] = true;
        alive[17][15] = true;
        
        //plantFromFile( fileName );
        
        //OR plant the first generation systematically using a pattern, using
        //one of these methods, for example:
        
        plantBlock ( 20, 20, 10, 10 );
        plantGlider(1, 2, 4);
        //plantGlider(26, 2, 4);
        //plantGlider(26, 26, 2);
        //plantGlider(1, 26, 3);
    }

    
    //Sets all cells to dead
    public void makeEveryoneDead() {
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                alive[i][j] = false;
            }
        }
    }
    
    //reads the first generations' alive cells from a file
    /*public void plantFromFile(String fileName) throws IOException {

        FileReader f = new FileReader(fileName);
        Scanner s = new Scanner(f);

        int x, y;

        while ( s.hasNext() ) {
            //fill this in
        }
    }*/
    
    //Plants a solid rectangle of alive cells.  Would be used in place of plantFromFile()
    public void plantBlock(int startX, int startY, int numColumns, int numRows) {
        
        int endCol = Math.min(startX + numColumns, numCellsX);
        int endRow = Math.min(startY + numRows, numCellsY);

        for (int i = startX; i < endCol; i++) {
            for (int j = startY; j < endRow; j++) {
                alive[i][j] = true;
            }
        }
    }
    
    //Plants a "glider" group, which is a cluster of living cells that migrates across the grid from 1 generation to the next
    public void plantGlider(int startX, int startY, int direction) { //direction can be "SW", "NW", "SE", or "NE"
        //NW
        if(direction == 1){
            alive[startX][startY] = true;
            alive[startX+1][startY] = true;
            alive[startX+2][startY] = true;
            alive[startX][startY+1] = true;
            alive[startX+1][startY+2] = true;
        }
        //NE
        else if(direction == 2){
            alive[startX][startY] = true;
            alive[startX][startY+1] = true;
            alive[startX][startY+2] = true;
            alive[startX+1][startY+2] = true;
            alive[startX+2][startY+1] = true;
        }
        //SW
        else if(direction == 3){
            alive[startX][startY] = true;
            alive[startX+1][startY] = true;
            alive[startX+2][startY] = true;
            alive[startX+2][startY+1] = true;
            alive[startX+1][startY+2] = true;
        }
        //SE
        else if(direction == 4){
            alive[startX+2][startY] = true;
            alive[startX+2][startY+1] = true;
            alive[startX+2][startY+2] = true;
            alive[startX+1][startY+2] = true;
            alive[startX][startY+1] = true;
        }
    }

    //Applies the rules of The Game of Life to set the true-false values of the aliveNext[][] array,
    //based on the current values in the alive[][] array
    public void computeNextGeneration() {
        for(int i = 0; i < aliveNext.length; i ++){
            for(int j = 0; j < aliveNext[0].length; j ++){
                //get number of living cells
                int living = countLivingNeighbors(i,j);
                //Each living cell with 4 or more neighbours dies
                //Each living cell with one or no neighbours dies
                if(living <= 1 || living >= 4){
                    aliveNext[i][j] = false;
                }
                else if(living == 2){
                    //Each living cell with 2 neighbours survives 
                    if(alive[i][j]){
                        aliveNext[i][j] = true;
                    }
                    else{
                        aliveNext[i][j] = false;
                    }
                }
                //Each living cell with 3 neighbours survives 
                //Each dead cell with 3 neighbours becomes alive.
                else if(living == 3){
                    aliveNext[i][j] = true;
                }
            }
        }
    }
   
    //Overwrites the current generation's 2-D array with the values from the next generation's 2-D array
    public void plantNextGeneration() {
        for(int i = 0; i < alive.length; i ++){
            for(int j = 0; j < alive[0].length; j ++){
                alive[i][j] = aliveNext[i][j];
            }
        }
        currGeneration ++;
    }
    
    //Counts the number of living cells adjacent to cell (i, j)
    public int countLivingNeighbors(int i, int j) {
        int living = 0;
        int startrow = i - 1;
        int endrow = i + 1;
        int startcol = j - 1;
        int endcol = j + 1;
        //if top row
        if(i == 0){
            startrow = i;
        }
        //if bottom row
        if(i == numCellsX - 1){
            endrow = i;
        }
        //if leftmost column
        if(j == 0){
            startcol = j;
        }
        //if rightmost column
        if(j == numCellsY - 1){
            endcol = j;
        }
        //counts living cells surrounding middle cell, including middle cell
        for(int m = startrow; m <= endrow; m ++){
            for(int n = startcol; n <= endcol; n++){
                if(alive[m][n]){
                        living ++;
                }
            }
        }
        //removes middle cell from count
        if(alive[i][j]){
            living --;
        }
        return living; //make it return the right thing
    }
 
    //Makes the pause between generations
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }
    
    //Displays the statistics at the top of the screen
    void drawLabel(Graphics g, int state) {
        g.setColor(deadColor);
        g.fillRect(0, 0, width, borderWidth);
        g.setColor(Color.yellow);
        g.drawString("Generation: " + state, labelX, labelY);
    }
    
    //Draws the current generation of living cells on the screen
    public void paint( Graphics g ) {
        int x, y, i, j;
        
        //start with first row
        y = borderWidth;
        
        //set background to black for first generation
        if(currGeneration == 1 ){
            g.setColor(deadColor); 
            g.fillRect(0, 0, width, height);
         
        }
        
        drawLabel(g, currGeneration);
        
        for (i = 0; i < numCellsX; i++) {
            //start at beginning of row
            x = borderWidth;
            for (j = 0; j < numCellsY; j++) {
                if(alive[i][j]){
                    g.setColor(aliveColor);
                }
                else{
                    g.setColor(deadColor);
                }
                //fill cell with colour
                g.fillRect(x, y, cellWidth, cellWidth);
                //white gridlines
                g.setColor(Color.white);
                g.drawRect(x , y,  cellWidth, cellWidth);
                //next cell in row
                x += cellWidth;
            }
            //next row
            y += cellWidth;
        }
    }

    //Sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Game of Life Simulator");
        setSize(height, width);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);//calls paint() for the first time
    }
    
    //Main algorithm
    public static void main(String args[]) throws IOException {

        GameOfLife currGame = new GameOfLife();

        currGame.initializeWindow();
        //Sets the initial generation of living cells, either by reading from a 
        //file or creating them algorithmically
        currGame.plantFirstGeneration(); 
        for (int i = 0; i < currGame.numGenerations; i++) {
            currGame.repaint();
            currGame.sleep(150);
            currGame.computeNextGeneration();
            currGame.plantNextGeneration();
        }
    } 
} //end of class
