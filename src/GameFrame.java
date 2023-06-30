import javax.swing.*;

public class GameFrame extends JFrame {
    GamePanel gamePanel = new GamePanel();
    GameFrame(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(gamePanel);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }
}
