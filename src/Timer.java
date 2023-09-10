import java.awt.*;

public class Timer extends Thread{
    private int whiteMinutes = 10;
    private int whiteSeconds = 0;
    private int blackMinutes = 10;
    private int blackSeconds = 0;

    public void draw(Graphics g){
        Font customFont = new Font("Arial", Font.PLAIN, 50);
        g.setColor(new Color(255, 255, 255));
        g.setFont(customFont);
        g.drawString("White:", 940, 700);

        if (whiteSeconds < 10 && whiteMinutes < 10){
            g.drawString("0" + whiteMinutes + " : " + "0" + whiteSeconds, 930, 760);
        }
        else if (whiteMinutes == 10 && whiteSeconds < 10){
            g.drawString("" + whiteMinutes + " : 0" + whiteSeconds, 930, 760);
        } else g.drawString("0" + whiteMinutes + " : " + whiteSeconds, 930, 760);

        g.drawString("Black:", 940, 80);
        if (blackSeconds < 10 && blackMinutes < 10){
            g.drawString("0" + blackMinutes + " : " + "0" + blackSeconds, 930, 140);
        }
        else if (blackMinutes == 10 && blackSeconds < 10){
            g.drawString("" + blackMinutes + " : 0" + blackSeconds, 930, 140);
        } else g.drawString("0" + blackMinutes + " : " + blackSeconds, 930, 140);

        customFont = new Font("Arial", Font.PLAIN, 25);
        g.setFont(customFont);
        if (GamePanel.whiteWon) g.drawString("WHITE WON!", 920, 550);
        else if (GamePanel.blackWon) g.drawString("BLACK WON!", 920, 250);
        if (GamePanel.staleMate) g.drawString("STALEMATE, IT'S A DRAW", 840, 250);
    }
    public void run(){
        while (GamePanel.gameRunning) {
            if(whiteSeconds == 0 && whiteMinutes == 0) {
                GamePanel.blackWon = true;
                GamePanel.gameRunning = false;
                GamePanel.play.setVisible(true);
                break;
            } else if(blackSeconds == 0 && blackMinutes == 0) {
                GamePanel.whiteWon = true;
                GamePanel.gameRunning = false;
                GamePanel.play.setVisible(true);
                break;
            }

            if(Pieces.getTurn % 2 == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                whiteSeconds--;
                if (whiteSeconds < 0) {
                    whiteSeconds = 59;
                    whiteMinutes--;
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                blackSeconds--;
                if (blackSeconds < 0) {
                    blackSeconds = 59;
                    blackMinutes--;
                }
            }
        }
    }
}
