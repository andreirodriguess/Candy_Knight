/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import Coletaveis.*;
/**
 *
 * @author felip
 */
// Cavaleiro "É UMA" EntidadeJogo
public class Cavaleiro extends EntidadeJogo {

    private int dinheiro;
    private Arma armaEquipada;

    // NOVOS CAMPOS PARA O ESCUDO
    private boolean escudoDeTrocaAtivo;
    private Coletavel armaInicial;
            
    public Cavaleiro(String nome) {
        super(nome, 10, 15);
        this.dinheiro = 0;
        
        armaInicial = new EspadaDeAlcacuz();
        armaInicial.usar(this);
        
        // Inicializa os novos campos
        this.escudoDeTrocaAtivo = false;
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
        System.out.println(this.getNome() + " sente uma energia elástica ao seu redor!");
    }
    public void desativarEscudoDeTroca() {
        this.escudoDeTrocaAtivo = false;
        System.out.println(this.getNome() + " sente uma energia elástica ao seu redor!");
    }

    
    public boolean isEscudoDeTrocaAtivo() {
        return this.escudoDeTrocaAtivo;
    }
    
    @Override
    public boolean isPlayer(){
        return true;
    }
    
    public void setArma(Arma arma){
        this.armaEquipada = arma;
    }
    
    public Arma getArma(){
        return armaEquipada;
    }
    
    public int getDinheiro(){
        return this.dinheiro;
    }
}