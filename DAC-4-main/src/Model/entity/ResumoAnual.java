package Model.entity;

/** Resultado de um ano e município; ausência de registros é diferente de zero. */
public record ResumoAnual(
        int ano, int quantidadeRegistros, int matriculas, int aprovados,
        int reprovados, int abandono, int cursando,
        Double percentualAprovacao, Double percentualReprovacao, Double percentualAbandono
) {
    public boolean possuiDados() {
        return quantidadeRegistros > 0;
    }
}
