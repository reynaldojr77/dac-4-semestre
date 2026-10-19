package Controller;

import Model.dao.MatriculaDAO;
import Model.entity.Matricula;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndicadoresController {

    private final MatriculaDAO matriculaDAO;

    public IndicadoresController(){
        this.matriculaDAO = new MatriculaDAO();
    }

    /**
     * Carrega a lista de matrículas de um arquivo específico.
     */
    public List<Matricula> carregarDadosDoArquivo(String caminhoArquivo){
        return matriculaDAO.carregarPorArquivo(caminhoArquivo);
    }

    /**
     * Carrega a lista unificada de vários arquivos (ex: 2018 a 2026).
     */
    public List<Matricula> carregarTodosOsDados(List<String> caminhosArquivos) {
        return matriculaDAO.carregarTodosArquivos(caminhosArquivos);
    }

    // --- CÁLCULOS E INDICADORES (REGRAS DE NEGÓCIO) ---

    /**
     * Calcula o total geral de matrículas na lista fornecida.
     */
    public int calcularTotalMatriculas(List<Matricula> lista) {
        return lista.stream()
                .mapToInt(Matricula::getMatriculasTotal)
                .sum();
    }

    /**
     * Calcula o total de alunos aprovados.
     */
    public int calcularTotalAprovados(List<Matricula> lista) {
        return lista.stream()
                .mapToInt(Matricula::getAprovados)
                .sum();
    }

    public int calcularTotalReprovados(List<Matricula> lista) {
        return lista.stream()
                .mapToInt(Matricula::getReprovados)
                .sum();
    }

    /**
     * Calcula o total de abandonos.
     */
    public int calcularTotalAbandono(List<Matricula> lista) {
        return lista.stream()
                .mapToInt(Matricula::getAbandono)
                .sum();
    }

    /**
     * Calcula a taxa percentual de abandono em relação ao total de matrículas.
     */
    public double calcularTaxaAbandonoPercentual(List<Matricula> lista) {
        int total = calcularTotalMatriculas(lista);
        if (total == 0) return 0.0;

        int abandono = calcularTotalAbandono(lista);
        return ((double) abandono / total) * 100;
    }

    /**
     * Calcula a taxa percentual de aprovação em relação ao total de matrículas.
     */
    public double calcularTaxaAprovacaoPercentual(List<Matricula> lista) {
        int total = calcularTotalMatriculas(lista);
        if (total == 0) return 0.0;

        int aprovados = calcularTotalAprovados(lista);
        return ((double) aprovados / total) * 100;
    }

    /**
     * Calcula a taxa percentual de reprovação em relação ao total de matrículas.
     */
    public double calcularTaxaReprovacaoPercentual(List<Matricula> lista) {
        int total = calcularTotalMatriculas(lista);
        if (total == 0) return 0.0;

        int reprovados = calcularTotalReprovados(lista);
        return ((double) reprovados / total) * 100;
    }

    /**
     * Agrupa o total de matrículas por Município.
     * @return Map onde a chave é o nome do município e o valor é o total de matrículas.
     */
    public Map<String, Integer> somarMatriculasPorMunicipio(List<Matricula> lista) {
        return lista.stream()
                .collect(Collectors.groupingBy(
                        Matricula::getNomeMunicipio,
                        Collectors.summingInt(Matricula::getMatriculasTotal)
                ));
    }

    public List<Matricula> filtrarRegistros(
            List<Matricula> registros,
            String municipio,
            int ano
    ) {
        return registros.stream()
                .filter(registro -> registro.getAnoReferencia() == ano)
                .filter(registro ->
                        "Todos os municípios".equals(municipio)
                                || municipio.equals(registro.getNomeMunicipio())
                )
                .collect(Collectors.toList());
    }
}
