public abstract class Pessoa{
    private String nome;
    private String email;
    private String senha;
    private int idade;
    private final char sexo;

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome=nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail( String email ){
        this.email=email;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha( String senha ){
        this.senha=senha;
    }

    public int getIdade(){
        return idade;
    }

    public void setIdade( int idade ){
        this.idade=idade;
    }

    public char getSexo(){
        return sexo;
    }

    public void setSexo( char sexo ){
        this.sexo=sexo;
    }

    public Pessoa( String nome, String email, String senha, int idade, char sexo){
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setIdade(idade);
        setSexo(sexo);
    }
}
