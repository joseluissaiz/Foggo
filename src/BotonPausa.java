import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class BotonPausa extends JButton {
    private ControlPanel cPanel;
    private ImageIcon pauseImg;
    private ImageIcon resumeImg;
    private boolean isPaused;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public BotonPausa(ControlPanel cPanel) {
        this.cPanel = cPanel;
        isPaused = false;
        try {
            pauseImg = new ImageIcon(ImageIO.read(new File("src/pausar.png")));
            resumeImg = new ImageIcon(ImageIO.read(new File("src/reanudar.png")));
            this.setIcon(pauseImg);
        }catch (IOException e) {
            e.printStackTrace();
        }
        this.setOpaque(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setBackground(null);
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPaused) {
                    reanudar();
                } else {
                    pausar();
                }
            }
        });
    }

    //---------------------------------------------------------------------------------------------------------FUNCIONES

    private void pausar() {
        isPaused = true;
        this.setIcon(resumeImg);
        for (Fuego fuego:cPanel.obtenerVentana().obtenerListaFuegos()) {
            fuego.pausar();
        }
    }

    private void reanudar() {
        isPaused = false;
        this.setIcon(pauseImg);
        for (Fuego fuego:cPanel.obtenerVentana().obtenerListaFuegos()) {
            fuego.reanudar();
        }
    }

}
