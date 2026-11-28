package model;

import exception.DadoInvalidoException;

public class Tecnico extends Pessoa {
    private String equipeAtual;

    public Tecnico(String nome, String email, String senha, int idade, char sexo, String equipeAtual)
            throws DadoInvalidoException {
        super(nome, email, senha, idade, sexo);
        setEquipeAtual(equipeAtual);
    }

    @Override
    public String getTipoPerfil() {
        return "Técnico";
    }

    public String getEquipeAtual() {
        return equipeAtual;
    }

    public void setEquipeAtual(String equipeAtual) {
        this.equipeAtual = equipeAtual;
    }
}