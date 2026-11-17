import java.util.ArrayList;

public class Equipe {
    private String nome;
    private ArrayList<Atleta> elenco = new ArrayList<>();
    private Tecnico tecnico;

    public Tecnico getTecnico(){
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico){
        this.tecnico=tecnico;
    }

    public void adicionarAtleta(Atleta atleta){
        // Ainda vou colocar as Exceções depois...
        elenco.add(atleta;)
    }

    public void removerAtleta(Atleta atleta){
        elenco.remove(atleta);
    }

    // Classe incompleta. Vou completar em outro momento...
}
