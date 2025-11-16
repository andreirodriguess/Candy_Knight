/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package candyknight.Entidades;

/**
 *
 * @author felip
 */
// Cavaleiro "É UMA" EntidadeJogo
public class Cavaleiro extends EntidadeJogo {

    private int dinheiro;

    // NOVOS CAMPOS PARA O ESCUDO
    private boolean escudoDeTrocaAtivo;
    private int duracaoEscudo;
    
    private int potenciaBase; // Guarda o dano original (15)
    private int durabilidadeArma;

    public int getDurabilidadeArma() {
        return durabilidadeArma;
    }

    public Cavaleiro(String nome) {
        super(nome, 100, 15);
        this.dinheiro = 0;
        
        // Inicializa os novos campos
        this.escudoDeTrocaAtivo = false;
        this.duracaoEscudo = 0;
        
        this.potenciaBase = this.getPotencia(); // Guarda o valor 15
        this.durabilidadeArma = 0; // Começa com 0 (desarmado)
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " ataca " + alvo.getNome() + " com sua Espada Doce!");
        alvo.receberDano(this.getPotencia());
    }

    public void morrer() {
        System.out.println(this.getNome() + " foi derrotado! Fim de jogo...");
    }

    public void coletarDinheiro(int quantidade) {
        this.dinheiro += quantidade;
        System.out.println(this.getNome() + " coletou " + quantidade + " moedas!");
    }

    // --- MÉTODOS NOVOS PARA O ESCUDO ---

    /**
     * Ativa o efeito do escudo de troca.
     * Chamado pelo item EscudoDeGoma.
     * @param duracao O número de movimentos que o efeito durará.
     */
    public void ativarEscudoDeTroca(int duracao) {
        this.escudoDeTrocaAtivo = true;
        this.duracaoEscudo = duracao;
        System.out.println(this.getNome() + " sente uma energia elástica ao seu redor!");
    }

    /**
     * Deve ser chamado a cada movimento/turno do jogador.
     * Reduz a duração dos buffs ativos.
     */
    public void decrementarDuracaoBuffs() {
        if (this.escudoDeTrocaAtivo) {
            this.duracaoEscudo--;
            if (this.duracaoEscudo <= 0) {
                this.escudoDeTrocaAtivo = false;
                System.out.println("O efeito do Escudo de Goma passou!");
            }
        }
    }
    
    /**
     * Verifica se o escudo de troca está ativo.
     * @return true se o jogador pode trocar de lugar, false caso contrário.
     */
    public boolean isEscudoDeTrocaAtivo() {
        return this.escudoDeTrocaAtivo;
    }
    
    public void setDurabilidadeArma(int durabilidade) {
        this.durabilidadeArma = durabilidade;
        System.out.println("A espada parece ter " + this.durabilidadeArma + " usos restantes.");
    }
    
    public void decrementarDurabilidadeArma() {
        // Só faz sentido decrementar se o jogador está armado
        if (this.getArmado()) {
            
            this.durabilidadeArma--; // Reduz um uso
            
            if (this.durabilidadeArma <= 0) {
                // A arma quebrou!
                System.out.println("A Espada de Alcaçuz se quebrou com o impacto!");
                this.setArmado(false); // Jogador está desarmado
                this.setPotencia(this.potenciaBase); // Reverte para o dano base (15)
                System.out.println(this.getNome() + " está desarmado! Ataque revertido para " + this.potenciaBase + ".");
            } else {
                // A arma ainda está boa
                System.out.println("Usos restantes da espada: " + this.durabilidadeArma);
            }
        }
    }
}