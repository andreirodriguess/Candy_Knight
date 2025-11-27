
package Coletaveis;

import Entidades.Cavaleiro;

public class Moeda implements Coletavel{

    private int valor;

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    
    @Override
    public void usar(Cavaleiro cavaleiro) {
        cavaleiro.coletarDinheiro(this.valor);
    }

    @Override
    public String getNome() {
        return "moeda";
    }
    @Override
    public void fortalecer(int nivel) {
      
    }
}