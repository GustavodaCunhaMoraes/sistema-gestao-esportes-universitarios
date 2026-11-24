
import java.util.ArrayList;
import java.util.Comparator;

public class Campeonato {
    private ArrayList<String> regulamento;
    private  Modalidade modalidade ;
    private int numEquipes=0;
    private ArrayList<Jogo> tabelaJogos;
    private ArrayList<Equipe> classificacao;
    private int duracao;
    // ---------Regulamento---------------
    public void adicionarRegra(String regra){
        regulamento.add(regra);
    }

    public void removerRegra(String regra){
        regulamento.remove(regra);
    }
    //------------Modalidade do Campenoato------
    public Modalidade getModalidade(){
        return modalidade;
    }

    public void setModalidade(Modalidade modalidade){
        this.modalidade=modalidade;
    }
    //-------------Numero de Equipes do Campeonato----
    public int getNumEquipes(){
        return numEquipes;
    }

    //---------Tabela de jogos------
    public void adicionarJogo(Jogo jogo){
        tabelaJogos.add(jogo);
    }

    public void removerJogo(Jogo jogo){
        tabelaJogos.remove(jogo);
    }
    //----------Classificação---
    public void adicionarEquipes(Equipe equipe){
        classificacao.add(equipe);
        numEquipes++;
    }

    public void ordenarEquipes(){
        classificacao.sort(Comparator.comparingInt(Equipe::getPontos).reversed());
    }
    //--------- Duracao dos jogos----
    public int getDuracao(){
        return duracao;
    }

    public void setDuracao(int duracao){
        this.duracao=duracao;
    }

    //---------Construtor-------
    public Campeonato(Modalidade modalidade, int duracao){
        setModalidade(modalidade);
        setDuracao(duracao);
    }
}
