import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    final int size = 800;
    final int gameUnit = size/8;
    Pieces[] pieces;
    String[][] chessBoard;
    JButton[] upgradeButton;
    Pieces[] sparePieces;
    int whiteQueenAlived = 0;
    int blackQueenAlived = 8;
    int whiteBishopAlived = 16;
    int blackBishopAlived = 24;
    int whiteKnightAlived = 32;
    int blackKnightAlived = 40;
    int whiteRookAlived = 48;
    int blackRookAlived = 56;
    int xPawnUpgrade;
    int yPawnUpgrade;
    Point movedPosition;
    Point initialPosition;
    GamePanel(){
        this.setPreferredSize(new Dimension(size,size));
        pieces = new Pieces[32];
        chessBoard = new String[size/gameUnit][size/gameUnit];

        int xPosition = 0;
        for(int i = 0; i < 8; i++){
            pieces[i] = new Pieces(new ImageIcon("BlackPieces/pion negru.png").getImage(),xPosition, gameUnit, this, "black", "pawn", false);
            xPosition += gameUnit;
        }
        xPosition = 0;
        for(int i = 8; i < 16; i++){
            pieces[i] = new Pieces(new ImageIcon("WhitePieces/pion alb.png").getImage(), xPosition, gameUnit * 6, this, "white", "pawn", false);
            xPosition += gameUnit;
        }

        pieces[16] = new Pieces(new ImageIcon("WhitePieces/tura alba.png").getImage(), 0, gameUnit * 7, this, "white", "rook", false);
        pieces[17] = new Pieces(new ImageIcon("WhitePieces/tura alba.png").getImage(), gameUnit * 7, gameUnit * 7, this, "white", "rook", false);
        pieces[18] = new Pieces(new ImageIcon("BlackPieces/tura neagra.png").getImage(), 0, 0, this, "black", "rook", false);
        pieces[19] = new Pieces(new ImageIcon("BlackPieces/tura neagra.png").getImage(), gameUnit * 7, 0, this, "black", "rook", false);

        pieces[20] = new Pieces(new ImageIcon("WhitePieces/cal alb.png").getImage(), gameUnit, gameUnit * 7, this, "white", "knight", false);
        pieces[21] = new Pieces(new ImageIcon("WhitePieces/cal alb.png").getImage(), gameUnit * 6, gameUnit * 7, this, "white", "knight", false);
        pieces[22] = new Pieces(new ImageIcon("BlackPieces/cal negru.png").getImage(), gameUnit, 0, this, "black", "knight", false);
        pieces[23] = new Pieces(new ImageIcon("BlackPieces/cal negru.png").getImage(), gameUnit * 6, 0, this, "black", "knight", false);

        pieces[24] = new Pieces(new ImageIcon("WhitePieces/nebun alb.png").getImage(), gameUnit * 2, gameUnit * 7, this, "white", "bishop", false);
        pieces[25] = new Pieces(new ImageIcon("WhitePieces/nebun alb.png").getImage(), gameUnit * 5, gameUnit * 7, this, "white", "bishop", false);
        pieces[26] = new Pieces(new ImageIcon("BlackPieces/nebun negru.png").getImage(), gameUnit * 2, 0, this, "black", "bishop", false);
        pieces[27] = new Pieces(new ImageIcon("BlackPieces/nebun negru.png").getImage(), gameUnit * 5, 0, this, "black", "bishop", false);

        pieces[28] = new Pieces(new ImageIcon("WhitePieces/regina alba.png").getImage(), gameUnit * 3, gameUnit * 7, this, "white", "queen", false);
        pieces[29] = new Pieces(new ImageIcon("BlackPieces/regina neagra.png").getImage(), gameUnit * 3, 0, this, "black", "queen", false);

        pieces[30] = new Pieces(new ImageIcon("WhitePieces/rege alb.png").getImage(), gameUnit * 4, gameUnit * 7, this, "white", "king", false);
        pieces[31] = new Pieces(new ImageIcon("BlackPieces/rege negru.png").getImage(), gameUnit * 4, 0, this, "black", "king", false);


        this.setLayout(null);
        upgradeButton = new JButton[4];
        upgradeButton[0] = new JButton("Queen");
        upgradeButton[0].setLocation(size / 2 - 200, size / 2 - 12);

        upgradeButton[1] = new JButton("Bishop");
        upgradeButton[1].setLocation(size / 2 - 100, size / 2 - 12);

        upgradeButton[2] = new JButton("Knight");
        upgradeButton[2].setLocation(size / 2, size / 2 - 12);

        upgradeButton[3] = new JButton("Rook");
        upgradeButton[3].setLocation(size / 2 + 100, size / 2 - 12);



        for (JButton button : upgradeButton) {
            button.setSize(100, 25);
            this.add(button);
            button.setVisible(false);
            button.addActionListener(this);
        }

        sparePieces = new Pieces[64];
        for(int i = 0; i < 8; i++){
            sparePieces[i] = new Pieces(new ImageIcon("WhitePieces/regina alba.png").getImage(), -250, -250, this, "white", "queen", true);
        }
        for(int i = 8; i < 16; i++){
            sparePieces[i] = new Pieces(new ImageIcon("BlackPieces/regina neagra.png").getImage(), -250, -250, this, "black", "queen", true);
        }
        for(int i = 16; i < 24; i++){
            sparePieces[i] = new Pieces(new ImageIcon("WhitePieces/nebun alb.png").getImage(), -250, -250, this, "white", "bishop", true);
        }
        for(int i = 24; i < 32; i++){
            sparePieces[i] = new Pieces(new ImageIcon("BlackPieces/nebun negru.png").getImage(), -250, -250, this, "black", "bishop", true);
        }
        for(int i = 32; i < 40; i++){
            sparePieces[i] = new Pieces(new ImageIcon("WhitePieces/cal alb.png").getImage(), -250, -250, this, "white", "knight", true);
        }
        for(int i = 40; i < 48; i++){
            sparePieces[i] = new Pieces(new ImageIcon("BlackPieces/cal negru.png").getImage(), -250, -250, this, "black", "knight", true);
        }
        for(int i = 48; i < 56; i++){
            sparePieces[i] = new Pieces(new ImageIcon("WhitePieces/tura alba.png").getImage(), -250, -250, this, "white", "rook", true);
        }
        for(int i = 56; i < 64; i++){
            sparePieces[i] = new Pieces(new ImageIcon("BlackPieces/tura neagra.png").getImage(), -250, -250, this, "black", "rook", true);
        }
        movedPosition = new Point(-1, -1);
        initialPosition = new Point(-1, -1);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        for(Pieces pieces: pieces){
            pieces.draw(g);
        }
        for(Pieces pieces: sparePieces){
            pieces.draw(g);
        }
    }

    public void draw(Graphics g){
        int drawSquares = 0;
        for(int i = 0; i < size; i = i + gameUnit){
            for(int j = 0; j < size; j = j + gameUnit){
                if(drawSquares % 2 == 0){
                    g.setColor(new Color(225, 217, 217));
                    g.fillRect(i,j,gameUnit,gameUnit);
                }else {
                    g.setColor(new Color(55, 73, 43));
                    g.fillRect(i,j,gameUnit,gameUnit);
                }
                drawSquares++;
            }
            drawSquares++;
        }
    }

    public void upgradePiece(int x, int y){
        for(JButton button: upgradeButton){
            button.setVisible(true);
        }
        this.xPawnUpgrade = x;
        this.yPawnUpgrade = y;
    }

    public Pieces setDeadPiece(int x, int y){
        for (Pieces pieces: pieces){
            if(pieces.imageCorner.getX() == x && pieces.imageCorner.getY() == y && !pieces.legalMoveMade){
                pieces.deadPiece = true;
                pieces.imageCorner = new Point(-250,-250);
                return pieces;
            }
        }
        for(Pieces pieces: sparePieces){
            if(pieces.imageCorner.getX() == x && pieces.imageCorner.getY() == y && !pieces.legalMoveMade){
                pieces.deadPiece = true;
                pieces.imageCorner = new Point(-250,-250);
                System.out.println("piece found and killer");
                return pieces;
            }
        }
        return null;
    }

    public void setAlive(Pieces piece, int x, int y){
        piece.imageCorner = new Point(x, y);
        piece.deadPiece = false;
    }

    public boolean whiteChessCheck(){
        if (pieces[30].deadPiece) return false;

        int whiteKingX = (int) pieces[30].imageCorner.getX() / gameUnit;
        int whiteKingY = (int) pieces[30].imageCorner.getY() / gameUnit;

        boolean[][] movableMap = new boolean[size / gameUnit][size / gameUnit];

        for(Pieces pieces: pieces){
            if(pieces.color.equals("black") && !pieces.deadPiece){
                pieces.chess(movableMap, (int) pieces.imageCorner.getX() / gameUnit, (int) pieces.imageCorner.getY() / gameUnit, whiteKingX, whiteKingY);
            }
        }
        for(Pieces pieces: sparePieces){
            if(pieces.color.equals("black") && !pieces.deadPiece){
                pieces.chess(movableMap, (int) pieces.imageCorner.getX() / gameUnit, (int) pieces.imageCorner.getY() / gameUnit, whiteKingX, whiteKingY);
            }
        }
        return movableMap[whiteKingY][whiteKingX];
    }

    public boolean blackChessCheck(){
        if (pieces[31].deadPiece) return false;

        int blackKingX = (int) pieces[31].imageCorner.getX() / gameUnit;
        int blackKingY = (int) pieces[31].imageCorner.getY() / gameUnit;
        boolean[][] movableMap = new boolean[size / gameUnit][size / gameUnit];

        for(Pieces pieces: pieces){
            if(pieces.color.equals("white") && !pieces.deadPiece){
                pieces.chess(movableMap, (int) pieces.imageCorner.getX() / gameUnit, (int) pieces.imageCorner.getY() / gameUnit, blackKingX, blackKingY);
            }
        }
        for(Pieces pieces: sparePieces){
            if(pieces.color.equals("white") && !pieces.deadPiece){
                pieces.chess(movableMap, (int) pieces.imageCorner.getX() / gameUnit, (int) pieces.imageCorner.getY() / gameUnit, blackKingX, blackKingY);
            }
        }
        return movableMap[blackKingY][blackKingX];
    }

    public boolean checkKingCaptured(int x, int y){
        return ((int) pieces[30].imageCorner.getX() == x * gameUnit && (int) pieces[30].imageCorner.getY() == y * gameUnit) ||
                ((int) pieces[31].imageCorner.getX() == x * gameUnit && (int) pieces[31].imageCorner.getY() == y * gameUnit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Pieces pieces: pieces){
            if(pieces.imageCorner.getX() == xPawnUpgrade && pieces.imageCorner.getY() == yPawnUpgrade){
                pieces.deadPiece = true;
                pieces.imageCorner = new Point(-250,-250);
            }
        }

        if(e.getSource() == upgradeButton[0]){
            if(yPawnUpgrade == 0){
                setAlive(sparePieces[whiteQueenAlived], xPawnUpgrade, yPawnUpgrade);
                whiteQueenAlived++;
            } else {
                setAlive(sparePieces[blackQueenAlived], xPawnUpgrade, yPawnUpgrade);
                blackQueenAlived++;
            }
        } else if (e.getSource() == upgradeButton[1]) {
            if(yPawnUpgrade == 0){
                setAlive(sparePieces[whiteBishopAlived], xPawnUpgrade, yPawnUpgrade);
                whiteQueenAlived++;
            } else {
                setAlive(sparePieces[blackBishopAlived], xPawnUpgrade, yPawnUpgrade);
                blackBishopAlived++;
            }
        } else if (e.getSource() == upgradeButton[2]) {
            if(yPawnUpgrade == 0){
                setAlive(sparePieces[whiteKnightAlived], xPawnUpgrade, yPawnUpgrade);
                whiteKnightAlived++;
            } else {
                setAlive(sparePieces[blackKnightAlived], xPawnUpgrade, yPawnUpgrade);
                blackKnightAlived++;
            }
        } else if (e.getSource() == upgradeButton[3]) {
            if(yPawnUpgrade == 0){
                setAlive(sparePieces[whiteRookAlived], xPawnUpgrade, yPawnUpgrade);
                whiteRookAlived++;
            } else {
                setAlive(sparePieces[blackRookAlived], xPawnUpgrade, yPawnUpgrade);
                blackRookAlived++;
            }
        }

        Pieces.upgradingPiece = false;
        repaint();
        for(JButton button: upgradeButton){
            button.setVisible(false);
        }
    }
}
