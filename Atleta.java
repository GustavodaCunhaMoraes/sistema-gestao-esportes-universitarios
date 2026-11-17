public class Atleta extends Pessoa {
    private String equipe;
    private boolean suspenso;

    public String getEquipe(){
        return equipe;
    }

    public void setEquipe(String equipe){
        this.equipe=equipe;
    }

    public boolean getSuspenso(){
        return suspenso;
    }

    public void setSuspenso(boolean suspenso){
        this.suspenso=suspenso;
    }

    public Atleta(String nome, String email, String senha, int idade, char sexo, 
        String equipe, boolean suspenso){
        setEquipe(equipe);
        setSuspenso(suspenso);
        super(nome, email, senha, idade, sexo);
    }
}
