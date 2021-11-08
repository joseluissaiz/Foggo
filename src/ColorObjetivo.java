import java.awt.Color;

public record ColorObjetivo(Color color, int temperatura) {

    public Color getColor() {
        return color;
    }

    public int getTemperatura() {
        return temperatura;
    }
}