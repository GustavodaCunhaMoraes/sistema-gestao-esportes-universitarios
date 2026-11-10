public class EspaçoEsportivo {
    private String nome;
    private String tipo;
    private String localizacao;
    private int capacidade;
    private String horaDisp;
    private boolean funcionamento;

    public void setNome(String nome){
        this.nome=nome;
    }

    public String getNome(){
        return nome;
    }

    public void setTipo(String tipo){
        this.tipo=tipo;
    }

    public String getTipo(){
        return tipo;
    }

    public void setLocalizacao(String localizacao){
        this.localizacao=localizacao;
    }

    public String getLocalizacao(){
        return localizacao;
    }

    public void setCapacidade( int capacidade){
        this.capacidade=capacidade;
    }

    public int getCapacidade(){
        return capacidade;
    }

    public void setHoraDisp(String horaDisp){
        this.horaDisp=horaDisp;
    }

    public String getHoraDisp(){
        return horaDisp;
    }

    public void setFuncionamento(boolean funcionamento){
        this.funcionamento=funcionamento;
    }

    public boolean getFuncionamento(){
        return funcionamento;
    }

    public EspaçoEsportivo(String nome, String tipo, String localizacao,
         int capacidade, String horaDisp, boolean funcionamento ){
        setNome(nome);
        setTipo(tipo);
        setLocalizacao(localizacao);
        setCapacidade(capacidade);
        setHoraDisp(horaDisp);
        setFuncionamento(funcionamento);
    }

    public void ExibirInfo(String nome, String tipo, String localizacao,
         int capacidade, String horaDisp, boolean funcionamento ){
        System.out.println("Nome do Espaço: " + getNome() + " | Tipo: "+ getTipo() + 
                           " | Localização: " + getLocalizacao() + " | Capacidade: " +
                            getCapacidade() + " | Horários disponíveis: " + getHoraDisp() + 
                            " | Status de Funcionamento: " + getFuncionamento() );

    }

}
