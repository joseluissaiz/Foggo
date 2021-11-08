import java.awt.Color;

public class PaletaColores {
    ColorObjetivo[] paleta;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR

    public PaletaColores(ColorObjetivo... colores) {
        paleta = new ColorObjetivo[256];

        for (int i = 0; i < colores.length; i++) {
            if (i == colores.length-1) {
                paleta[0] = colores[colores.length-1];
            } else {

                //Obtenemos la temperatura alta y la baja en orden descendente, y sus colores
                int temperaturaAlta = colores[i].getTemperatura();

                float altoR = colores[i].getColor().getRed();
                float altoG = colores[i].getColor().getGreen();
                float altoB = colores[i].getColor().getBlue();
                float altoA = colores[i].getColor().getAlpha();


                int temperaturaBaja = colores[i+1].getTemperatura();

                float bajoR = colores[i+1].getColor().getRed();
                float bajoG = colores[i+1].getColor().getGreen();
                float bajoB = colores[i+1].getColor().getBlue();
                float bajoA = colores[i+1].getColor().getAlpha();


                //Calculamos la diferencia de cada tono entre altos y bajos
                float difR = altoR - bajoR;
                float difG = altoG - bajoG;
                float difB = altoB - bajoB;
                float difA = altoA - bajoA;

                //De esta forma, sabemos cuanto tiene que descender el alto para llegar al bajo
                //Ahora, toca saber en cuantos pasos se debe llegar a ese numero, para ello
                //debemos sacar la diferencia de temperatura entre alto y bajo
                int pasos = (temperaturaAlta - temperaturaBaja)-2;

                //Si existe diferencia de tono, obtenemos la cantidad de tonalidad que se debe descender
                //En caso contrario, no se desciende
                float descR;
                float descG;
                float descB;
                float descA;

                if (difR == 0) {descR = 0;} else {descR = difR/pasos;}
                if (difG == 0) {descG = 0;} else {descG = difG/pasos;}
                if (difB == 0) {descB = 0;} else {descB = difB/pasos;}
                if (difA == 0) {descA = 0;} else {descA = difA/pasos;}

                //Asignamos la temperatura baja y alta a sus casillas correspondientes
                paleta[temperaturaAlta] = colores[i];
                paleta[temperaturaBaja] = colores[i+1];

                //Para cada grado de diferencia de temperatura, se le resta la cantidad descente a la
                // temperatura alta
                for (int tempIntermedia = temperaturaAlta-1; tempIntermedia > temperaturaBaja; tempIntermedia--) {
                    //Calculamos el paso en el que estamos para poder multiplicarlo por la diferencia
                    int paso = temperaturaAlta - tempIntermedia;

                    //Asignamos la temperatura intermedia con la diferencia aplicada
                    float interR = altoR-(descR*paso);
                    float interG = altoG-(descG*paso);
                    float interB = altoB-(descB*paso);
                    float interA = altoA-(descA*paso);

                    //Controlamos que los valores sean exactos y no haya rangos menores a 0.
                    if (interR < 0) interR = 0;
                    if (interG < 0) interG = 0;
                    if (interB < 0) interB = 0;
                    if (interA < 0) interA = 0;

                    paleta[tempIntermedia] = new ColorObjetivo(
                            new Color((int)interR, (int)interG, (int)interB, (int)interA),
                            tempIntermedia);
                }
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------DTO

    public Color obtenerColorPorTemperatura(int temperatura) {
        return paleta[temperatura].getColor();
    }

    public void insertarColorObjetivo(ColorObjetivo... colorObjetivo) {
        for (ColorObjetivo color: colorObjetivo) {
            this.paleta[color.getTemperatura()] = color;
        }
    }

}
