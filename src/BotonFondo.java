import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BotonFondo extends JButton {
    private final ControlPanel cPanel;
    private final String[] formatosValidos = new String[] {
            ".jpg", ".png", ".jpeg", ".gif",
            ".tiff", ".bmp", ".eps", ".raw",
            ".cr2", ".nef", ".orf", ".sr2"
    };

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public BotonFondo(ControlPanel cPanel) {
        super("Cambiar fondo");
        this.cPanel = cPanel;

        agregarFormato();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.decode("#1e1e1e"));
                setForeground(Color.decode("#ffffff"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setBackground(Color.decode("#0e0e0e"));
                setForeground(Color.decode("#999999"));
            }
        });

        this.addActionListener(e -> cargarImagen());
    }

    private void agregarFormato() {
        this.setBackground(Color.decode("#1e1e1e"));
        this.setForeground(Color.decode("#ffffff"));
        this.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.decode("#0e0e0e"), Color.decode("#0a0a0a"))
        );
        this.setFocusPainted(false);
    }

    //---------------------------------------------------------------------------------------------------------FUNCIONES

    private void cargarImagen() {
        FileDialog fd = new FileDialog(new JFrame());
        fd.setFile("*.jpg;*.jpeg;*.png;*.gif");
        fd.setVisible(true);
        File[] f = fd.getFiles();

        if(f.length > 0) {
            String filePath = fd.getFiles()[0].getAbsolutePath();
            String fileName = fd.getFiles()[0].getName();
            boolean formatoValido = false;
            int p = 0;
            while (!formatoValido && p < formatosValidos.length) {
                if (fileName.endsWith(formatosValidos[p])) {
                    formatoValido = true;
                } else {
                    p++;
                }
            }

            if (formatoValido) {
                try {
                    BufferedImage img = ImageIO.read(new File(filePath));
                    cPanel.obtenerVentana().obtenerLienzo().setBackground(img);
                } catch (IOException ioException) {
                    System.out.println("KGD MONMTL");
                }
            }

            System.out.println(fileName);
        }
    }

}
