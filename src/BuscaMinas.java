
import java.util.Scanner;

public class BuscaMinas {

    // Estado en el que se encuentra la partida --> 0 = jugando, 1 = perdido, 2 = ganado;
    public static final int ESTADO = 0;
    public static final char DST = '_';
    public static final char LUG = 'X';
    public static final char BMB = '*';
    public static final char BND = 'P';


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

        minarTab(tablero, dim);

        boolean salir = false;
        boolean victoria;
        
        do {
            
            //Visualiza ubicación bombas, descomentar para ver
            matrizBool(tablero);
            muesTab(visible);
            
            infoUs("- MENU -");
            System.out.println("1. Destapar");
            System.out.println("2. Poner/Quitar Bandera (P)");
            System.out.println("3. Finalizar juego");
            System.out.print("¿Qué desea hacer? : ");
            int opc = teclado.nextInt();

            boolean bomba = false;
            switch (opc) {
                case 1:

                    infoUs("Introduzca fila:");
                    int fi1 = teclado.nextInt();
                    infoUs("Introduzca columna:");
                    int co1 = teclado.nextInt();
                    
                    bomba = abrirPos(fi1, co1, tablero, visible, dim);                
//                    muesTab(visible);

                    break;
                case 2:
                    infoUs("Introduzca fila:");
                    int fi2 = teclado.nextInt();
                    infoUs("Introduzca columna:");
                    int co2 = teclado.nextInt();

                    ponerBand(fi2, co2, visible, dim);
//                    muesTab(visible);

                    break;
                case 3:
                    salir = true;
                    break;
                default:
                    System.err.println("Opción incorrecta");
            }
            
            victoria = compruebaVictor(bomba);
            
            if ( victoria == false) {
                infoUs("FIN DEL JUEGO");
                salir = true;
            }
            
        } while (salir == false &&  victoria == true);
        

    }

    public static boolean compruebaVictor(boolean posic){
        boolean victoria = true;
        
        if (posic == true ) {
            infoUs("BOOOOOOOOOOOOOOOOOM!");
            victoria = false;
        }
        return victoria;
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
        
        //No se sale del tablero
        if (correctaSN(fil, col, dim)) {
            //Si es una mina devuelve true
            if (minadoSN(fil, col, dim, tab)) {
                esMina = true;
            }else{
                //Si no es empieza recursion
                destapar(tab, vis, fil, col, dim);
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

    public static void minarTab(boolean tab[][], int cBomb[]) {

        int fil = cBomb[0];
        int col = cBomb[1];
        int cntBomb = cBomb[2];
        int aleFi, aleCo;

        do {
            aleFi = (int) (Math.random() * (fil - 1));
            aleCo = (int) (Math.random() * (col - 1));
            if (minadoSN(fil, col, cBomb, tab) == false) {
                tab[aleFi][aleCo] = true;
                cntBomb--;
            }
        } while (cntBomb > 0);

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
         // quitado redundancia
        boolean correcto = true;

        //Si es mayor o igual
        if (fil > dim[0] || col > dim[1] || fil <= 0 || col <= 0) {
            correcto = false;
        }

        return correcto;
    }
    
    public static boolean minadoSN(int fil, int col, int dim[], boolean tab[][]){
        boolean minado = false;
        
        //Comprueba que no se sale de la matriz
        if (correctaSN(fil, col, dim)) {
            //Si es una bomba retorna true
            if (tab[fil][col] == true) {
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
        
        if (correctaSN(fil, col, dim) && vis[fil][col] == LUG && minadoSN(fil, col, dim, tab) == false) {

            if (contBmbAlr(fil, col, tab,dim) == 0) {
                
                vis[fil][col] = DST;
                destapar(tab, vis, fil, col + 1, dim);
                
                destapar(tab, vis, fil, col - 1, dim);
                
                destapar(tab, vis, fil - 1, col, dim);
                
                destapar(tab, vis, fil + 1, col, dim);
                
                destapar(tab, vis, fil + 1, col + 1, dim);
                
                destapar(tab, vis, fil + 1, col - 1, dim);
                
                destapar(tab, vis, fil - 1, col + 1, dim);
                
                destapar(tab, vis, fil - 1, col + 1, dim);
                
            } else {
                int cant = contBmbAlr(fil, col, tab,dim);
                String cNum;
                
                cNum = String.valueOf(cant);
                
                System.out.println(cNum);
                vis[fil][col] = cNum.charAt(0);
                
            }
        }
    }

    public static int contBmbAlr(int fil, int col, boolean tab[][],int dim[]) {
        int totalBmb = 0;

        if (minadoSN( fil -1, col, dim, tab) == true) {
            //f-1,c
            totalBmb++;
        } else if (minadoSN( fil - 1, col + 1, dim, tab) == true) {
            //f-1,c+1
            totalBmb++;
        } else if (minadoSN(fil, col + 1, dim, tab) == true) {
            //f,c+1
            totalBmb++;
        } else if (minadoSN(fil + 1, col + 1, dim, tab) == true) {
            //f+1,c+1
            totalBmb++;
        } else if (minadoSN(fil + 1, col, dim, tab) == true) {
            //f+1,c
            totalBmb++;
        } else if (minadoSN(fil + 1, col - 1, dim, tab) == true) {
            //f+1,c-1
            totalBmb++;
        } else if (minadoSN(fil, col -1, dim, tab) == true) {
            //f,c-1
            totalBmb++;
        } else if (minadoSN(fil - 1, col - 1, dim, tab) == true) {
            //f-1,c-1
            totalBmb++;
        }
        return totalBmb;
    }
    
    public static void matrizBool(boolean matriz[][]){
        
        System.out.print("    ");//4Espacios
        for (int i = 1; i < matriz[0].length; i++) {
            System.out.printf("%-2d", i);
        }
        System.out.println("");
        for (int i = 1; i < matriz.length; i++) {
            System.out.printf("%-3d|", i);
            for (int j = 1; j < matriz[0].length; j++) {
                if (matriz[i][j] == true) {
                    System.out.print(BMB+"|");
                }else{
                    System.out.print(DST+"|");
                }
            }
            System.out.println(" "+i);
        }
    }

}
