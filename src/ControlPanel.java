import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ControlPanel extends JPanel {
    private final VentanaPrincipal ventana;

    //-----------------------------------------------------------------------------------------------------CONSTRUCTORES

    public ControlPanel(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.setBackground(Color.decode("#1e2b32"));
        crearEstructura();
    }

    private void crearEstructura() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.01F;
        JLabel title = new JLabel();
        title.setIcon(new ImageIcon("src/flamex_logo.png"));
        title.setHorizontalAlignment(JLabel.LEFT);
        this.add(title, gbc);

        agregarBotonPausa();
        agregarBarraGas();
        agregarBotonImagen();
    }

    //---------------------------------------------------------------------------------------------------------ELEMENTOS

    private void agregarBotonPausa() {
        GridBagConstraints gbc = new GridBagConstraints();
        BotonPausa botonPausa = new BotonPausa(this);

        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(-30,25,50,25);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.002F;
        this.add(botonPausa, gbc);
    }

    private void agregarBarraGas() {
        GridBagConstraints gbc = new GridBagConstraints();

        //Creamos el titulo de la barra
        JLabel gasTxt = new JLabel("GAS");
        gasTxt.setForeground(Color.LIGHT_GRAY);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,25,0,25);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0F;
        this.add(gasTxt, gbc);

        //Creamos la barra de gas
        GasSlider gasSlider = new GasSlider(this);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.002F;
        this.add(gasSlider, gbc);
    }

    private void agregarBotonImagen() {
        GridBagConstraints gbc = new GridBagConstraints();

        BotonFondo botonFondo = new BotonFondo(this);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(25,25,350,25);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 0.001F;
        this.add(botonFondo, gbc);

    }

    //---------------------------------------------------------------------------------------------------------------DTO

    public VentanaPrincipal obtenerVentana() { return this.ventana; }

}
