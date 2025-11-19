/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coletaveis;

import Entidades.Cavaleiro;

/**
 *
 * @author felip
 */
public class EscudoDeGoma implements Coletavel {
    
    private int duracaoEmMovimentos;

    // Construtor com nível
    public EscudoDeGoma(int nivel) {
        // Duração base de 2 movimentos.
        // A cada 5 níveis, ganha +1 turno de duração.
        this.duracaoEmMovimentos = 2 + (nivel / 5);
    }

    // Construtor padrão
    public EscudoDeGoma() {
        this(0);
    }

    @Override
    public String getNome() {
        return "Escudo de Goma (Troca)";
    }

    @Override
    public void usar(Cavaleiro cavaleiro) {
        System.out.println(cavaleiro.getNome() + " equipou o " + this.getNome() + "!");
        System.out.println("Pode trocar de lugar com monstros por " + duracaoEmMovimentos + " movimentos!");
        
        cavaleiro.ativarEscudoDeTroca(duracaoEmMovimentos);
    }
}