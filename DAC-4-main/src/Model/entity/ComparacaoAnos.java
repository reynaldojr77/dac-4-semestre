package Model.entity;

/** Variações sempre calculadas como ano comparado menos ano base. */
public record ComparacaoAnos(ResumoAnual base, ResumoAnual comparado) {
    public boolean possuiDadosNosDoisAnos() {
        return base.possuiDados() && comparado.possuiDados();
    }

    public Integer diferencaMatriculas() {
        return possuiDadosNosDoisAnos() ? comparado.matriculas() - base.matriculas() : null;
    }

    public Double variacaoMatriculasPercentual() {
        if (!possuiDadosNosDoisAnos() || base.matriculas() == 0) return null;
        return diferencaMatriculas() * 100.0 / base.matriculas();
    }

    public Double diferencaAprovacaoPontosPercentuais() {
        return diferencaPercentual(base.percentualAprovacao(), comparado.percentualAprovacao());
    }

    public Double diferencaReprovacaoPontosPercentuais() {
        return diferencaPercentual(base.percentualReprovacao(), comparado.percentualReprovacao());
    }

    public Double diferencaAbandonoPontosPercentuais() {
        return diferencaPercentual(base.percentualAbandono(), comparado.percentualAbandono());
    }

    private Double diferencaPercentual(Double inicial, Double finalizado) {
        return inicial == null || finalizado == null ? null : finalizado - inicial;
    }
}
