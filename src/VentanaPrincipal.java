import javax.swing.JFrame;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private final int           ALTURA = 720;
    private final int           ANCHO  = 1220;
    private       Lienzo        lienzo;
    private       List<Fuego>   listaFuegos;
    private PaletaColores paletaFuego;
    private PaletaColores paletaHumo;

    //-----------------------------------------------------------------------------------------------------CONSTRUCTORES

    public VentanaPrincipal(String... titulo) {
        try {
            this.setTitle(titulo[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            this.setTitle("");
        }
        this.setResizable(false);
        this.setSize(ANCHO, ALTURA);
        this.crearFrame();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crearPaletas();
    }

    private void crearFrame() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.4F;
        c.weighty = 1.0F;
        this.add(new ControlPanel(this), c);

        c.gridx = 1;
        c.weightx = 0.6F;
        this.lienzo = new Lienzo((ANCHO-((ANCHO/10)*4)), ALTURA, this);
        this.add(lienzo, c);

    }

    private void crearPaletas() {
        paletaFuego = new PaletaColores(
                new ColorObjetivo(new Color(255,255,255,250), 255),
                new ColorObjetivo(new Color(255, 170, 0, 159), 200),
                new ColorObjetivo(new Color(165, 23, 0, 203), 150),
                new ColorObjetivo(new Color(174, 0, 0,50), 100),
                new ColorObjetivo(new Color(136, 0, 0,20), 50),
                new ColorObjetivo(new Color(136, 0, 0,10), 20),
                new ColorObjetivo(new Color(0, 0, 0,0), 0)
        );
        paletaHumo = new PaletaColores(
                new ColorObjetivo(new Color(0, 0, 0, 255), 255),
                new ColorObjetivo(new Color(0, 0, 0, 50), 100),
                new ColorObjetivo(new Color(0, 0, 0, 10), 50),
                new ColorObjetivo(new Color(0, 0, 0,0), 0)
        );
    }

    //---------------------------------------------------------------------------------------------------------------DTO

    public List<Fuego> obtenerListaFuegos() {
        return this.listaFuegos;
    }

    public Lienzo obtenerLienzo() { return this.lienzo; }

    //--------------------------------------------------------------------------------------------------------------MAIN

    public void empezarFuego() {
        this.listaFuegos = new ArrayList<>();
        this.listaFuegos.add(new Fuego(0, 0, ((ANCHO/10)*7)-20, ALTURA-37, paletaHumo));
        this.listaFuegos.add(new Fuego(0, 0, ((ANCHO/10)*7)-20, ALTURA-37, paletaFuego));

        for (Fuego fuego : listaFuegos) {
            fuego.start();
        }
    }

    public static void main(String[] args) {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.lienzo.start();
        ventana.empezarFuego();
    }

}
