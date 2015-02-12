
import java.util.Scanner;

public class BuscaMinas {

    // Estado en el que se encuentra la partida --> 0 = jugando, 1 = perdido, 2 = ganado;
    public static final int ESTADO = 0;
    public static final char LUG = '_';
    public static final char BMB = 'X';
    public static final char BND = 'P';

    //Cantidad de casillas vistas
    public static int cvistas = 0;

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        infoUs("BUSCAMINAS");
        infoUs("1. Nivel principiante --> 8x8 casillas, 10 minas");
        infoUs("2. Nivel intermedio --> 10x10 casillas, 40 minas");
        infoUs("3. Nivel experto --> 16x30 casillas, 99 minas");
        System.out.print("Introduce opción: ");
        int sel = teclado.nextInt();

        int dim[] = creaTabs(sel);

        //Tableros de juego
        boolean tablero[][] = new boolean[dim[0] + 1][dim[1] + 1];
        char visible[][] = new char[dim[0] + 1][dim[1] + 1];

        initTab(visible, tablero);

        muesTab(visible);

        minadoTab(tablero, dim);

        int salir = 0;
        do {
            infoUs("- MENU -");
            System.out.println("1. Destapar");
            System.out.println("2. Poner/Quitar Bandera (P)");
            System.out.println("3. Finalizar juego");
            System.out.print("¿Qué desea hacer? : ");
            int opc = teclado.nextInt();

            switch (opc) {
                case 1:

                    infoUs("Introduzca fila:");
                    int fi1 = teclado.nextInt();
                    infoUs("Introduzca columna:");
                    int co1 = teclado.nextInt();

                    break;
                case 2:
                    infoUs("Introduzca fila:");
                    int fi2 = teclado.nextInt();
                    infoUs("Introduzca columna:");
                    int co2 = teclado.nextInt();

                    ponerBand(fi2, co2, visible, dim);

                    muesTab(visible);

                    break;
                case 3:
                    salir = 1;
                    break;
                default:
                    System.err.println("Opción incorrecta");
            }
        } while (salir != 1);
        

    }

    public static void ponerBand(int fil, int col, char vis[][], int dim[]) {
        //Posición correcta
        if (correctaSN(fil, col, dim)) {
            //Si ya hay bandera quitala
            if (vis[fil][col] == BND) {
                vis[fil][col] = LUG;
            } else {
                //Si no hay ponla
                vis[fil][col] = BND;
            }
        } else {
            infoUs("Posición incorrecta");
        }
    }
    
    public static boolean abrirPos(int fil,int col,boolean tab[][],char vis[][], int dim[]){
        boolean esMina = false;
        
        if (correctaSN(fil, col, dim)) {
            if (tab[fil][col]) {
                esMina = true;
            }else{
                System.out.println("Recursiva");
            }
        }else{
            return false;
        }
        return esMina;
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
     * Es necesario utilizar esta función mediante control de errores
     * (try-catch)
     *
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
     * Muestra contenido de la matriz que se le pase por parametro, no sustituye
     * caracteres
     *
     * @param tab - Matriz a mostrar
     */
    public static void muesTab(char tab[][]) {
        System.out.print("    ");//4Espacios
        for (int i = 1; i < tab[0].length; i++) {
            System.out.printf("%-2d", i);
        }
        System.out.println();
        for (int fi = 1; fi < tab.length; fi++) {
            System.out.printf("%-3d|", fi);
            for (int co = 1; co < tab[0].length; co++) {
                System.out.print(tab[fi][co] + "|");
            }
            System.out.println(" " + fi);
        }
    }

    /**
     * Inicializa ambos tableros a ceros declarando la partida como acaba de
     * comenzar
     *
     * @param vis - Tablero visible de la partida
     * @param tab - Tablero con valores, minas y numeros alrededor;
     */
    public static void initTab(char vis[][], boolean tab[][]) {
        for (int fil = 0; fil < tab.length; fil++) {
            for (int col = 0; col < tab[0].length; col++) {
                tab[fil][col] = false;
                //Todas las casillas tapadas
                vis[fil][col] = LUG;
            }
        }
    }

    public static void minadoTab(boolean tab[][], int cBomb[]) {

        int fil = cBomb[0];
        int col = cBomb[1];
        int cntBomb = cBomb[2];
        int aleFi, aleCo;

        do {
            aleFi = (int) (Math.random() * (fil - 1));
            aleCo = (int) (Math.random() * (col - 1));
            tab[aleFi][aleCo] = true;
            cntBomb--;
        } while (cntBomb > 0 && tab[aleFi][aleCo] == true);

    }

    /**
     * Función para evaluar si la posición introducida es correcta o no.
     * @param fil Fila donde se desea realizar acción
     * @param col Columna donde se desea realizar acción
     * @param dim Vector de dimensiónes proveniente de la dificultad seleccionada
     * --> [0]= fila, [1] = columna, [2] = cantidad bombas
     * @return Valor boleano. True en caso de ser correcto
     */
    public static boolean correctaSN(int fil, int col, int dim[] ) {

        boolean correcto;

        if (fil > dim[0] && col > dim[1]) {
            correcto = false;
        } else if (fil <= 0 && col <= 0) {
            correcto = false;
        } else {
            correcto = true;
        }

        return correcto;
    }
    
    public static boolean minadoSN(int fil, int col, int dim[], boolean tab[][]){
        boolean minado = false;
        
        if (correctaSN(fil, col, dim)) {
            if (tab[fil][col]) {
                minado = true;
            }
        }else{
            return false;
        }
        
        return minado;
    }

    //Recursiva

    public static void destapar(boolean tab[][], char vis[][], int fil, int col, int dim[]) {
        //caso base
        if (correctaSN(fil, col, dim) == false) {

        }else{
            vis[fil][col]= (char)contBmbAlr(fil, col, tab);
            destapar(tab, vis, fil, col + 1, dim);
            destapar(tab, vis, fil, col - 1, dim);
            destapar(tab, vis, fil - 1, col, dim);
            destapar(tab, vis, fil + 1, col, dim);
            destapar(tab, vis, fil + 1, col + 1, dim);
            destapar(tab, vis, fil + 1, col - 1, dim);
            destapar(tab, vis, fil - 1, col + 1, dim);
            destapar(tab, vis, fil - 1, col + 1, dim);
        }
    }

    public static int contBmbAlr(int fil, int col, boolean tab[][]) {
        int totalBmb = 0;

        if (tab[fil - 1][col] == true) {
            //f-1,c
            totalBmb++;
        } else if (tab[fil - 1][col + 1] == true) {
            //f-1,c+1
            totalBmb++;
        } else if (tab[fil][col + 1] == true) {
            //f,c+1
            totalBmb++;
        } else if (tab[fil + 1][col + 1] == true) {
            //f+1,c+1
            totalBmb++;
        } else if (tab[fil + 1][col] == true) {
            //f+1,c
            totalBmb++;
        } else if (tab[fil + 1][col - 1] == true) {
            //f+1,c-1
            totalBmb++;
        } else if (tab[fil][col - 1] == true) {
            //f,c-1
            totalBmb++;
        } else if (tab[fil - 1][col - 1] == true) {
            //f-1,c-1
            totalBmb++;
        }
        return totalBmb;
    }

}
