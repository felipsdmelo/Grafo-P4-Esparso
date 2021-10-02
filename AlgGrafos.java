public class AlgGrafos {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        grafo.abrirTexto("./myfiles/grafo01.txt");
        grafo.imprimirGrafo();
        if (grafo.isP4Esparso())
            System.out.println("\nO grafo é P4-Esparso");
        else
            System.out.println("\nO grafo não é P4-Esparso");
    }
}
