/*----------------------------------------------------------------
 *  Author:   Hayden Ivatts
 *  Email:    hpivat21@g.holycross.edu
 *  Written:  5-2-18
 *  
 *  You lose displays a discouraging message when you click on a bomb
 *  and lose the game.
 *----------------------------------------------------------------*/

import GUI.*;

/**
 * A <i>YouLose</i> object represents a message that appears on the screen
 * you lose the game.
 */
public class YouLose extends Widget {

    /**
     * The width of the box as it is shown on the screen.
     */
    public static final int WIDTH = 300;

    /**
     * The height of the box as it is shown on the screen.
     */
    public static final int HEIGHT = 200;

    

    /**
     * Initialize a new status box for the given game.
     * @param x the x coordinate of the location to draw the box.
     * @param y the y coordinate of the location to draw the box.
     */
    public YouLose(int x, int y) {
        super(x, y, WIDTH, HEIGHT);
    }

    public void repaint(GUI.Canvas canvas) {
        // Draw a yellow box with a red outline
        canvas.setPenColor(Canvas.YELLOW);
        canvas.filledRectangle(x, y, width, height);
        canvas.setPenColor(Canvas.RED);
        canvas.setPenRadius(2.0);
        canvas.rectangle(x+0.5, y+0.5, width-1, height-1);

        // Draw some mean text suggesting the loser run
        canvas.setFont(Canvas.DEFAULT_FONT);
        canvas.setPenColor(Canvas.BLACK);
        canvas.setFont(30);
        canvas.textLeft(x + 15, y + 15, "You lost the game");
        canvas.setFont(12);
        canvas.textLeft(x + 15, y + 45, "The mines are blowing up all around you");
        canvas.setFont(40);
        canvas.setPenColor(canvas.BLUE);
        canvas.textLeft(x + 15, y + 85, "RUN!!");
        
        
    }
}
