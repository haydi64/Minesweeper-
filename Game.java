/*----------------------------------------------------------------
 *  Author:   Hayden Ivatts
 *  Email:    hpivat21@g.holycross.edu
 *  Written:  5-2-18
 *  
 *  Minesweeper game. This class implements the game window and most
 *  of the game logic.
 *----------------------------------------------------------------*/

import GUI.*;

/**
 * A <i>Game</i> object manages all information about a minesweeper game as it
 * is being played and displayed on the screen. This includes information about
 * all of the cells (this is stored in a 2-D array of Cell objects), how many
 * flags have been planted, how many mines have been deployed, etc. Game extends
 * Window, so it can be drawn on the screen. It also extends EventListener so it
 * can respond to user interaction.
 */
public class Game extends Window implements EventListener {

    /**
     * Number of cells tall the game board will be.
     */
    public static final int NUM_ROWS = 20;

    /**
     * Number of cells wide the game board will be.
     */
    public static final int NUM_COLS = 30;

    // Example game screen layout:
    // +---------------------------------------------------------+
    // |      M A R G I N = 50                                   |
    // | M  + - - - - - - - - - - - - - - - - - - - - - - - + M  |
    // | A  |                                               | A  |
    // | R  |                                               | R  |
    // | G  |                Grid of Cells                  | G  |
    // | I  |                                               | I  |
    // | N  |                                               | N  |
    // | =  |       600 = NUM_COLS * Cell.SIZE wide         | =  |
    // | 50 |                      by                       | 50 |
    // |    |       400 = NUM_ROWS * Cell.SIZE tall         |    |
    // |    |                                               |    |
    // |    |                                               |    |
    // |    |                                               |    |
    // |    + - - - - - - - - - - - - - - - - - - - - - - - +    |
    // |            SPACE     S   SPACE   S    SPACE             |
    // |    + - - - - - - - + P + - - - + P + - - - - - - - +    |
    // |    |    Status     | A | Timer | A |     Help      |    |
    // |    |       Box     | C |       | C |      Box      |    |
    // |    + - - - - - - - + E + - - - + E + - - - - - - - +    |
    // |     M A R G I N = 50                                    |
    // +-- ------------------------------------------------------+

    /**
     * Width of the game window, in pixels.
     * Equal to 2*MARGIN + GRID_WIDTH
     * or 2*MARGIN + 2*SPACE + StatusBox.WIDTH, Timer.WIDTH, HelpBox.WIDTH,
     * whichever is larger.
     */
    public static final int WIDTH = 700;

    /**
     * Height of the game window, in pixels.
     * Equal to 2*MARGIN + SPACE
     *     + GRID_HEIGHT
     *     + max(StatusBox.Height, Timer.HEIGHT, HelpBox.HEIGHT)
     */
    public static final int HEIGHT = 600; 

    /**
     * Width of the grid part of the window, in pixels.
     * Equal to NUM_COLS * Cell.SIZE.
     */
    public static final int GRID_WIDTH = NUM_COLS * Cell.SIZE;

    /**
     * Height of the grid part of the window, in pixels.
     * Equal to NUM_ROWS * Cell.SIZE.
     */
    public static final int GRID_HEIGHT = NUM_ROWS * Cell.SIZE;

    /**
     * Margin around the edges of the canvas.
     */
    private static final int MARGIN = 50;

    /**
     * Space between elements on the canvas.
     */
    private static final int SPACE = 25;

    // A 2-D array of Cell objects to keep track of the board state.
    private Cell[][] cells = new Cell[NUM_ROWS][NUM_COLS];

    private int numMines = 0;    // number of mines deployed
    private int numRevealed = 0; // number of cells revealed so far

    // Whether or not the game has been won.
    private boolean gameWon = false;

    // Whether or not the game has been lost
    private boolean gameLost = false;

    // Name of the user playing the game.
    private String username;

    // The difficulty level of the game, used for tracking top scores.
    private String difficulty;

    // The status box that appears in the top left.
    private StatusBox status;

    // The timer that appears in the top middle.
    private Timer timer;

    // The help box that appears in the top right.
    private HelpBox help;

    //Losing message that appears in the center of the screen
    private YouLose lose;

    //Winning message that appears in the center of the screen
    private YouWin win;

    /**
     * Constructor: Initializes a new game, but does not deploy any mines, plant
     * any flags, etc. The difficulty is either "easy", "medium", or "hard", and
     * will be used to load the proper top scores file. Name is used as the
     * user's name.
     */
    public Game(String name, String difficulty) {
        super("Minesweeper!", WIDTH, HEIGHT);

        this.username = name;
        this.difficulty = difficulty;

        // Create the background
        setBackgroundColor(Canvas.DARK_GRAY);

        // Create a border around the grid
        Box border = new Box(MARGIN-1.5, MARGIN-1.5, GRID_WIDTH+3, GRID_HEIGHT+3);
        border.setBackgroundColor(null);
        border.setBorderColor(Canvas.BLACK);
        add(border);

        // Create the info boxes
        help = new HelpBox(
                WIDTH - MARGIN - HelpBox.WIDTH,
                HEIGHT - MARGIN - HelpBox.HEIGHT);
        add(help);

        //Create Timer box
        timer = new Timer(WIDTH - MARGIN - Timer.WIDTH - HelpBox.WIDTH - SPACE ,
                          HEIGHT - Timer.HEIGHT - MARGIN);
        add(timer);

        //Create status box
        status = new StatusBox(this, WIDTH - MARGIN - Timer.WIDTH - HelpBox.WIDTH - StatusBox.WIDTH - SPACE - SPACE,
                               HEIGHT - Timer.HEIGHT - MARGIN);
        //Add status box
        add(status);

        //Create a losing message
        lose = new YouLose(200, 100);

        //Create a winning message
        win = new YouWin(200, 100);
        
        
        //Initialize row and col variables
        int row, col;

        //Creates 600 cells and adds them to the screen
        for(int i = 0; i < NUM_ROWS; i++) {
            for(int j = 0; j < NUM_COLS; j++) {
                cells[i][j] = new Cell(MARGIN + Cell.SIZE * j, MARGIN + Cell.SIZE * i);
                add(cells[i][j]);
            }
        }           
    }

    /**
     * Get the number of mines that are deployed.
     */
    public int getNumMinesDeployed() {
        return numMines;
    }

    /**
     * Get the number of hidden cells remaining to be revealed.
     */
    public int getNumCellsRemaining() {
        return NUM_ROWS * NUM_COLS - numRevealed;
    }

    /**
     * Deploy the given number of mines. This gets called once during game
     * setup. The game doesn't actually begin officially until the user clicks
     * a cell, so the timer should not start yet.
     */
    public void deployMines(int mines) {

        //Use while loop to keep track of how many have been planted, use num mine variable
        while(numMines < mines) {
            //Generates random row to put mine
            int randrow = StdRandom.uniform(NUM_ROWS - 1);
            
            //Generates random column to put mine
            int randcol = StdRandom.uniform(NUM_COLS - 1);

            //Plants mine at random location
            cells[randrow][randcol].plantMine();
            
            for(int i = -1; i < 2; i++) {
                for(int j = -1; j < 2; j++) {
                    if((randrow + i) >= 0 && (randrow + i) < NUM_ROWS && (randcol + j) >= 0 && (randcol + j) < NUM_COLS) {
                        cells[randrow + i][randcol + j].incrementNeighborMineCount();
                            
                    }
                }
            }
            //Iterates loop when all neighbors have been incremented and mine is placed
            numMines++;
        }
    }

    

    

         

    /**
     * Respond to a mouse click. This function will be called each time the user
     * clicks on the game window. The x, y parameters indicate the screen
     * coordinates where the user has clicked, and the button parameter
     * indicates which mouse button was clicked (either "left", "middle", or
     * "right"). The function should update the game state according to what the
     * user has clicked.
     * @param x the x coordinate where the user clicked, in pixels.
     * @param y the y coordinate where the user clicked, in pixels.
     * @param button either "left", "middle", or "right".
     */
    public void mouseClicked(double x, double y, String button) {
        // User clicked the mouse, see what they want to do.
        
        // If game is over, then ignore the mouse click.
        if (gameWon || gameLost)
            return;

        // If the user middle-clicked, ignore it.
        if (!button.equals("left") && !button.equals("right"))
            return;

        // If the user clicked outside of the game grid, ignore it.
        if (x < MARGIN || y < MARGIN
                || x >= MARGIN + GRID_WIDTH || y >= MARGIN + GRID_HEIGHT) {
            return;
                }

        // Calculate which cell the user clicked.
        int row = (int)((y - MARGIN) / Cell.SIZE);
        int col = (int)((x - MARGIN) / Cell.SIZE);

        
        StdOut.printf("You clicked row %d column %d with button %s\n", row, col, button);
        //Starts timer when first cell is clicked
        timer.startCounting();
        
        //Returns if cell is already revealed
        if(cells[row][col].isRevealed()) {
            return;
        }
        //Reveal cell when it is clicked
        cells[row][col].reveal();

        //Increment revealed variable
        numRevealed++;

       

        //If user clicks on a mine then they lose the game
        if(cells[row][col].isMine()) {
            gameLost = true;
        } else if((getNumCellsRemaining() - getNumMinesDeployed()) == 0 ) {
            gameWon = true;
        }

        //If the game is lost all mines are shown
        if(gameWon) {
            timer.stopCounting();

            //Displays message when the user wins
            add(win);
            System.out.println("Congratulations!! You survived the mine field");
        } else if(gameLost) {
            timer.stopCounting();
            //Displays message when the user loses
            add(lose);
            for(int i = 0; i < NUM_ROWS; i++) {
                for(int j = 0; j < NUM_COLS; j++) {
                    cells[i][j].showMine();
                }
                
            }
        }
            
            
    }

    /**
     * Respond to key presses. This function will be called each time the user
     * presses a key. The parameter indicates the character the user pressed.
     * The function should update the game state according to what character the
     * user has pressed. 
     * @param c the character that was typed.
     */
    public void keyTyped(char c)
    {
        // User pressed a key, see what they want to do.
        switch (c) {
            case 'q': 
            case 'Q': 
                hide(); // user wants to quit
                break;
            default:
                break; // anything else is ignored
        }
    }

    /**
     * Paint the background for this window on the canvas. Don't call this
     * directly, it is called by the GUI system automatically. This function
     * should draw something on the canvas, if you like. Or the background can
     * be blank.
     * @param canvas the canvas on which to draw.
     */
    public void repaintWindowBackground(GUI.Canvas canvas) {

    }
}
