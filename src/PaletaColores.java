import java.awt.Color;

public class PaletaColores {
    ColorObjetivo[] paleta;
    ColorObjetivo[] colores;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public PaletaColores(ColorObjetivo... colores) {
        paleta = new ColorObjetivo[256];
        this.colores = colores;

        actualizarPaleta();
    }

    //-----------------------------------------------------------------------------------------------------------GESTION

    private void actualizarPaleta() {
        ordenarPorTemperatura();
        for (int i = 0; i < colores.length; i++) {
            if (i == colores.length-1) {
                paleta[0] = colores[colores.length-1];
            } else {
                crearTrama(colores[i], colores[i+1]);
            }
        }
    }

    private void ordenarPorTemperatura() {
        for (int i = 0; i < colores.length; i++) {
            for (int j = 0; j < colores.length; j++) {
                if (colores[i].getTemperatura() > colores[j].getTemperatura()) {
                    ColorObjetivo tmp = colores[i];
                    colores[i] = colores[j];
                    colores[j] = tmp;
                }
            }
        }
    }

    private void crearTrama(ColorObjetivo mayor, ColorObjetivo menor) {
        paleta[mayor.getTemperatura()] = mayor;
        paleta[menor.getTemperatura()] = menor;
        int[] mayorRGBA = obtenerRGBA(mayor);
        int[] menorRGBA = obtenerRGBA(menor);
        int difTemperatura = mayor.getTemperatura() - menor.getTemperatura() - 2;
        float[] difRGBA = new float[] {
                (mayorRGBA[0]-menorRGBA[0]),
                (mayorRGBA[1]-menorRGBA[1]),
                (mayorRGBA[2]-menorRGBA[2]),
                (mayorRGBA[3]-menorRGBA[3])
        };
        float[] escalonRGBA = new float[] {
                (difRGBA[0]/difTemperatura),
                (difRGBA[1]/difTemperatura),
                (difRGBA[2]/difTemperatura),
                (difRGBA[3]/difTemperatura)
        };

        for (int temp = mayor.getTemperatura()-1, paso = 1; temp > menor.getTemperatura(); temp--, paso ++) {
            int r = (int) ((escalonRGBA[0] > 0) ? mayorRGBA[0]-(escalonRGBA[0]*paso) : mayorRGBA[0]+(Math.abs(escalonRGBA[0])*paso));
            int g = (int) ((escalonRGBA[1] > 0) ? mayorRGBA[1]-(escalonRGBA[1]*paso) : mayorRGBA[1]+(Math.abs(escalonRGBA[1])*paso));
            int b = (int) ((escalonRGBA[2] > 0) ? mayorRGBA[2]-(escalonRGBA[2]*paso) : mayorRGBA[2]+(Math.abs(escalonRGBA[2])*paso));
            int a = (int) ((escalonRGBA[3] > 0) ? mayorRGBA[3]-(escalonRGBA[3]*paso) : mayorRGBA[3]+(Math.abs(escalonRGBA[3])*paso));
            paleta[temp] = new ColorObjetivo(new Color(comprobarGama(r),comprobarGama(g),comprobarGama(b),comprobarGama(a)), temp);
        }

    }

    private int[] obtenerRGBA(ColorObjetivo colorObjetivo) {
        Color color = colorObjetivo.getColor();
        return new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
    }

    private int comprobarGama(float gama) {
        return (gama > 255) ? 255 : (int) ((gama < 0) ? 0 : gama);
    }

    //---------------------------------------------------------------------------------------------------------------DTO

    public Color obtenerColorPorTemperatura(int temperatura) {
        return paleta[temperatura].getColor();
    }

    // TODO: 13/11/2021 Se necesita probar 
    public void insertarColoresObjetivo(ColorObjetivo... coloresObjetivo) {
        ColorObjetivo[] coloresCp = colores.clone();
        colores = new ColorObjetivo[coloresCp.length+coloresObjetivo.length];
        System.arraycopy(coloresCp, 0, colores, 0, coloresCp.length);
        for (int i = coloresCp.length, j = 0; i < colores.length; i++, j++) {
            colores[i] = coloresObjetivo[j];
        }
        actualizarPaleta();
    }
}
