package model;

import exception.DadoInvalidoException;

public abstract class Pessoa {
    private String nome;
    private String email;
    private String senha;
    private int idade;
    private char sexo;

    public Pessoa(String nome, String email, String senha, int idade, char sexo) throws DadoInvalidoException {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setIdade(idade);
        setSexo(sexo);
    }

    public abstract String getTipoPerfil();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws DadoInvalidoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new DadoInvalidoException("O nome não pode ser vazio.");
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws DadoInvalidoException {
        if (email == null || !email.contains("@")) {
            throw new DadoInvalidoException("E-mail inválido.");
        }
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) throws DadoInvalidoException {
        if (senha == null || senha.length() < 4) {
            throw new DadoInvalidoException("A senha deve ter pelo menos 4 caracteres.");
        }
        this.senha = senha;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) throws DadoInvalidoException {
        if (idade < 14 || idade > 100) {
            throw new DadoInvalidoException("Idade inválida para cadastro.");
        }
        this.idade = idade;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
}