/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.candyknight;

/**
 *
 * @author felip
 */
public abstract class MonstroDoce extends EntidadeJogo {

    private int recompensaEmDinheiro;

    public MonstroDoce(String nome, int vidaMax, int forca, int recompensa) {
        super(nome, vidaMax, forca);
        this.recompensaEmDinheiro = recompensa;
    }

    public int getRecompensaEmDinheiro() {
        return recompensaEmDinheiro;
    }
}