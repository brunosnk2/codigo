package com.example.a0070149.trabalho4;

import java.io.Serializable;

/**
 * Created by 0070149 on 04/12/2017.
 */

public class Sensor implements Serializable {

    int id;
    String nome;
    String valor;

    public Sensor(String nome, String valor) {
        this(0, nome, valor);
    }
    public Sensor(int id, String nome, String valor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;

    }



    @Override
    public int hashCode() {
        return id;
    }

}
