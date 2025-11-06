/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

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

    public Cavaleiro(String nome) {
        super(nome, 100, 15);
        this.dinheiro = 0;
        
        // Inicializa os novos campos
        this.escudoDeTrocaAtivo = false;
        this.duracaoEscudo = 0;
    }

    public void atacar(EntidadeJogo alvo) {
        if (!alvo.estaVivo()) return;

        System.out.println(this.getNome() + " ataca " + alvo.getNome() + " com sua Espada Doce!");
        alvo.receberDano(this.getForcaAtaque());
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