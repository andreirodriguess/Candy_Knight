package candyknight; // Pacote raiz, junto com GameLogic e ControleJanela

// Importa as classes dos seus pacotes específicos
import candyknight.Entidades.EntidadeJogo;
import candyknight.Coletaveis.Coletavel;

/**
 * Representa uma única casa (célula) no tabuleiro 3x3.
 * Ela atua como um "container" que pode guardar uma entidade (jogador/monstro)
 * e/ou um item (coletável).
 */
public class Celula {

    private EntidadeJogo entidade; 
    private Coletavel item;

    public Celula() {
        this.entidade = null;
        this.item = null;
    }

    // --- Métodos de Verificação ---
    public boolean temEntidade() {
        return this.entidade != null;
    }
    
    public boolean temItem() {
        return this.item != null;
    }

    // --- Getters e Setters ---
    public EntidadeJogo getEntidade() {
        return entidade;
    }

    public void setEntidade(EntidadeJogo entidade) {
        this.entidade = entidade;
    }

    public Coletavel getItem() {
        return item;
    }

    public void setItem(Coletavel item) {
        this.item = item;
    }
    
    // --- Métodos de Limpeza ---
    public void limparEntidade() {
        this.entidade = null;
    }

    public void limparItem() {
        this.item = null;
    }
}