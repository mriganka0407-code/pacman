import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

class PacMan extends JPanel implements ActionListener, KeyListener{
    private int rowCount = 21;
    private int colCount = 19;
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

    javax.swing.Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

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
        char direction ='U';
        int velocityX = 0;
        int velocityY = 0;
       
            Block(int x, int y, int width, int height, Image image, int startX, int startY){
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.image = image;
                this.startX = startX;
                this.startY = startY;
            }

            void updateDirection(char direction){
                char prevDirection = this.direction;
                this.direction = direction;
                updateVelocity();
                this.x += this.velocityX;
                this.y += this.velocityY;

                for(Block wall : walls){
                    if(collision(this, wall)){
                        this.x -= this.velocityX;
                        this.y -= this.velocityY;
                        this.direction = prevDirection;
                        updateVelocity();
                        break;
                    }
                }
            }

            void updateVelocity(){
                if(direction == 'U'){
                    velocityX = 0;
                    velocityY = -tileSize/4;
                }
                else if(direction == 'D'){
                    velocityX = 0;
                    velocityY = tileSize/4;
                }
                else if(direction == 'L'){
                    velocityX = -tileSize/4;
                    velocityY = 0;
                }
                else if(direction == 'R'){
                    velocityX = tileSize/4;
                    velocityY = 0;
                }
            }

            void reset(){
                x = startX;
                y = startY;
                direction = 'U';
                updateVelocity();
            }
    }

   

    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
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
        for(Block ghost     : ghosts){
           char newDirection = directions[random.nextInt(4)];
           ghost.updateDirection(newDirection);

        }
        gameLoop = new javax.swing.Timer(35,this);
        gameLoop.start();
        
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

    public void paintComponent(Graphics g){
            super.paintComponent(g);
            draw(g);
        }

    public void draw(Graphics g){
        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, this);
        }
        for(Block food : foods){
            g.drawImage(food.image, food.x, food.y, food.width, food.height, this);
        }
        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, this);
        }
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, this);

        g.setColor(Color.white);
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        if(pacman.x<0){
            pacman.x = boardWidth;
        }
        if(pacman.x>boardWidth){
            pacman.x = 0;
        }
        for(Block wall: walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        for(Block ghost: ghosts){

            if(ghost.y==tileSize*9 && ghost.direction !='U' && ghost.direction !='D'){
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
             for(Block wall: walls){
            if(collision(ghost, wall)){
                ghost.x -= ghost.velocityX;
                ghost.y -= ghost.velocityY;
                char newDirection = directions[random.nextInt(4)];
                ghost.updateDirection(newDirection);
            }
            if(ghost.x < 0 ){
                ghost.x = boardWidth;
            }
            if(ghost.x>boardWidth){
                ghost.x = 0;
            }
            
        }
            if(collision(pacman,ghost)){
                lives--;
                if(lives == 0){
                    gameOver = true;
                    return;
                }
                else{
                    reset();
                }
            }
        }

         Block foodEaten=null;
        for(Block food: foods){
            if(collision(pacman, food)){
                foodEaten = food;
                score += 10;
            }
        }
        if(foodEaten != null){
            foods.remove(foodEaten);
        }
        if(foods.isEmpty()){
            loadMap();
            reset();
        }
    }

       

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    public void reset(){
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for(Block ghost: ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

  
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();

        }
    }

   
    public void keyPressed(KeyEvent e){
        if(gameOver){
            loadMap();
            reset();
            lives=3;
            score=0;
            gameOver = false;
            gameLoop.start();
        }

        if(e.getKeyCode()==KeyEvent.VK_UP){
            pacman.updateDirection('U');
            pacman.image = pacmanUpImage;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
            pacman.image = pacmanDownImage;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
            pacman.image = pacmanLeftImage;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
            pacman.image = pacmanRightImage;
        }
    }

    public void keyReleased(KeyEvent e){
        
    }

    public void keyTyped(KeyEvent e){
        
    }

}