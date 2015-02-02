
import java.util.Scanner;

public class BuscaMinas {

    // Estado en el que se encuentra la partida --> 0 = jugando, 1 = perdido, 2 = ganado;
    public static final int ESTADO = 0; 
    public static final char LUG = '_';

    //Cantidad de casillas vistas
    public static int cvistas = 0;
    
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        infoUs("1. Nivel principiante --> 8x8 casillas, 10 minas");
        infoUs("2. Nivel intermedio --> 10x10 casillas, 40 minas");
        infoUs("3. Nivel experto --> 16x30 casillas, 99 minas");
        System.out.print("Introduce opción: ");
        int sel = teclado.nextInt();
        
        int dim[] = creaTabs(sel);
        
        //Tableros de juego
       int tablero[][] = new int[dim[0]][dim[1]];
       char visible[][] = new char[dim[0]][dim[1]];
       
        initTab(visible, tablero);
        
        muesTab(visible);

    }

    public static void infoUs(String mensaje) {
        for (int i = 0; i < 15; i++) {
            System.out.print("*");
            if (i == 14) {
                System.out.println();
            }
        }
        System.out.println(mensaje);
        for (int i = 0; i < 15; i++) {
            System.out.print("*");
            if (i == 14) {
                System.out.println();
            }
        }
    }
    /**
     * 
     * Es necesario utilizar esta función mediante control de errores (try-catch)
     * @param slc - Segun selección del usario devuelve dimensiones diferentes;
     * @return - Vector con valores para crear el tablero segun dificultad 
     */
    public static int[] creaTabs(int slc) {
        //Vector con dimensiones
        int[] dimTab = new int[3];

        switch (slc) {

            case 1:
                dimTab[0] = 8;
                dimTab[1] = 8;
                dimTab[2] = 10;
                break;
            case 2:
                dimTab[0] = 10;
                dimTab[1] = 10;
                dimTab[2] = 40;
                break;
            case 3:
                dimTab[0] = 16;
                dimTab[1] = 30;
                dimTab[2] = 99;
                break;
            default:
                throw new AssertionError("Error dificultad");
        }

        return dimTab;
    }
    /**
     * Muestra contenido de la matriz que se le pase por parametro, no sustituye caracteres
     * @param tab - Matriz a mostrar
     */
    public static void muesTab(char tab[][]){
        for (int fi = 0; fi < tab.length; fi++) {
            for (int co = 0; co < tab[0].length; co++) {
                System.out.print(tab[fi][co]+"|");
            }
            System.out.println(" "+fi);
        }
    }
    /**
     * Inicializa ambos tableros a ceros declarando la partida como acaba de comenzar
     * @param vis - Tablero visible de la partida
     * @param tab - Tablero con valores, minas y numeros alrededor;
     */
    public static void initTab(char vis[][],int tab[][]){
        for (int fil = 0; fil < tab.length; fil++) {
            for (int col = 0; col < tab[0].length; col++) {
                tab[fil][col] = 0;
                //Todas las casillas tapadas
                vis[fil][col] = LUG;
            }
        }
    }
}
