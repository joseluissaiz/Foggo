import javax.swing.JLabel;
import javax.swing.JSlider;
import java.awt.Color;
import java.util.Hashtable;

public class GasSlider extends JSlider {
    private final ControlPanel cPanel;


    public GasSlider(ControlPanel cPanel) {
        super(JSlider.HORIZONTAL, 25,100,75);
        this.cPanel = cPanel;
        agregarFormato();
        this.addChangeListener(e -> ajustarGas());
    }

    private void agregarFormato() {
        this.setInverted(true);
        this.setMajorTickSpacing(25);

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
        this.setLabelTable(labels);

        this.setPaintLabels(true);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.decode("#1e2b32"));
        this.setPaintTicks(true);
    }

    // TODO: 13/11/2021 Se necesita ajustar
    private void ajustarGas() {
        //Cuando cambie el estado de la barra de gas, es necesario aplicarle la conversion conveniente
        //100% = 0.0001;
        //50% = x
        //25% = 1.5;
        double gas = ((double)this.getValue()/100);

        for (Fuego fuego : cPanel.obtenerVentana().obtenerListaFuegos()) {
            fuego.setGas(gas);
        }
    }

}
