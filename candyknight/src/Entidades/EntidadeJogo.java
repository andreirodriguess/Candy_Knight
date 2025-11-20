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
public abstract class EntidadeJogo {

    private String nome;
    private int pontosDeVidaMax;
    private int pontosDeVidaAtuais;
    private int potencia; //capacidade de causar dano/curar
    private boolean armado;
    

    public EntidadeJogo(String nome, int vidaMax, int potencia) {
        this.nome = nome;
        this.pontosDeVidaMax = vidaMax;
        this.pontosDeVidaAtuais = vidaMax;
        this.potencia = potencia;
        this.armado = false;
    }

    public abstract void atacar(EntidadeJogo alvo);
    public abstract void morrer();
    
    public boolean getArmado()
    {
        return this.armado;
    }
    public void receberDano(int quantidade){
        this.pontosDeVidaAtuais -= quantidade;
        System.out.println(this.nome + " recebeu " + quantidade + " de dano!");

        if (this.pontosDeVidaAtuais <= 0) {
            this.pontosDeVidaAtuais = 0;
            this.morrer(); 
        }
    }

    public String getNome() { return nome; }
    public int getPontosDeVidaAtuais() { return pontosDeVidaAtuais; }
    public int getPotencia() { return potencia; }
    public boolean estaVivo() { return this.pontosDeVidaAtuais > 0; }
    // ... (depois dos seus métodos get)

    // Necessário para PocaoDeCalda e PeDeMolequinho
    public void curar(int quantidade) {
        this.pontosDeVidaAtuais += quantidade;
        if (this.pontosDeVidaAtuais > this.pontosDeVidaMax) {
            this.pontosDeVidaAtuais = this.pontosDeVidaMax;
        }
        System.out.println(this.nome + " recuperou " + quantidade + " de vida!");
    }
    protected void setNome(String novoNome)
    {
        this.nome = novoNome;
    }
    public void setPotencia(int potencia)
    {
        this.potencia = potencia;
    }

    // Necessário para PeDeMolequinho (Fase 2)
    protected void setPontosDeVidaAtuais(int valor) {
        if (valor < 0) {
            this.pontosDeVidaAtuais = 0;
        } else if (valor > this.pontosDeVidaMax) {
            this.pontosDeVidaAtuais = this.pontosDeVidaMax;
        } else {
            this.pontosDeVidaAtuais = valor;
        }
    }

    // Necessário para PeDeMolequinho (Fase 2)
    protected void setPontosDeVidaMax(int valor) {
        this.pontosDeVidaMax = valor;
        if (this.pontosDeVidaAtuais > this.pontosDeVidaMax) {
            this.pontosDeVidaAtuais = this.pontosDeVidaMax;
        }
    }
    public void setArmado(boolean estado) {
        this.armado = estado;
    }
    
    public boolean isPlayer(){
        return false;
    }
    
    
    
}