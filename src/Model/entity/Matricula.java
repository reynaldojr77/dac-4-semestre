package Model.entity;

public class Matricula {
    private int id;
    private int anoReferencia;
    private String nomeMunicipio;
    private String nomeUnidadeEscolar;
    private int matriculasTotal;
    private int matriculaInicial;
    private int matriculaAposCenso;
    private int transferidos;
    private int cancelados;
    private int falecido;
    private int abandono;
    private int aprovados;
    private int reprovados;
    private int cursando;
    private int outrasSituacoes;

    public Matricula() {
    }

    public Matricula(int id, int anoReferencia, String nomeMunicipio, String nomeUnidadeEscolar, int matriculasTotal, int matriculaInicial, int matriculaAposCenso, int transferidos, int cancelados, int falecido, int abandono, int aprovados, int reprovados, int cursando, int outrasSituacoes) {
        this.id = id;
        this.anoReferencia = anoReferencia;
        this.nomeMunicipio = nomeMunicipio;
        this.nomeUnidadeEscolar = nomeUnidadeEscolar;
        this.matriculasTotal = matriculasTotal;
        this.matriculaInicial = matriculaInicial;
        this.matriculaAposCenso = matriculaAposCenso;
        this.transferidos = transferidos;
        this.cancelados = cancelados;
        this.falecido = falecido;
        this.abandono = abandono;
        this.aprovados = aprovados;
        this.reprovados = reprovados;
        this.cursando = cursando;
        this.outrasSituacoes = outrasSituacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnoReferencia() {
        return anoReferencia;
    }

    public void setAnoReferencia(int anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public String getNomeUnidadeEscolar() {
        return nomeUnidadeEscolar;
    }

    public void setNomeUnidadeEscolar(String nomeUnidadeEscolar) {
        this.nomeUnidadeEscolar = nomeUnidadeEscolar;
    }

    public int getMatriculasTotal() {
        return matriculasTotal;
    }

    public void setMatriculasTotal(int matriculasTotal) {
        this.matriculasTotal = matriculasTotal;
    }

    public int getMatriculaInicial() {
        return matriculaInicial;
    }

    public void setMatriculaInicial(int matriculaInicial) {
        this.matriculaInicial = matriculaInicial;
    }

    public int getMatriculaAposCenso() {
        return matriculaAposCenso;
    }

    public void setMatriculaAposCenso(int matriculaAposCenso) {
        this.matriculaAposCenso = matriculaAposCenso;
    }

    public int getTransferidos() {
        return transferidos;
    }

    public void setTransferidos(int transferidos) {
        this.transferidos = transferidos;
    }

    public int getCancelados() {
        return cancelados;
    }

    public void setCancelados(int cancelados) {
        this.cancelados = cancelados;
    }

    public int getFalecido() {
        return falecido;
    }

    public void setFalecido(int falecido) {
        this.falecido = falecido;
    }

    public int getAbandono() {
        return abandono;
    }

    public void setAbandono(int abandono) {
        this.abandono = abandono;
    }

    public int getAprovados() {
        return aprovados;
    }

    public void setAprovados(int aprovados) {
        this.aprovados = aprovados;
    }

    public int getReprovados() {
        return reprovados;
    }

    public void setReprovados(int reprovados) {
        this.reprovados = reprovados;
    }

    public int getCursando() {
        return cursando;
    }

    public void setCursando(int cursando) {
        this.cursando = cursando;
    }

    public int getOutrasSituacoes() {
        return outrasSituacoes;
    }

    public void setOutrasSituacoes(int outrasSituacoes) {
        this.outrasSituacoes = outrasSituacoes;
    }


}
