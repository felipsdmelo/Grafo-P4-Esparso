import java.util.HashMap;
import java.util.Map;

public class Vertice {
    private int id;
    private int grau;
    private Map<Integer, Vertice> vizinhanca;
    private boolean visitado = false;

    public Vertice(int id) {
        this.id = id;
        this.vizinhanca = new HashMap<>();
    }

    public int getId() {
        return this.id;
    }

    public int getGrau(int id) {
        return this.vizinhanca.size();
    }

    public Map<Integer, Vertice> getVizinhanca() {
        return vizinhanca;
    }

    public boolean isVisitado() {
        return this.visitado;
    }

    public void setVisitadoTrue() {
        if (!isVisitado())
            this.visitado = true;
    }

    public void setVisitadoFalse() {
        if (isVisitado())
            this.visitado = false;
    }

    public void adicionarVizinho(Vertice vizinho) {
        vizinhanca.put(vizinho.getId(), vizinho);
    }

    public boolean possuiVizinho(Vertice vizinho) {
        if (vizinhanca.containsValue(vizinho))
            return true;
        return false;
    }

    public void deletarVizinho(Vertice vizinho) {
        if (vizinhanca.containsValue(vizinho))
            vizinhanca.remove(vizinho);
    }

    public void imprimir() {
        System.out.print("Vértice " + getId() + ", Vizinhos: ");
        System.out.println(vizinhanca.keySet());
    }

}
