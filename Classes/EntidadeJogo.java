/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
public abstract class EntidadeJogo {

    private String nome;
    private int pontosDeVidaMax;
    private int pontosDeVidaAtuais;
    private int forcaAtaque;

    public EntidadeJogo(String nome, int vidaMax, int forca) {
        this.nome = nome;
        this.pontosDeVidaMax = vidaMax;
        this.pontosDeVidaAtuais = vidaMax;
        this.forcaAtaque = forca;
    }

    public abstract void atacar(EntidadeJogo alvo);
    public abstract void morrer();

    public void receberDano(int quantidade) {
        this.pontosDeVidaAtuais -= quantidade;
        System.out.println(this.nome + " recebeu " + quantidade + " de dano!");

        if (this.pontosDeVidaAtuais <= 0) {
            this.pontosDeVidaAtuais = 0;
            this.morrer(); 
        }
    }

    public String getNome() { return nome; }
    public int getPontosDeVidaAtuais() { return pontosDeVidaAtuais; }
    public int getForcaAtaque() { return forcaAtaque; }
    public boolean estaVivo() { return this.pontosDeVidaAtuais > 0; }
}