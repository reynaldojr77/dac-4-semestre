package Viewer;

import Controller.IndicadoresController;
import Model.entity.Matricula;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardView extends JFrame {

    private JComboBox<String> seletorMunicipio;
    private JComboBox<Integer> seletorAno;
    private JButton botaoAplicar;

    private JLabel valorMatriculas = new JLabel("—", JLabel.CENTER);
    private JLabel valorAprovados = new JLabel("—", JLabel.CENTER);
    private JLabel valorReprovados = new JLabel("—", JLabel.CENTER);
    private JLabel valorAbandono = new JLabel("—", JLabel.CENTER);

    private JLabel valorTaxaAprovacao = new JLabel("—", JLabel.CENTER);
    private JLabel valorTaxaReprovacao = new JLabel("—", JLabel.CENTER);
    private JLabel valorTaxaAbandono = new JLabel("—", JLabel.CENTER);

    private JTable tabelaMatriculas;
    private DefaultTableModel modeloTabela;

    private final DefaultCategoryDataset dadosGrafico = new DefaultCategoryDataset();
    private final DefaultCategoryDataset dadosGraficoTaxas = new DefaultCategoryDataset();

    private IndicadoresController controller;
    private List<Matricula> registrosCompletos;

    private JFreeChart graficoMunicipios;
    private JFreeChart graficoTaxas;

    public DashboardView() {
        setTitle("DAC - Indicadores Educacionais de MS");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        montarInterface();
    }

    private JPanel criarPainelFiltros() {
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        seletorMunicipio = new JComboBox<>();
        seletorAno = new JComboBox<>();
        botaoAplicar = new JButton("Aplicar filtros");

        painelFiltros.add(new JLabel("Município:"));
        painelFiltros.add(seletorMunicipio);

        painelFiltros.add(new JLabel("Ano:"));
        painelFiltros.add(seletorAno);

        painelFiltros.add(botaoAplicar);
        return painelFiltros;
    }

    public void preencherFiltros(List<Matricula> registros) {
        seletorMunicipio.removeAllItems();
        seletorAno.removeAllItems();

        seletorMunicipio.addItem("Todos os municípios");

        registros.stream()
                .map(Matricula::getNomeMunicipio)
                .distinct()
                .sorted()
                .forEach(seletorMunicipio::addItem);

        registros.stream()
                .map(Matricula::getAnoReferencia)
                .distinct()
                .sorted()
                .forEach(seletorAno::addItem);
    }

    public void configurarFiltros(IndicadoresController controller, List<Matricula> registros) {
        this.controller = controller;
        this.registrosCompletos = registros;
        botaoAplicar.addActionListener(evento -> aplicarFiltros());
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String municipio = (String) seletorMunicipio.getSelectedItem();
        Integer ano = (Integer) seletorAno.getSelectedItem();

        if (municipio == null || ano == null) {
            return;
        }

        List<Matricula> registrosFiltrados = controller.filtrarRegistros(
                registrosCompletos,
                municipio,
                ano
        );

        atualizarTabela(registrosFiltrados);
        atualizarGraficoMunicipios(controller.somarMatriculasPorMunicipio(registrosFiltrados));

        if (registrosFiltrados.isEmpty()) {
            valorMatriculas.setText("Sem dados");
            valorAprovados.setText("Sem dados");
            valorReprovados.setText("Sem dados");
            valorAbandono.setText("Sem dados");

            valorTaxaAprovacao.setText("Sem dados");
            valorTaxaReprovacao.setText("Sem dados");
            valorTaxaAbandono.setText("Sem dados");

            dadosGraficoTaxas.clear();
            if (graficoTaxas != null) {
                graficoTaxas.setTitle("Taxas gerais — Sem dados");
            }
        } else {
            int totalMatriculas = controller.calcularTotalMatriculas(registrosFiltrados);
            int totalAprovados = controller.calcularTotalAprovados(registrosFiltrados);
            int totalReprovados = controller.calcularTotalReprovados(registrosFiltrados);
            int totalAbandono = controller.calcularTotalAbandono(registrosFiltrados);

            double taxaAprovacao = controller.calcularTaxaAprovacaoPercentual(registrosFiltrados);
            double taxaReprovacao = controller.calcularTaxaReprovacaoPercentual(registrosFiltrados);
            double taxaAbandono = controller.calcularTaxaAbandonoPercentual(registrosFiltrados);

            atualizarIndicadores(totalMatriculas, totalAprovados, totalReprovados, totalAbandono);
            atualizarTaxas(taxaAprovacao, taxaReprovacao, taxaAbandono);
            atualizarGraficoTaxas(taxaAprovacao, taxaReprovacao, taxaAbandono, ano, municipio);
        }

        if ("Todos os municípios".equals(municipio)) {
            graficoMunicipios.setTitle("Até 10 municípios com mais matrículas — " + ano);
        } else {
            graficoMunicipios.setTitle("Matrículas — " + municipio + " — " + ano);
        }
    }

    private JPanel criarCartaoIndicador(String titulo, JLabel valor) {
        JPanel cartao = new JPanel(new BorderLayout(0, 8));
        cartao.setBorder(BorderFactory.createEtchedBorder());

        JLabel rotuloTitulo = new JLabel(titulo, JLabel.CENTER);
        rotuloTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        valor.setFont(new Font("SansSerif", Font.BOLD, 24));

        cartao.add(rotuloTitulo, BorderLayout.NORTH);
        cartao.add(valor, BorderLayout.CENTER);
        return cartao;
    }

    private JPanel criarPainelIndicadores() {
        JPanel painelIndicadores = new JPanel(new GridLayout(1, 4, 12, 0));

        painelIndicadores.add(criarCartaoIndicador("Total de matrículas", valorMatriculas));
        painelIndicadores.add(criarCartaoIndicador("Aprovados", valorAprovados));
        painelIndicadores.add(criarCartaoIndicador("Reprovados", valorReprovados));
        painelIndicadores.add(criarCartaoIndicador("Abandono", valorAbandono));

        return painelIndicadores;
    }

    private JPanel criarPainelTaxas() {
        JPanel painelTaxas = new JPanel(new GridLayout(1, 3, 12, 0));

        painelTaxas.add(criarCartaoIndicador("Taxa de aprovação", valorTaxaAprovacao));
        painelTaxas.add(criarCartaoIndicador("Taxa de reprovação", valorTaxaReprovacao));
        painelTaxas.add(criarCartaoIndicador("Taxa de abandono", valorTaxaAbandono));

        return painelTaxas;
    }

    public void atualizarIndicadores(int totalMatriculas, int totalAprovados, int totalReprovados, int totalAbandono) {
        NumberFormat formatador = NumberFormat.getIntegerInstance(Locale.forLanguageTag("pt-BR"));

        valorMatriculas.setText(formatador.format(totalMatriculas));
        valorAprovados.setText(formatador.format(totalAprovados));
        valorReprovados.setText(formatador.format(totalReprovados));
        valorAbandono.setText(formatador.format(totalAbandono));
    }

    public void atualizarTaxas(double taxaAprovacao, double taxaReprovacao, double taxaAbandono) {
        valorTaxaAprovacao.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f%%", taxaAprovacao));
        valorTaxaReprovacao.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f%%", taxaReprovacao));
        valorTaxaAbandono.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f%%", taxaAbandono));
    }

    private JScrollPane criarTabelaMatriculas() {
        String[] colunas = {
                "Ano",
                "Município",
                "Unidade escolar",
                "Matrículas",
                "Aprovados",
                "Reprovados",
                "Abandono"
        };

        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaMatriculas = new JTable(modeloTabela);
        tabelaMatriculas.setRowHeight(24);
        tabelaMatriculas.setDefaultEditor(Object.class, null);
        tabelaMatriculas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tabelaMatriculas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaMatriculas.getColumnModel().getColumn(2).setPreferredWidth(400);

        return new JScrollPane(tabelaMatriculas);
    }

    public void atualizarTabela(List<Matricula> registros) {
        modeloTabela.setRowCount(0);

        for (Matricula registro : registros) {
            modeloTabela.addRow(new Object[]{
                    registro.getAnoReferencia(),
                    registro.getNomeMunicipio(),
                    registro.getNomeUnidadeEscolar(),
                    registro.getMatriculasTotal(),
                    registro.getAprovados(),
                    registro.getReprovados(),
                    registro.getAbandono()
            });
        }
    }

    private ChartPanel criarGraficoMunicipios() {
        graficoMunicipios = ChartFactory.createBarChart(
                "10 municípios com mais matrículas — 2026",
                "Município",
                "Matrículas",
                dadosGrafico,
                PlotOrientation.HORIZONTAL,
                false,
                true,
                false
        );

        return new ChartPanel(graficoMunicipios);
    }

    private ChartPanel criarGraficoTaxas() {
        graficoTaxas = ChartFactory.createBarChart(
                "Taxas gerais — 2026",
                "Indicador",
                "Percentual (%)",
                dadosGraficoTaxas,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        return new ChartPanel(graficoTaxas);
    }

    public void atualizarGraficoMunicipios(Map<String, Integer> porMunicipio) {
        dadosGrafico.clear();

        porMunicipio.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach(entrada -> dadosGrafico.addValue(
                        entrada.getValue(),
                        "Matrículas",
                        entrada.getKey()
                ));
    }

    public void atualizarGraficoTaxas(double taxaAprovacao, double taxaReprovacao, double taxaAbandono, int ano, String municipio) {
        dadosGraficoTaxas.clear();
        dadosGraficoTaxas.addValue(taxaAprovacao, "Taxa", "Aprovação");
        dadosGraficoTaxas.addValue(taxaReprovacao, "Taxa", "Reprovação");
        dadosGraficoTaxas.addValue(taxaAbandono, "Taxa", "Abandono");

        if (graficoTaxas != null) {
            graficoTaxas.setTitle("Taxas gerais — " + municipio + " — " + ano);
        }
    }

    private void montarInterface() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(12, 12));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelCabecalho = new JPanel(new BorderLayout(0, 6));

        JLabel titulo = new JLabel("Indicadores Educacionais de MS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel descricao = new JLabel("Consulta de matrículas, aprovação, reprovação e abandono escolar.");

        painelCabecalho.add(titulo, BorderLayout.NORTH);
        painelCabecalho.add(descricao, BorderLayout.CENTER);

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBorder(BorderFactory.createTitledBorder("Painel de dados"));

        JTabbedPane abasResultados = new JTabbedPane();
        abasResultados.addTab("Tabela", criarTabelaMatriculas());
        abasResultados.addTab("Gráfico por município", criarGraficoMunicipios());
        abasResultados.addTab("Taxas gerais", criarGraficoTaxas());

        JPanel painelResultados = new JPanel(new BorderLayout(0, 16));
        painelResultados.add(criarPainelIndicadores(), BorderLayout.NORTH);

        JPanel painelSecundario = new JPanel(new BorderLayout(0, 12));
        painelSecundario.add(criarPainelTaxas(), BorderLayout.NORTH);
        painelSecundario.add(abasResultados, BorderLayout.CENTER);

        painelResultados.add(painelSecundario, BorderLayout.CENTER);

        painelConteudo.add(criarPainelFiltros(), BorderLayout.NORTH);
        painelConteudo.add(painelResultados, BorderLayout.CENTER);

        JLabel rodape = new JLabel("Fonte: Portal de Dados Abertos de MS | 2026: dados parciais.");

        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
    }
}
