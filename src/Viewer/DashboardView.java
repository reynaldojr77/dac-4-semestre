package Viewer;

import Controller.IndicadoresController;
import java.util.Map;
import javax.swing.JTabbedPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import Model.entity.Matricula;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Font;

public class DashboardView extends JFrame {

    // Componentes que serão utilizados para selecionar os filtros.
    private JComboBox<String> seletorMunicipio;
    private JComboBox<Integer> seletorAno;
    private JButton botaoAplicar;

    // Textos que mostrarão os valores dos indicadores.
    private JLabel valorMatriculas = new JLabel("—", JLabel.CENTER);
    private JLabel valorAprovados = new JLabel("—", JLabel.CENTER);
    private JLabel valorReprovados = new JLabel("—", JLabel.CENTER);
    private JLabel valorAbandono = new JLabel("—", JLabel.CENTER);

    // Componentes utilizados para apresentar os registros.
    private JTable tabelaMatriculas;
    private DefaultTableModel modeloTabela;

    // Guarda os valores que serão apresentados no gráfico.
    private DefaultCategoryDataset dadosGrafico =
            new DefaultCategoryDataset();

    // Recursos utilizados para aplicar os filtros.
    private IndicadoresController controller;
    private List<Matricula> registrosCompletos;

    // Referência ao gráfico, para atualizar seu título.
    private JFreeChart graficoMunicipios;

    public DashboardView() {

        // Configura a janela.
        setTitle("DAC - Indicadores Educacionais de MS");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Monta os componentes que aparecem dentro da janela.
        montarInterface();

    }
    private JPanel criarPainelFiltros() {

        // Organiza os componentes em sequência, alinhados à esquerda.
        JPanel painelFiltros = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10)
        );

        // Cria os seletores vazios; as opções serão preenchidas com os dados.
        seletorMunicipio = new JComboBox<>();
        seletorAno = new JComboBox<>();

        // Cria o botão utilizado para aplicar os filtros.
        botaoAplicar = new JButton("Aplicar filtros");

        // Adiciona os componentes na ordem em que devem aparecer.
        painelFiltros.add(new JLabel("Município:"));
        painelFiltros.add(seletorMunicipio);

        painelFiltros.add(new JLabel("Ano:"));
        painelFiltros.add(seletorAno);

        painelFiltros.add(botaoAplicar);

        // Entrega o painel montado para quem chamou este método.
        return painelFiltros;
    }

    public void preencherFiltros(List<Matricula> registros) {

        // Limpa as opções anteriores.
        seletorMunicipio.removeAllItems();
        seletorAno.removeAllItems();

        // Adiciona a opção que representa a consulta de todos os municípios.
        seletorMunicipio.addItem("Todos os municípios");

        // Obtém os nomes dos municípios, remove repetições e organiza a lista.
        registros.stream()
                .map(registro -> registro.getNomeMunicipio())
                .distinct()
                .sorted()
                .forEach(municipio -> seletorMunicipio.addItem(municipio));

        // Obtém os anos disponíveis, remove repetições e organiza a lista.
        registros.stream()
                .map(registro -> registro.getAnoReferencia())
                .distinct()
                .sorted()
                .forEach(ano -> seletorAno.addItem(ano));
    }

    public void configurarFiltros(
            IndicadoresController controller,
            List<Matricula> registros
    ) {

        // Guarda os recursos que serão utilizados nas consultas.
        this.controller = controller;
        this.registrosCompletos = registros;

        // Executa a aplicação dos filtros quando o botão é clicado.
        botaoAplicar.addActionListener(evento -> aplicarFiltros());

        // Preenche a tela inicialmente com as opções selecionadas.
        aplicarFiltros();
    }

    private void aplicarFiltros() {

        // Lê as opções selecionadas nos componentes.
        String municipio = (String) seletorMunicipio.getSelectedItem();
        Integer ano = (Integer) seletorAno.getSelectedItem();

        // Aguarda opções válidas antes de realizar a consulta.
        if (municipio == null || ano == null) {
            return;
        }

        // Solicita ao controller os registros do município e ano escolhidos.
        List<Matricula> registrosFiltrados = controller.filtrarRegistros(
                registrosCompletos,
                municipio,
                ano
        );

        // Atualiza a tabela e o gráfico com o mesmo conjunto de registros.
        atualizarTabela(registrosFiltrados);

        atualizarGraficoMunicipios(
                controller.somarMatriculasPorMunicipio(registrosFiltrados)
        );

        // Distingue ausência de registros de resultados realmente iguais a zero.
        if (registrosFiltrados.isEmpty()) {

            valorMatriculas.setText("Sem dados");
            valorAprovados.setText("Sem dados");
            valorReprovados.setText("Sem dados");
            valorAbandono.setText("Sem dados");

        } else {

            atualizarIndicadores(
                    controller.calcularTotalMatriculas(registrosFiltrados),
                    controller.calcularTotalAprovados(registrosFiltrados),
                    controller.calcularTotalReprovados(registrosFiltrados),
                    controller.calcularTotalAbandono(registrosFiltrados)
            );
        }

        // Ajusta o título do gráfico ao recorte apresentado.
        if ("Todos os municípios".equals(municipio)) {

            graficoMunicipios.setTitle(
                    "Até 10 municípios com mais matrículas — " + ano
            );

        } else {

            graficoMunicipios.setTitle(
                    "Matrículas — " + municipio + " — " + ano
            );
        }
    }

    private JPanel criarCartaoIndicador(String titulo, JLabel valor) {

        // Organiza o título em cima e o valor no centro.
        JPanel cartao = new JPanel(new BorderLayout(0, 8));

        // Cria uma borda visível para separar o cartão.
        cartao.setBorder(BorderFactory.createEtchedBorder());

        // Cria o título do indicador.
        JLabel rotuloTitulo = new JLabel(titulo, JLabel.CENTER);
        rotuloTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Destaca o número com uma fonte maior.
        valor.setFont(new Font("SansSerif", Font.BOLD, 26));

        // Coloca os componentes dentro do cartão.
        cartao.add(rotuloTitulo, BorderLayout.NORTH);
        cartao.add(valor, BorderLayout.CENTER);

        return cartao;
    }

    private JPanel criarPainelIndicadores() {

        // Cria uma linha com quatro colunas e espaçamento entre os cartões.
        JPanel painelIndicadores = new JPanel(
                new GridLayout(1, 4, 12, 0)
        );

        // Monta os quatro cartões e os adiciona ao painel.
        painelIndicadores.add(
                criarCartaoIndicador("Total de matrículas", valorMatriculas)
        );

        painelIndicadores.add(
                criarCartaoIndicador("Aprovados", valorAprovados)
        );

        painelIndicadores.add(
                criarCartaoIndicador("Reprovados", valorReprovados)
        );

        painelIndicadores.add(
                criarCartaoIndicador("Abandono", valorAbandono)
        );

        return painelIndicadores;
    }

    public void atualizarIndicadores(
            int totalMatriculas,
            int totalAprovados,
            int totalReprovados,
            int totalAbandono
    ) {

        // Prepara a formatação dos números no padrão brasileiro.
        NumberFormat formatador = NumberFormat.getIntegerInstance(
                Locale.forLanguageTag("pt-BR")
        );

        // Substitui os traços pelos valores recebidos.
        valorMatriculas.setText(formatador.format(totalMatriculas));
        valorAprovados.setText(formatador.format(totalAprovados));
        valorReprovados.setText(formatador.format(totalReprovados));
        valorAbandono.setText(formatador.format(totalAbandono));
    }

    private JScrollPane criarTabelaMatriculas() {

        // Define os nomes das colunas, na ordem de apresentação.
        String[] colunas = {
                "Ano",
                "Município",
                "Unidade escolar",
                "Matrículas",
                "Aprovados",
                "Reprovados",
                "Abandono"
        };

        // Cria o modelo com as colunas definidas e nenhuma linha inicialmente.
        modeloTabela = new DefaultTableModel(colunas, 0);

        // Cria a tabela usando esse modelo.
        tabelaMatriculas = new JTable(modeloTabela);

        // Define a altura das linhas.
        tabelaMatriculas.setRowHeight(24);

        // Desativa a edição das células.
        tabelaMatriculas.setDefaultEditor(Object.class, null);

        // Mantém as larguras das colunas e permite rolagem horizontal.
        tabelaMatriculas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Reserva mais espaço para os nomes dos municípios e das escolas.
        tabelaMatriculas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaMatriculas.getColumnModel().getColumn(2).setPreferredWidth(400);

        // Coloca a tabela dentro de uma área com rolagem.
        return new JScrollPane(tabelaMatriculas);
    }

    public void atualizarTabela(List<Matricula> registros) {

        // Remove as linhas anteriores antes de inserir os registros recebidos.
        modeloTabela.setRowCount(0);

        // Percorre os registros e adiciona uma linha para cada um.
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

        // Cria um gráfico de barras usando o conjunto de dados da classe.
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

        // Coloca o gráfico dentro de um componente visual do Swing.
        return new ChartPanel(graficoMunicipios);
    }

    public void atualizarGraficoMunicipios(
            Map<String, Integer> porMunicipio
    ) {

        // Limpa os valores anteriores antes de atualizar o gráfico.
        dadosGrafico.clear();

        // Ordena os municípios pelo total, seleciona até dez e preenche o gráfico.
        porMunicipio.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(10)
                .forEach(entrada -> {
                    dadosGrafico.addValue(
                            entrada.getValue(),
                            "Matrículas",
                            entrada.getKey()
                    );
                });
    }

    private void montarInterface() {

        // Painel principal: organiza o conteúdo e deixa uma margem nas bordas.
        JPanel painelPrincipal = new JPanel(new BorderLayout(12, 12));
        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Cabeçalho: reúne o título e a descrição.
        JPanel painelCabecalho = new JPanel(new BorderLayout(0, 6));

        JLabel titulo = new JLabel("Indicadores Educacionais de MS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel descricao = new JLabel(
                "Consulta de matrículas, aprovação e abandono escolar."
        );

        painelCabecalho.add(titulo, BorderLayout.NORTH);
        painelCabecalho.add(descricao, BorderLayout.CENTER);

        // Área central: receberá os componentes das próximas etapas.
        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBorder(
                BorderFactory.createTitledBorder("Painel de dados")
        );

        // Cria as abas que permitem alternar entre tabela e gráfico.
        JTabbedPane abasResultados = new JTabbedPane();

        abasResultados.addTab("Tabela", criarTabelaMatriculas());
        abasResultados.addTab("Gráfico por município", criarGraficoMunicipios());

        // Reúne os indicadores e as abas.
        JPanel painelResultados = new JPanel(new BorderLayout(0, 16));

        painelResultados.add(criarPainelIndicadores(), BorderLayout.NORTH);
        painelResultados.add(abasResultados, BorderLayout.CENTER);

        // Mantém os filtros no alto e coloca os resultados abaixo.
        painelConteudo.add(criarPainelFiltros(), BorderLayout.NORTH);
        painelConteudo.add(painelResultados, BorderLayout.CENTER);

        // Rodapé: identifica a fonte dos dados.
        JLabel rodape = new JLabel(
                "Fonte: Portal de Dados Abertos de MS | 2026: dados parciais."
        );

        // Distribui as três regiões dentro do painel principal.
        painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
        painelPrincipal.add(rodape, BorderLayout.SOUTH);

        // Define o painel principal como o conteúdo da janela.
        setContentPane(painelPrincipal);
    }
}
