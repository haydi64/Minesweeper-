/*----------------------------------------------------------------
 *  Author:   Hayden Ivatts
 *  Email:    hpivat21@g.holycross.edu
 *  Written:  5-2-18
 *  
 *  YouWin prints a message congratulating someone when they win the game
 *----------------------------------------------------------------*/

import GUI.*;

/**
 * A <i>YouWin</i> object represents a message that appears on the screen
 * when you win the game.
 */
public class YouWin extends Widget {

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
    public YouWin(int x, int y) {
        super(x, y, WIDTH, HEIGHT);
    }

    public void repaint(GUI.Canvas canvas) {
        // Draw a green box with a blue outline
        canvas.setPenColor(Canvas.GREEN);
        canvas.filledRectangle(x, y, width, height);
        canvas.setPenColor(Canvas.BLUE);
        canvas.setPenRadius(2.0);
        canvas.rectangle(x+0.5, y+0.5, width-1, height-1);

        // Draw some encourgaing text telling the user to go celebrate
        canvas.setFont(Canvas.DEFAULT_FONT);
        canvas.setPenColor(Canvas.MAGENTA);
        canvas.setFont(30);
        canvas.textLeft(x + 15, y + 15, "CONGRATS!!!!");
        canvas.setFont(12);
        canvas.textLeft(x + 15, y + 45, "You have escaped the mines");
        canvas.setFont(30);
        canvas.textLeft(x + 15, y + 85, "Go celebrate!");
        
        
    }
}
