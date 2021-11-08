import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

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
        JLabel title = new JLabel("");
        title.setIcon(new ImageIcon("src/flamex_logo.png"));
        title.setHorizontalAlignment(JLabel.LEFT);
        this.add(title, gbc);

        crearBarraGas();
        crearBotonImagen();
    }

    //---------------------------------------------------------------------------------------------------------ELEMENTOS

    private void crearBarraGas() {
        GridBagConstraints gbc = new GridBagConstraints();

        //Creamos el titulo de la barra
        JLabel gasTxt = new JLabel("GAS");
        gasTxt.setForeground(Color.LIGHT_GRAY);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,25,0,25);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0F;
        this.add(gasTxt, gbc);

        //Creamos la barra de gas
        JSlider gasSlider = new JSlider(JSlider.HORIZONTAL, 25,100,75);
        gasSlider.setInverted(true);
        gasSlider.setMajorTickSpacing(25);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();

        JLabel l25 = new JLabel("100 m³/s");
        l25.setForeground(Color.WHITE);
        JLabel l50 = new JLabel("75 m³/s");
        l50.setForeground(Color.WHITE);
        JLabel l75 = new JLabel("50 m³/s");
        l75.setForeground(Color.WHITE);
        JLabel l100 = new JLabel("25 m³/s");
        l100.setForeground(Color.WHITE);

        labels.put(25, l25);
        labels.put(50, l50);
        labels.put(75, l75);
        labels.put(100, l100);
        gasSlider.setLabelTable(labels);

        gasSlider.setPaintLabels(true);
        gasSlider.setForeground(Color.WHITE);
        gasSlider.setBackground(Color.decode("#1e2b32"));
        gasSlider.setPaintTicks(true);
        gasSlider.addChangeListener(e -> {

            //Cuando cambie el estado de la barra de gas, es necesario aplicarle la conversion conveniente
            //100% = 0.0001;
            //50% = x
            //25% = 1.5;
            double gas = ((double)gasSlider.getValue()/100);

            System.out.println(gas);

            for (Fuego fuego : ventana.obtenerListaFuegos()) {
                fuego.setGas(gas);
            }
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.002F;
        this.add(gasSlider, gbc);
    }

    private void crearBotonImagen() {
        GridBagConstraints gbc = new GridBagConstraints();

        JButton botonImagen = new JButton("Cambiar fondo");
        botonImagen.setBackground(Color.decode("#1e1e1e"));
        botonImagen.setForeground(Color.decode("#ffffff"));
        botonImagen.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.decode("#0e0e0e"), Color.decode("#0a0a0a"))
        );
        botonImagen.setFocusPainted(false);

        botonImagen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                botonImagen.setBackground(Color.decode("#1e1e1e"));
                botonImagen.setForeground(Color.decode("#ffffff"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                botonImagen.setBackground(Color.decode("#0e0e0e"));
                botonImagen.setForeground(Color.decode("#999999"));
            }
        });

        botonImagen.addActionListener(e -> {
            FileDialog fd = new FileDialog(new JFrame());
            fd.setFile("*.jpg;*.jpeg;*.png;*.gif");
            fd.setVisible(true);
            File[] f = fd.getFiles();
            if(f.length > 0){
                String filePath = fd.getFiles()[0].getAbsolutePath();
                String fileName = fd.getFiles()[0].getName();
                String[] formats = new String[] {
                    ".jpg", ".png", ".jpeg", ".gif",
                    ".tiff", ".bmp", ".eps", ".raw",
                    ".cr2", ".nef", ".orf", ".sr2"
                };

                boolean formatoValido = false;
                int p = 0;
                while (!formatoValido && p < formats.length) {
                    if (fileName.endsWith(formats[p])) { formatoValido = true; } else { p++; }
                }

                if (formatoValido) {
                    try {
                        BufferedImage img = ImageIO.read(new File(filePath));
                        ventana.obtenerLienzo().setBackground(img);
                    } catch (IOException ioException) {
                        System.out.println("KGD MONMTL");
                    }
                }

                System.out.println(fileName);
            }
        });

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(25,25,350,25);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.001F;
        this.add(botonImagen, gbc);

    }

}
