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
        } /*
        if (v1.possuiVizinho(v2)) {
            System.out.println("Os vértices " + id_1 + " e " + id_2 + " já são vizinhos");
            return;
        } */
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
        int c = 1;
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
                    c += 1;
                }
            }
        }
        return c;
    }

    /**
     * Dados dois inteiros origem e destino, calcula a quantidade
     * de vértices entre eles
     *
     * @param origem: ID do vértice de origem
     * @param destino: ID do vértice de destino
     * @return: quantidade de vértices de origem para destino (inclusive)
     */
    public int BFS(int origem, int destino) {
        reset();
        Vertice v1 = this.verticeMap.get(origem);
        List<Integer> caminho = new ArrayList<>();
        caminho.add(origem);
        v1.setVisitadoTrue();
        Queue<List<Integer>> fila = new LinkedList<>();
        fila.offer(caminho);

        while (!fila.isEmpty()) {
            caminho = fila.poll();
            int ultimo = caminho.get(caminho.size() - 1);
            if (ultimo == destino) {
                return caminho.size();
            }

            Vertice ultimoVertice = this.verticeMap.get(ultimo);
            for (Vertice v : ultimoVertice.getVizinhanca().values()) {
                if (!v.isVisitado()) {
                    List<Integer> novoCaminho = new ArrayList<>(caminho);
                    novoCaminho.add(v.getId());
                    fila.offer(novoCaminho);
                    v.setVisitadoTrue();
                }
            }
        }
        return caminho.size();
    }

    public void abrirTexto(String arquivo) {
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

                if (this.verticeMap.get(v1) == null) // evitar aviso desnecessário de vértice já existente
                    adicionarVertice(v1);

                for (int i = 2 ; i < pedacos.length ; i++) {
                    int v2 = Integer.parseInt(pedacos[i]);
                    // pode ser a primeira ocorrência de v2
                    if (this.verticeMap.get(v2) == null)
                        adicionarVertice(v2);
                    adicionarAresta(v1, v2);
                }
            }
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Método que a partir de um grafo g, retorna todos os
     * subgrafos de tamanho 5 de g
     * Complexidade: O(n^5)
     *
     * @return: lista com todos os subgrafos de tamanho 5 de g
     */
    public List<Grafo> gerarSubgrafos() {
        List<Grafo> subgrafos = new ArrayList<>();
        for (Vertice v1 : this.verticeMap.values()) {
            for (Vertice v2 : this.verticeMap.values()) {
                for (Vertice v3 : this.verticeMap.values()) {
                    for (Vertice v4 : this.verticeMap.values()) {
                        for (Vertice v5 : this.verticeMap.values()) {
                            // Verifica se os vértices são todos diferentes entre si, evitando repetição de {v1, v2} e {v2, v1}
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

    /**
     * Um grafo é P4-Esparso se para todos os subgrafos de 5 vértices existe
     * no máximo um P4 induzido
     *
     * @return: true caso seja P4-Esparso ou false caso contrário
     */
    public boolean isP4Esparso() {
        if (this.verticeMap.size() <= 4) // qualquer grafo com 4 ou menos vértices é P4-Esparso
            return true;

        List<Grafo> subgrafos = gerarSubgrafos();
        for (Grafo g : subgrafos) {
            for (Vertice v1 : g.verticeMap.values()) {
                int controle = 0;
                for (Vertice v2 : g.verticeMap.values()) {
                    if (v1.getId() < v2.getId()) { // evita repetição {v1, v2} e {v2, v1}
                        if (BFS(v1.getId(), v2.getId()) >= 4)
                            controle += 1;
                        if (controle > 1)
                            return false;
                    }
                }
            }
        }
        return true;
    }
