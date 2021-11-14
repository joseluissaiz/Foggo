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
    private boolean             activo;
    private int                 probabilidadIncendio;

    // TODO: 14/11/2021 Se necesita ajustar
    private double              gas;

    // TODO: 13/11/2021 Se necesita implementar
    //private int                 factorPonderacion;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public Fuego(int x, int y, int ancho, int altura, PaletaColores paleta) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.altura = altura;
        this.paleta = paleta;
        pixels = new int[ancho][altura];
        imagen = new BufferedImage(ancho, altura, BufferedImage.TYPE_INT_ARGB);
        activo = true;
        gas = 1.5; //1.5
        probabilidadIncendio = 250; //250

    }

    //-----------------------------------------------------------------------------------------------------------GESTION

    private void actualizar() {
        encenderPixeles();
        propagarPixeles();
        enfriarPixeles();
    }

    //----------------------------------------------------------------------------------------------------------GRAFICOS

    private void encenderPixeles() {
        boolean seEnciende;
        Random probabilidad = new Random();

        for (int fx = 0; fx < ancho; fx++) {
            //Si los pixeles estan cerca del centro, hay mas posibilidades de que se enciendan
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
        if (x - 1 >= 0){                 pixels[x - 1][y] = 230; }  //Propagacion lineal
        if (x - 2 >= 0){                 pixels[x - 2][y] = 200; }  //Propagacion lineal
        if (x + 1 <= pixels.length-1){   pixels[x + 1][y] = 230; }  //Propagacion lineal
        if (x + 2 <= pixels.length-1){   pixels[x + 2][y] = 200; }  //Propagacion lineal

    }

    private void calcularPunto(int x, int y) {
        //Calculamos la media de la base sobre el punto con la cantidad maxima de posiciones posibles (max 5)
        int posiciones = 0;
        if      (x >= 2 && x < pixels.length-3){ posiciones = 5; }
        else if (x >= 1 && x < pixels.length-2){ posiciones = 3; }
        else if (x >= 0 && x < pixels.length-1){ posiciones = 1; }

        //Aplicamos al algoritmo sobre el punto teniendo en cuenta el numero de posiciones
        int media = 0;
        if (posiciones == 5) {
            media = (pixels[x][y+1] +
                     pixels[x-1][y+1] +
                     pixels[x-2][y+1] +
                     pixels[x+1][y+1] +
                     pixels[x+2][y+1] +
                     pixels[x][y]
            )/6;
        }
        else if (posiciones == 3) {
            media = (pixels[x][y+1] +
                     pixels[x-1][y+1] +
                     pixels[x+1][y+1] +
                     pixels[x][y]
            )/4;
        }
        else if (posiciones == 1) {
            media = (pixels[x][y+1] +
                     pixels[x][y] +
                     pixels[x][y]
            )/3;
        }

        //Asignamos la media calculada al punto en cuestion
        pixels[x][y] = media;
    }

    public void dibujar(Graphics g) {
        for (int fx = 0; fx < ancho; fx++) {
            for (int cy = 0; cy < altura; cy++) {
                imagen.setRGB(fx, cy, paleta.obtenerColorPorTemperatura(pixels[fx][cy]).getRGB());
            }
        }
        g.drawImage(imagen, x, y, null);
    }

    //------------------------------------------------------------------------------------------------------------THREAD

    @Override
    public void run() {
        super.run();
        while (true) {
            if (activo) {
                this.actualizar();
            }
            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //---------------------------------------------------------------------------------------------------------------DTO

    /**en pruebas*/
    public void setGas(double gas) {
        this.gas = gas;
        if      (gas*100 < 50) { this.probabilidadIncendio = (int) (Math.abs(gas*230)); }
        else if (gas*100 < 75) { this.probabilidadIncendio = (int) (Math.abs(gas*250));                     }
        else if (gas*100 < 90) { this.probabilidadIncendio = (int) (Math.abs(Math.pow((gas*100),gas*1.8))); }
    }

    public void pausar() {
        this.activo = false;
    }

    public void reanudar() {
        this.activo = true;
    }

}
