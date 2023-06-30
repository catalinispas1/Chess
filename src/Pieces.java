import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.ImageObserver;

public class Pieces implements ImageObserver{
    private final GamePanel gamePanel;
    private final Image image;
    private double imageHeight;
    private double imageWidth;
    Point imageCorner;
    private Point previousPoint;
    Point initialPoint;
    final String color;
    final String pieceType;
    private boolean pawnFirstMove;
    boolean pieceSelected;
    boolean deadPiece;
    boolean legalMoveMade;
    static private int getTurn;
    static boolean kingChess;
    static boolean upgradingPiece;

    Pieces(Image image, int xPosition, int yPosition, GamePanel gamePanel, String color, String pieceType, boolean deadPiece){
        this.deadPiece = deadPiece;
        this.gamePanel = gamePanel;
        this.image = image;
        imageCorner = new Point(xPosition, yPosition);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        gamePanel.addMouseListener(clickListener);
        gamePanel.addMouseMotionListener(dragListener);
        this.color = color;
        this.pieceType = pieceType;
        if(!deadPiece) gamePanel.chessBoard[yPosition / gamePanel.gameUnit][xPosition / gamePanel.gameUnit] = color;
    }

    public void draw(Graphics g){
        imageHeight = image.getHeight (this) * 1.5;
        imageWidth = image.getWidth (this) * 1.5;

        if(!deadPiece) g.drawImage(image, (int) imageCorner.getX(), (int) imageCorner.getY(), (int) imageWidth, (int) imageHeight, null);
    }

    private class ClickListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            previousPoint = e.getPoint();
            pieceSelected = containsPiece(previousPoint);
            if(pieceSelected){
                initialPoint = new Point(imageCorner);
            }
            legalMoveMade = false;

        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if(pieceSelected){
                int newImageX = (int) (previousPoint.getX()) / gamePanel.gameUnit * gamePanel.gameUnit;
                int newImageY = (int) (previousPoint.getY()) / gamePanel.gameUnit * gamePanel.gameUnit;

                if(isLegalMove(newImageX / gamePanel.gameUnit, newImageY / gamePanel.gameUnit)) {
                    imageCorner = new Point(newImageX, newImageY);
                    legalMoveMade = true;

                    boolean pieceKilled = false;
                    boolean chess = false;
                    Pieces piece = null;
                    String tempColor = null;

                    if(gamePanel.chessBoard[newImageY / gamePanel.gameUnit] [newImageX / gamePanel.gameUnit] != null){
                        tempColor = gamePanel.chessBoard[newImageY / gamePanel.gameUnit] [newImageX / gamePanel.gameUnit];
                        pieceKilled = true;
                        piece = gamePanel.setDeadPiece(newImageX, newImageY);
                        imageCorner = new Point(newImageX, newImageY);
                    }

                    gamePanel.chessBoard[newImageY / gamePanel.gameUnit] [newImageX / gamePanel.gameUnit] = color;
                    gamePanel.chessBoard[(int)initialPoint.getY() / gamePanel.gameUnit] [(int)initialPoint.getX() / gamePanel.gameUnit] = null;

                    if(color.equals("white") && gamePanel.whiteChessCheck()) {
                        imageCorner = new Point(initialPoint);
                        gamePanel.chessBoard[(int) initialPoint.getY() / gamePanel.gameUnit][(int) initialPoint.getX() / gamePanel.gameUnit] = color;
                        gamePanel.chessBoard[newImageY / gamePanel.gameUnit][newImageX / gamePanel.gameUnit] = tempColor;
                        getTurn--;
                        chess = true;
                    } else if (color.equals("black") && gamePanel.blackChessCheck()) {
                        imageCorner = new Point(initialPoint);
                        gamePanel.chessBoard[(int)initialPoint.getY() / gamePanel.gameUnit] [(int)initialPoint.getX() / gamePanel.gameUnit] = color;
                        gamePanel.chessBoard[newImageY / gamePanel.gameUnit] [newImageX / gamePanel.gameUnit] = tempColor;
                        getTurn--;
                        chess = true;
                    }

                    if(pieceKilled && chess){
                        gamePanel.setAlive(piece, newImageX, newImageY);
                    }

                    if(pieceType.equals("pawn")){
                        pawnFirstMove = true;

                        if((int) imageCorner.getY() / gamePanel.gameUnit == 0 || (int) imageCorner.getY() / gamePanel.gameUnit == 7){
                            gamePanel.upgradePiece(newImageX, newImageY);
                            upgradingPiece = true;
                        }
                    }

                    getTurn++;
                }
                else imageCorner = new Point(initialPoint);
                gamePanel.repaint();
            }
        }
    }

    private void dragPiece(Point currentPoint){
        imageCorner.translate((int) (currentPoint.getX() - previousPoint.getX()),
                ((int) (currentPoint.getY() - previousPoint.getY())));

        previousPoint = currentPoint;
        gamePanel.repaint();
    }

    private class DragListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if(!upgradingPiece) {
                if (getTurn % 2 == 0 && !kingChess) {
                    if (pieceSelected && color.equals("white")) {
                        dragPiece(e.getPoint());
                    }
                } else {
                    if (pieceSelected && color.equals("black")) {
                        dragPiece(e.getPoint());
                    }
                }
            }
        }
    }
    private boolean isLegalMove(int newImageX, int newImageY){
        if (newImageX > 7 || newImageX < 0 || newImageY >  7 || newImageY < 0) return false;
        if (gamePanel.checkKingCaptured(newImageX, newImageY)) return false;

        boolean[][] movableMap = new boolean[gamePanel.chessBoard.length][gamePanel.chessBoard.length];

        int initialX = (int) initialPoint.getX() / gamePanel.gameUnit;
        int initialY = (int) initialPoint.getY() / gamePanel.gameUnit;

        if(pieceType.equals("pawn") && color.equals("white")) {
            if(initialY - 1 >= 0 && gamePanel.chessBoard[initialY - 1][initialX] == null) {
                movableMap[initialY - 1][initialX] = true;
            }
            if(!pawnFirstMove && initialY - 2 >= 0 && gamePanel.chessBoard[initialY - 2][initialX] == null && gamePanel.chessBoard[initialY - 1][initialX] == null){
                movableMap[initialY - 2][initialX] = true;
            }
            checkWhitePawnAttacking(movableMap, initialX, initialY, newImageX, newImageY);
        } else if(pieceType.equals("pawn") && color.equals("black")){
            if (initialY + 1 >= 0 && gamePanel.chessBoard[initialY + 1][initialX] == null) {
                movableMap[initialY + 1][initialX] = true;
            }
            if(!pawnFirstMove && initialY + 2 >= 0 && gamePanel.chessBoard[initialY + 2][initialX] == null && gamePanel.chessBoard[initialY + 1][initialX] == null){
                movableMap[initialY + 2][initialX] = true;
            }
            checkBlackPawnAttacking(movableMap, initialX, initialY, newImageX, newImageY);
        } else if(pieceType.equals("rook")){
            checkLineMoovable(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("knight")){
            checkMoovableKnight(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("bishop")) {
            checkDiagonallyMoovable(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("queen")) {
            checkDiagonallyMoovable(movableMap, initialX, initialY, newImageX, newImageY);
            checkLineMoovable(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("king")) {
            checkMoovableKing(movableMap, initialX, initialY, newImageX, newImageY);
        }
        return movableMap[newImageY][newImageX];
    }

    public void chess(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY){
        if(pieceType.equals("pawn") && color.equals("white")) {
            checkWhitePawnAttacking(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("pawn") && color.equals("black")) {
            checkBlackPawnAttacking(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("bishop")) {
            checkDiagonallyMoovable(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("knight")) {
            checkMoovableKnight(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("rook")){
            checkLineMoovable(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("queen")){
            checkLineMoovable(movableMap, initialX, initialY, newImageX, newImageY);
            checkDiagonallyMoovable(movableMap, initialX, initialY, newImageX, newImageY);
        } else if (pieceType.equals("king")) {
            checkMoovableKing(movableMap, initialX, initialY, newImageX, newImageY);
        }
    }

    private void checkWhitePawnAttacking(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY){
        if((initialX > 0 && initialX - 1 == newImageX  || initialX < gamePanel.chessBoard.length && initialX + 1 == newImageX) &&
                (gamePanel.chessBoard[newImageY][newImageX] != null && gamePanel.chessBoard[newImageY][newImageX].equals("black") && initialY - 1 == newImageY)){
            movableMap[newImageY][newImageX] = true;
        }
    }
    private void checkBlackPawnAttacking(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY){
        if((initialX > 0 && initialX - 1 == newImageX  || initialX < gamePanel.chessBoard.length && initialX + 1 == newImageX) &&
                (gamePanel.chessBoard[newImageY][newImageX] != null && gamePanel.chessBoard[newImageY][newImageX].equals("white") && initialY + 1 == newImageY)){
            movableMap[newImageY][newImageX] = true;
        }
    }
    private void checkLineMoovable(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY) {
        for(int i = initialY - 1; i >= newImageY; i--){
            if(gamePanel.chessBoard[i][initialX] != null){
                if(color.equals(gamePanel.chessBoard[i][initialX])) break;
                else {
                    movableMap[i][initialX] = true;
                    break;
                }
            } else {
                movableMap[i][initialX] = true;
            }
        }
        for(int i = initialY + 1; i <= newImageY; i++){
            if(gamePanel.chessBoard[i][initialX] != null){
                if(color.equals(gamePanel.chessBoard[i][initialX])) break;
                else {
                    movableMap[i][initialX] = true;
                    break;
                }
            } else {
                movableMap[i][initialX] = true;
            }
        }
        for(int j = initialX + 1; j <= newImageX; j++){
            if(gamePanel.chessBoard[initialY][j] != null){
                if(color.equals(gamePanel.chessBoard[initialY][j])) break;
                else {
                    movableMap[initialY][j] = true;
                    break;
                }
            } else {
                movableMap[initialY][j] = true;
            }
        }
        for(int j = initialX - 1; j >= newImageX; j--){
            if(gamePanel.chessBoard[initialY][j] != null){
                if(color.equals(gamePanel.chessBoard[initialY][j])) break;
                else {
                    movableMap[initialY][j] = true;
                    break;
                }
            } else {
                movableMap[initialY][j] = true;
            }
        }
    }

    private void checkDiagonallyMoovable(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY){
        int i = initialY - 1;
        int j = initialX - 1;
        while (i >= newImageY && j >= newImageX){
            if(gamePanel.chessBoard[i][j] != null){
                if(color.equals(gamePanel.chessBoard[i][j])) break;
                else {
                    movableMap[i][j] = true;
                    break;
                }
            } else {
                movableMap[i][j] = true;
            }
            i--;
            j--;
        }

        i = initialY - 1;
        j = initialX + 1;
        while (i >= newImageY && j <= newImageX){
            if(gamePanel.chessBoard[i][j] != null){
                if(color.equals(gamePanel.chessBoard[i][j])) break;
                else {
                    movableMap[i][j] = true;
                    break;
                }
            } else {
                movableMap[i][j] = true;
            }
            i--;
            j++;
        }

        i = initialY + 1;
        j = initialX + 1;
        while (i <= newImageY && j <= newImageX){
            if(gamePanel.chessBoard[i][j] != null){
                if(color.equals(gamePanel.chessBoard[i][j])) break;
                else {
                    movableMap[i][j] = true;
                    break;
                }
            } else {
                movableMap[i][j] = true;
            }
            i++;
            j++;
        }

        i = initialY + 1;
        j = initialX - 1;
        while (i <= newImageY && j >= newImageX){
            if(gamePanel.chessBoard[i][j] != null){
                if(color.equals(gamePanel.chessBoard[i][j])) break;
                else {
                    movableMap[i][j] = true;
                    break;
                }
            } else {
                movableMap[i][j] = true;
            }
            i++;
            j--;
        }
    }
    private void checkMoovableKnight(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY){
        if( ( (initialX == newImageX + 1 || initialX == newImageX - 1) && (initialY == newImageY + 2 || initialY == newImageY - 2)
                || (initialX == newImageX + 2 || initialX == newImageX - 2) && (initialY == newImageY + 1 || initialY == newImageY - 1) )
                && (gamePanel.chessBoard[newImageY][newImageX] == null || !gamePanel.chessBoard[newImageY][newImageX].equals(color) ) ) {
            movableMap[newImageY][newImageX] = true;
        }
    }
    private void checkMoovableKing(boolean[][] movableMap, int initialX, int initialY, int newImageX, int newImageY){
        boolean left = newImageX == initialX - 1 && newImageY == initialY;
        boolean leftUp = newImageX == initialX - 1 && newImageY == initialY + 1;
        boolean up = newImageX == initialX && newImageY == initialY - 1;
        boolean upRight = newImageX == initialX + 1 && newImageY == initialY - 1;
        boolean right = newImageX == initialX + 1 && newImageY == initialY;
        boolean rightDown = newImageX == initialX + 1 && newImageY == initialY + 1;
        boolean down = newImageX == initialX && newImageY == initialY + 1;
        boolean downLeft = newImageX == initialX - 1 && newImageY == initialY - 1;

        if ( (left || leftUp || up || upRight || right || rightDown || down || downLeft) &&
                (gamePanel.chessBoard[newImageY][newImageX] == null || !gamePanel.chessBoard[newImageY][newImageX].equals(color)) ) {
            movableMap[newImageY][newImageX] = true;
        }
    }

    private boolean containsPiece(Point point){
        double x = point.getX();
        double y = point.getY();

        return x >= imageCorner.getX() && y >= imageCorner.getY()
                    && x <= imageCorner.getX() + imageWidth
                    && y <= imageCorner.getY() + imageHeight;
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
}
