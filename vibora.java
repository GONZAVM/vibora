import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Vibora extends JFrame {

    ImagenSnake imagenSnake;
    Point snake;
    Point comida;
    ArrayList<Point> listaPosiciones = new ArrayList<Point>();

    int longitud = 3;
    int manzanasComidas = 0;

    int width = 1000;
    int height = 500;

    int widthPoint = 10;
    int heightPoint = 10;

    String direccion = "RIGHT";
    long frequency = 60;

    boolean gameOver = false;
    boolean inMenu = true;

    MenuInicio menuInicio;

    public Vibora() {
        setTitle("Snake");

        startGame();
        imagenSnake = new ImagenSnake();
        menuInicio = new MenuInicio();

        this.getContentPane().add(imagenSnake);

        setSize(width, height);

        this.addKeyListener(new Teclas());
        this.addMouseListener(new ClickMouse());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        setVisible(true);
        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void startGame() {
        manzanasComidas = 0;
        comida = new Point(200, 100);
        snake = new Point(320, 240);
        listaPosiciones = new ArrayList<Point>();
        listaPosiciones.add(snake);

        longitud = listaPosiciones.size();
    }

    public void generarComida() {
        Random rnd = new Random();
        comida.x = (rnd.nextInt(width - widthPoint)) + widthPoint;
        comida.y = (rnd.nextInt(height - heightPoint)) + heightPoint;
    }

    public void actualizar() {
        listaPosiciones.add(0, new Point(snake.x, snake.y));
        listaPosiciones.remove(listaPosiciones.size() - 1);

        for (int i = 1; i < listaPosiciones.size(); i++) {
            Point point = listaPosiciones.get(i);
            if (snake.x == point.x && snake.y == point.y) {
                gameOver = true;
            }
        }

        if ((snake.x > (comida.x - 10) && snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10) && snake.y < (comida.y + 10))) {
            manzanasComidas++;
            listaPosiciones.add(0, new Point(snake.x, snake.y));
            System.out.println(listaPosiciones.size());
            generarComida();
        }
        imagenSnake.repaint();
    }

    public static void main(String[] args) {
        Vibora snake1 = new Vibora();
    }

    public class ImagenSnake extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (gameOver) {
                g.setColor(new Color(0, 0, 0));
            } else {
                g.setColor(new Color(0, 0, 0));
            }
            g.fillRect(0, 0, width, height);
            g.setColor(new Color(255, 255, 255));

            if (inMenu) {
                menuInicio.paintComponent(g);
            } else {
                if (listaPosiciones.size() > 0) {
                    for (int i = 0; i < listaPosiciones.size(); i++) {
                        Point p = (Point) listaPosiciones.get(i);
                        g.fillRect(p.x, p.y, widthPoint, heightPoint);
                    }
                }

                Random rnd = new Random();
                Color colorComida = new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                g.setColor(colorComida);
                g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Manzanas comidas: " + manzanasComidas, 20, 30);

                if (gameOver) {
                    g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                    g.drawString("Fin del juego", 300, 200);
                    g.drawString("Record " + (listaPosiciones.size() - 1), 300, 240);

                    g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                    g.drawString("Jugar de nuevo", 100, 320);
                    g.drawString("Salir", 100, 340);
                }
            }
        }
    }

    public class Teclas extends java.awt.event.KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (inMenu) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    inMenu = false;
                    gameOver = false;
                    startGame();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            } else {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (direccion != "LEFT") {
                        direccion = "RIGHT";
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (direccion != "RIGHT") {
                        direccion = "LEFT";
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (direccion != "DOWN") {
                        direccion = "UP";
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (direccion != "UP") {
                        direccion = "DOWN";
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_N) {
                    gameOver = false;
                    startGame();
                }
            }
        }
    }

    public class Momento extends Thread {
        private long last = 0;

        public Momento() {
        }

        public void run() {
            while (true) {
                if ((java.lang.System.currentTimeMillis() - last) > frequency) {
                    if (!gameOver && !inMenu) {
                        if (direccion == "RIGHT") {
                            snake.x = snake.x + widthPoint;
                            if (snake.x > width) {
                                snake.x = 0;
                            }
                        } else if (direccion == "LEFT") {
                            snake.x = snake.x - widthPoint;
                            if (snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                        } else if (direccion == "UP") {
                            snake.y = snake.y - heightPoint;
                            if (snake.y < 0) {
                                snake.y = height;
                            }
                        } else if (direccion == "DOWN") {
                            snake.y = snake.y + heightPoint;
                            if (snake.y > height) {
                                snake.y = 0;
                            }
                        }
                    }
                    actualizar();

                    last = java.lang.System.currentTimeMillis();
                }
            }
        }
    }

    public class MenuInicio {
        public void paintComponent(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 1000, 500); // Ajusta las dimensiones según las de tu ventana principal

            // Dibuja "SNAKE" en letras grandes
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            g.drawString("SNAKE", 300, 200);

            // Dibuja opciones del menú
            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            g.drawString("Jugar", 100, 300);
            g.drawString("Ajustes", 100, 330);
            g.drawString("Estadísticas", 100, 360);
            g.drawString("Salir", 100, 390);
        }
    }

    public class ClickMouse extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (inMenu) {
                if (e.getX() >= 100 && e.getX() <= 160 && e.getY() >= 270 && e.getY() <= 290) {
                    inMenu = false;
                    gameOver = false;
                    startGame();
                } else if (e.getX() >= 100 && e.getX() <= 160 && e.getY() >= 300 && e.getY() <= 320) {
                    // Ajustes
                } else if (e.getX() >= 100 && e.getX() <= 160 && e.getY() >= 330 && e.getY() <= 350) {
                    // Estadísticas
                } else if (e.getX() >= 100 && e.getX() <= 160 && e.getY() >= 360 && e.getY() <= 380) {
                    System.exit(0);
                }
            }
        }
    }
}
