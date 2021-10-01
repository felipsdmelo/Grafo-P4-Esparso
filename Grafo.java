import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Grafo {
    // mapa de vértices por ID
    private Map<Integer, Vertice> verticeMap;
    private List<Integer> indicesDeVertices;

    public Grafo() {
        this.verticeMap = new HashMap<Integer, Vertice>();
        this.indicesDeVertices = new ArrayList<>();
    }

    public void adicionarVertice(int id) {
        // verifica validade do ID
        if (this.verticeMap.get(id) == null || !this.verticeMap.containsKey(id)) {
            Vertice vertice = new Vertice(id);
            this.verticeMap.put(vertice.getId(), vertice);
            this.indicesDeVertices.add(id);
        }
        else {
            System.out.println("Não foi possível adicionar o vértice " + id);
        }
    }

    public void deletarVertice(int id) {
        Vertice vertice = this.verticeMap.get(id);
        for (Vertice v : this.verticeMap.values())
            v.deletarVizinho(vertice);
        this.verticeMap.remove(vertice);
        this.indicesDeVertices.remove(vertice.getId());
    }

    public void adicionarAresta(int id_1, int id_2) {
        Vertice v1 = this.verticeMap.get(id_1);
        Vertice v2 = this.verticeMap.get(id_2);
        // verifica validade dos IDs
        if (v1 == null || v2 == null) {
            System.out.println("Não foi possível criar a aresta entre os vértices " + id_1 + " e " + id_2);
            return;
        }
        if (v1.possuiVizinho(v2)) {
            System.out.println("Os vértices " + id_1 + " e " + id_2 + " já são vizinhos");
            return;
        }
        v1.adicionarVizinho(v2);
        v2.adicionarVizinho(v1);
    }

    public void deletarAresta(int id_1, int id_2) {
        Vertice v1 = this.verticeMap.get(id_1);
        Vertice v2 = this.verticeMap.get(id_2);
        if (v1 == null || v2 == null) {
            System.out.println("Não foi possível deletar aresta entre os vértices " + id_1 + " e " + id_2);
            return;
        }
        if (v1.possuiVizinho(v2)) {
            v1.deletarVizinho(v2);
            v2.deletarVizinho(v1);
        }
    }

    public void reset() {
        for (Vertice v : this.verticeMap.values()) {
            v.setVisitadoFalse();
        }
    }

    public void imprimirGrafo() {
        for (Vertice v : this.verticeMap.values())
            v.imprimir();

    }

    public Grafo copiarGrafo() {
        Grafo g = new Grafo();
        for (Vertice v : this.verticeMap.values())
            g.adicionarVertice(v.getId());
        for (Vertice v : this.verticeMap.values()) {
            for (Vertice v1 : v.getVizinhanca().values())
                g.adicionarAresta(v.getId(), v1.getId());
        }
        return g;
    }

    public void DFS(int id) {
        Vertice v = this.verticeMap.get(id);
        if (v.isVisitado())
            return;
        v.setVisitadoTrue();
        System.out.println("Vértice " + id + " visitado");
        for (int indice : v.getVizinhanca().keySet()) {
            DFS(indice);
        }
    }

    public int BFS(int id) {
        reset();
        Vertice v = this.verticeMap.get(id);
        v.setVisitadoTrue();
        int i = 1;
        Queue<Vertice> fila = new LinkedList<>();
        fila.add(v);
        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();
            atual.setVisitadoTrue();
            for (Vertice vizinho : atual.getVizinhanca().values()) {
                if (!vizinho.isVisitado()) {
                    fila.add(vizinho);
                    // System.out.printf("Vértice %d visitado\n", vizinho.getId());
                    vizinho.setVisitadoTrue();
                    i += 1;
                }
            }
        }
        return i;
    }

    public void abrirTexto(String arquivo) { // parser de arquivos para grafo
        String linha = null;
        String pedacos[];

        try {
            FileReader arquivoEntrada = new FileReader(arquivo);
            BufferedReader b = new BufferedReader(arquivoEntrada);
            while ((linha = b.readLine()) != null) {
                // retira excessos de espaços em branco
                linha = linha.replaceAll("\\s + ", " ");
                pedacos = linha.split(" ");
                int v1 = Integer.parseInt(pedacos[0]);
                if (this.verticeMap.get(v1) == null) // evitar aviso desnecessario de vertice ja existente
                    this.adicionarVertice(v1);
                for (int i = 2 ; i < pedacos.length ; i++) {
                    int v2 = Integer.parseInt(pedacos[i]);
                    // pode ser a primeira ocorrência do v2
                    if (this.verticeMap.get(v2) == null) // evitar aviso desnecessario de vertice ja existente
                        this.adicionarVertice(v2);
                    this.adicionarAresta(v1, v2);
                }
            }
            System.out.print("\nArquivo lido com sucesso.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int fatorial(int n) {
        if (n >= 0) {
            int fat = 1;
            while (n >= 1) {
                fat *= n;
                n -= 1;
            }
            return fat;
        }
        return -1;
    }

    public int combinacaoSimples(int n, int k) {
        return fatorial(n) / (fatorial(k) * fatorial(n - k));
    }

    /**
     * Método que a partir de um grafo g, retorna todos os
     * subgrafos de tamanho 5 de g
     * Complexidade: O(n^5)
     *
     * @return lista com todos os subgrafos de tamanho 5 de g
     */
    public List<Grafo> subgrafosPossiveis() {
        List<Grafo> subgrafos = new ArrayList<>();
        for (Vertice v1 : this.verticeMap.values()) {
            for (Vertice v2 : this.verticeMap.values()) {
                for (Vertice v3 : this.verticeMap.values()) {
                    for (Vertice v4 : this.verticeMap.values()) {
                        for (Vertice v5 : this.verticeMap.values()) {
                            // Verifica se os vértices são todos diferentes entre si
                            if (v1.getId() < v2.getId() && v1.getId() < v3.getId() && v1.getId() < v4.getId() &&
                                    v1.getId() < v5.getId() && v2.getId() < v3.getId() && v2.getId() < v4.getId() &&
                                    v2.getId() < v5.getId() && v3.getId() < v4.getId() && v3.getId() < v5.getId() && v4.getId() < v5.getId()) {
                                Grafo g = new Grafo();
                                g.adicionarVertice(v1.getId());
                                g.adicionarVertice(v2.getId());
                                g.adicionarVertice(v3.getId());
                                g.adicionarVertice(v4.getId());
                                g.adicionarVertice(v5.getId());

                                // Adiciona as arestas entre os vértices que são vizinhos
                                if (v1.getVizinhanca().containsValue(v2))
                                    g.adicionarAresta(v1.getId(), v2.getId());
                                if (v1.getVizinhanca().containsValue(v3))
                                    g.adicionarAresta(v1.getId(), v3.getId());
                                if (v1.getVizinhanca().containsValue(v4))
                                    g.adicionarAresta(v1.getId(), v4.getId());
                                if (v1.getVizinhanca().containsValue(v5))
                                    g.adicionarAresta(v1.getId(), v5.getId());
                                if (v2.getVizinhanca().containsValue(v3))
                                    g.adicionarAresta(v2.getId(), v3.getId());
                                if (v2.getVizinhanca().containsValue(v4))
                                    g.adicionarAresta(v2.getId(), v4.getId());
                                if (v2.getVizinhanca().containsValue(v5))
                                    g.adicionarAresta(v2.getId(), v5.getId());
                                if (v3.getVizinhanca().containsValue(v4))
                                    g.adicionarAresta(v3.getId(), v4.getId());
                                if (v3.getVizinhanca().containsValue(v5))
                                    g.adicionarAresta(v3.getId(), v5.getId());
                                if (v4.getVizinhanca().containsValue(v5))
                                    g.adicionarAresta(v4.getId(), v5.getId());

                                subgrafos.add(g); // adicionando o subgrafo à lista
                            }
                        }
                    }
                }
            }
        }
        return subgrafos;
    }

    public boolean isP4Esparso() {
        if (this.verticeMap.size() <= 4)
            return true;

        List<Grafo> subgrafos = subgrafosPossiveis();
        for (Grafo g : subgrafos) {
            int controle = 0;
            for (Vertice v1 : g.verticeMap.values()) {
                // verificar se o BFS de v é >= 4
                // se sim, controle += 1
                // se controle > 1, return false
                for (Vertice v2 : g.verticeMap.values()) {
                    if (BFS(v1.getId()) >= 4)
                        controle += 1;
                    if (controle > 1)
                        return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        //List<Grafo> listaSubgrafos = new ArrayList<>();

        /*
        grafo.adicionarVertice(1);
        grafo.adicionarVertice(2);
        grafo.adicionarVertice(3);
        grafo.adicionarVertice(4);
        grafo.adicionarVertice(5);
        grafo.adicionarVertice(6);

        grafo.adicionarAresta(1, 5);
        grafo.adicionarAresta(2, 3);
        grafo.adicionarAresta(4, 5);
        grafo.adicionarAresta(4, 6);
        grafo.adicionarAresta(5, 6); */

        grafo.adicionarVertice(1);
        grafo.adicionarVertice(2);
        grafo.adicionarVertice(3);
        grafo.adicionarVertice(4);
        grafo.adicionarVertice(5);

        grafo.adicionarAresta(1, 5);
        grafo.adicionarAresta(2, 5);
        grafo.adicionarAresta(3, 5);
        grafo.adicionarAresta(4, 5);

        List<Grafo> listaSubgrafos = grafo.subgrafosPossiveis();
        for (Grafo g : listaSubgrafos) {
            g.imprimirGrafo();
            System.out.print("\n-------------\n\n");
        }

        System.out.print("\n=====================================================\n\n");
        grafo.imprimirGrafo();
        System.out.println();

        grafo.reset();

        System.out.println(grafo.isP4Esparso());

        for (Grafo g : listaSubgrafos) {
            for (Vertice v : g.verticeMap.values())
                System.out.println(grafo.BFS(v.getId()));
        }
    }
}
