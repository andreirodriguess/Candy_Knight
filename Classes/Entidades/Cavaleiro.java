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

    public Cavaleiro(String nome) {
        // O valor 15 aqui é o dano base (desarmado), que usaremos na lógica de 'desarmado'
        super(nome, 100, 15);
        this.dinheiro = 0;
        
        // Inicializa os novos campos
        this.escudoDeTrocaAtivo = false;
        this.duracaoEscudo = 0;
        
        this.potenciaBase = this.getPotencia(); // Guarda o valor 15 (dano desarmado)
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        // Esta lógica só é chamada se o jogador está ARMADO (controlado pela GameLogic)
        System.out.println(this.getNome() + " ataca " + alvo.getNome() + " com sua Espada Doce!");

        // Lógica de "Pool de Dano"
        int danoDisponivel = this.getPotencia(); // Este é o 'pool' da arma
        int vidaMonstro = alvo.getPontosDeVidaAtuais();

        // O dano a causar é o MENOR entre o 'pool' da arma e a 'vida' do monstro
        int danoACausar = Math.min(danoDisponivel, vidaMonstro);

        alvo.receberDano(danoACausar);

        // Reduz o 'pool' de dano (que está guardado na 'potencia')
        int poolRestante = danoDisponivel - danoACausar;
        this.setPotencia(poolRestante);

        // Verifica se a arma 'quebrou' (pool esgotado)
        if (poolRestante <= 0) {
            System.out.println("A arma perdeu todo o seu poder!");
            this.setArmado(false);
            this.setPotencia(this.potenciaBase); // Reseta para o dano base desarmado
            System.out.println(this.getNome() + " está desarmado! Potência revertida para " + this.potenciaBase + ".");
        } else {
            System.out.println("Pontos de dano restantes na arma: " + poolRestante);
        }
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
}