import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Fuego extends Thread {
    private final int           x, y;
    private final int           altura;
    private final int           ancho;
    private final int[][]       pixels;
    private final BufferedImage imagen;
    private final PaletaColores paleta;
    private double              gas;
    private int                 probabilidadIncendio;

    //-----------------------------------------------------------------------------------------------------CONSTRUCTORES

    public Fuego(int x, int y, int ancho, int altura, PaletaColores paleta) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.altura = altura;
        this.paleta = paleta;
        this.pixels = new int[ancho][altura];
        this.imagen = new BufferedImage(ancho, altura, BufferedImage.TYPE_INT_ARGB);
        this.gas = 1.5; //1.5
        this.probabilidadIncendio = 250; //250
    }

    //-----------------------------------------------------------------------------------------------------------GESTION

    public void actualizar() {
        encenderPixeles();
        propagarPixeles();
        enfriarPixeles();
    }

    //----------------------------------------------------------------------------------------------------------GRAFICOS

    private void encenderPixeles() {
        boolean seEnciende;
        Random probabilidad = new Random();
        for (int fx = 0; fx < ancho; fx++) {
            //Si el la llama esta cerca del centro, hay mas posibilidades de que se encienda
            if (fx >= ancho/2 - ancho/3 && fx <= ancho/2 + ancho/3) {
                seEnciende = probabilidad.nextInt(probabilidadIncendio-probabilidadIncendio/3) == 1; //202
            } else {
                seEnciende = probabilidad.nextInt(probabilidadIncendio) == 1; //202
            }
            if (seEnciende) {
                encenderLlama(fx, altura-1);
            }
        }
    }

    private void propagarPixeles() {
        for (int fx = 0; fx < ancho; fx++) {
            for (int cy = altura - 1; cy >= 0; cy--) {
                if (cy != altura -1) {
                    calcularPunto(fx, cy);
                }
            }
        }
    }

    private void enfriarPixeles() {
        //Aplicamos una cantidad de enfriamiento global
        for (int fx = 0; fx < ancho; fx++) {
            for (int cy = 0; cy < altura; cy++) {
                if (cy > altura/2 && (pixels[fx][cy]-10) >= 0) pixels[fx][cy] -= gas - 0.3;
                else pixels[fx][cy] = 0;
            }
            for (int cy = 0; cy < altura; cy++) {
                if (cy > altura/2 && (pixels[fx][cy]-10) >= 0) pixels[fx][cy] -= gas;
                else pixels[fx][cy] = 0;
            }
        }
    }

    private void encenderLlama(int x, int y) {
        //Llenamos la base de la llama con varios pixeles para aumentar la propagacion
                                         pixels[x][y] = 255;
        if (x - 1 >= 0){                 pixels[x - 1][y] = 230; }
        if (x - 2 >= 0){                 pixels[x - 2][y] = 200; }
        if (x + 1 <= pixels.length-1){   pixels[x + 1][y] = 230; }
        if (x + 2 <= pixels.length-1){   pixels[x + 2][y] = 200; }

    }

    private void calcularPunto(int x, int y) {
        //Calculamos la media de la base sobre el punto con la cantidad maxima de posiciones posibles (max 5)
        int posiciones = 0;
        if      (x >= 2 && x < pixels.length-3){ posiciones = 5; }
        else if (x >= 1 && x < pixels.length-2){ posiciones = 3; }
        else if (x >= 0 && x < pixels.length-1){ posiciones = 1; }

        //Aplicamos al algoritmo sobre el punto teniendo en cuenta el numero de posiciones
        int media = 0;
        if      (posiciones == 5) { media = (pixels[x][y+1] + pixels[x-1][y+1] + pixels[x-2][y+1] + pixels[x+1][y+1] + pixels[x+2][y+1])/5; }
        else if (posiciones == 3) { media = (pixels[x][y+1] + pixels[x-1][y+1] + pixels[x+1][y+1])/3; }
        else if (posiciones == 1) { media = (pixels[x][y+1]/2); }

        //Asignamos la media calculada al punto en cuestion
        pixels[x][y] = media;
    }

    private Color obtenerColorDeTemperatura(int cents) {
        return paleta.obtenerColorPorTemperatura(cents);
    }

    public void dibujar(Graphics g) {
        for (int fx = 0; fx < ancho; fx++) {
            for (int cy = 0; cy < altura; cy++) {
                imagen.setRGB(fx, cy, obtenerColorDeTemperatura(pixels[fx][cy]).getRGB());
            }
        }
        g.drawImage(imagen, x, y, null);
    }

    //------------------------------------------------------------------------------------------------------------THREAD

    @Override
    public void run() {
        super.run();
        while (true) {
            this.actualizar();

            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //---------------------------------------------------------------------------------------------------------------DTO

    public void setGas(double gas) {
        this.gas = gas;
        if      (gas*100 < 50) { this.probabilidadIncendio = (int) (Math.abs(gas*230)); }
        else if (gas*100 < 75) { this.probabilidadIncendio = (int) (Math.abs(gas*250));                     }
        else if (gas*100 < 90) { this.probabilidadIncendio = (int) (Math.abs(Math.pow((gas*100),gas*1.8))); }
        System.out.println(probabilidadIncendio);
    }
}
