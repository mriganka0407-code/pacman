import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

class PacMan extends JPanel{
    private int rowCount = 19;
    private int colCount = 21;
    private int tileSize = 32;
    private int boardWidth = colCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;

    private Image pacmanDownImage;
    private Image pacmanUpImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

     //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };


    //block class
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
       
            Block(int x, int y, int width, int height, Image image, int startX, int startY){
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.image = image;
                this.startX = startX;
                this.startY = startY;
            }
    }

    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);

        //load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();

        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        loadMap();
        
    }

    public void loadMap(){
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for(int r=0;r<rowCount;r++){
            for(int c=0; c<tileMap[r].length() ; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);
                int x = c * tileSize;
                int y = r* tileSize;

                if(tileMapChar=='X'){  //block wall
                    Block wall = new Block(x, y, tileSize, tileSize, wallImage, x, y);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b'){
                    Block blueGhost = new Block (x,y, tileSize, tileSize , blueGhostImage, x, y );
                    ghosts.add(blueGhost);
                }
                else if(tileMapChar == 'o'){
                    Block orangeGhost = new Block (x,y, tileSize, tileSize , orangeGhostImage, x, y );
                    ghosts.add(orangeGhost);
                }
                else if(tileMapChar == 'p'){
                    Block pinkGhost = new Block (x,y, tileSize, tileSize , pinkGhostImage, x, y );
                    ghosts.add(pinkGhost);
                }
                else if(tileMapChar == 'r'){
                    Block redGhost = new Block (x,y, tileSize, tileSize , redGhostImage, x, y );
                    ghosts.add(redGhost);
                }
                else if(tileMapChar == 'P'){
                    pacman = new Block(x,y, tileSize, tileSize , pacmanRightImage, x, y );
                }
                else if(tileMapChar == ' '){ //food
                    Block food = new Block(x+14, y+14, 4,4, null, 0, 0);
                    foods.add(food);
                }

            }
        }
    }


}