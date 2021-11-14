import javax.imageio.ImageIO;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;


public class Lienzo extends Canvas implements Runnable {
    private final VentanaPrincipal ventanaPrincipal;
    private       BufferedImage    background;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public Lienzo(int x, int y, VentanaPrincipal ventanaPrincipal) {
        this.setSize(x, y);
        this.setBackground(Color.BLACK);
        try {
            background = ImageIO.read(new File("src/background.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ventanaPrincipal = ventanaPrincipal;
    }

    //----------------------------------------------------------------------------------------------------------GRAPHICS

    private void dibujar() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            int numeroBuffers = 3;
            this.createBufferStrategy(numeroBuffers);
            System.out.println("Creada nueva estrategia con " + numeroBuffers + " buffers de memoria");
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(background, 0, 0, null);

        for (Fuego fuego : ventanaPrincipal.obtenerListaFuegos()) {
            fuego.dibujar(g);
        }

        bs.show();
        g.dispose();
    }

    //------------------------------------------------------------------------------------------------------------THREAD

    @Override
    public void run() {
        while (true) {
            this.dibujar();
            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }

    //---------------------------------------------------------------------------------------------------------------DTO

    public void setBackground(BufferedImage background) {this.background = background;}

}
