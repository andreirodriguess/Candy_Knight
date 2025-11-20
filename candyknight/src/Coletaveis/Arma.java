/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coletaveis;

import Entidades.Cavaleiro;

abstract public class Arma implements Coletavel{

    @Override
    abstract public void usar(Cavaleiro cavaleiro);

    @Override
    abstract public String getNome();
    
    abstract public void setAtaque(int novoAtaque,Cavaleiro cavaleiro);
    abstract public int getAtaque();
    abstract public void quebrar(Cavaleiro cavaleiro);
    
}
