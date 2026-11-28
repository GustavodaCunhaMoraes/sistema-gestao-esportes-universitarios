package model;

import exception.DadoInvalidoException;

public class Coordenador extends Pessoa {
    public Coordenador(String nome, String email, String senha, int idade, char sexo)
            throws DadoInvalidoException {
        super(nome, email, senha, idade, sexo);
    }

    @Override
    public String getTipoPerfil() {
        return "Coordenador Esportivo";
    }
}