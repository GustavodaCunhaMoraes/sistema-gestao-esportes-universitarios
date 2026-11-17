public class Tecnico extends Pessoa {
    private String equipe;

    public String getEquipe(){
        return equipe;
    }

    public void setEquipe(String equipe){
        this.equipe=equipe;
    }

     public Tecnico(String nome, String email, String senha, int idade, char sexo, String equipe){
        setEquipe(equipe);
        super(nome, email, senha, idade, sexo);
    }
}
