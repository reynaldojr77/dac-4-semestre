# DAC — Indicadores Educacionais de MS com SQLite

Projeto acadêmico do 4º semestre de Análise e Desenvolvimento de Sistemas, com sistema desenvolvido por **Reynaldo, João e Pedro**.

Aplicação em Java para consultar e comparar dados de matrículas, aprovação, reprovação e abandono escolar de Mato Grosso do Sul. Esta versão consulta o banco SQLite do Pedro, sem carregar os CSVs ao iniciar. Mantém o painel, os filtros e a ordenação, e acrescenta a comparação entre anos. Não inclui comparação entre municípios nem filtro por escola.

## Requisitos

- **Java 17 ou superior**. Para recompilar, é preciso um **JDK** 17 ou superior.
- Nada mais precisa ser instalado: o banco, as bibliotecas e o aplicativo compilado estão no repositório. Não é usado Maven nem servidor SQL.

Clone o repositório ou baixe e extraia o ZIP inteiro, mantendo a estrutura de pastas:

```sh
git clone https://github.com/reynaldojr77/dac-4-semestre.git
cd dac-4-semestre/DAC-4-main
```

## Executar no Windows

1. Abra a pasta `DAC-4-main`.
2. Execute `executar.bat`.

O lançador procura o Java em `JAVA_HOME`, no `PATH` ou nos JDKs do IntelliJ em `%USERPROFILE%\.jdks`. Se não encontrar, configure Java 17 ou superior ou execute pelo IntelliJ.

## Executar no Linux ou macOS

Na pasta `DAC-4-main`:

```sh
java -jar dac.jar
```

## Conferir pelo terminal, sem abrir a interface

Na pasta `DAC-4-main`, em qualquer sistema:

```sh
java -jar dac.jar --verificar-banco
```

O resultado deve mostrar 3.703 registros, os anos 2018–2024 e 2026, e terminar com `Consulta ao SQLite concluída. Nenhum CSV foi carregado.`

## Executar pelo IntelliJ IDEA

1. Abra `DAC-4-main` como projeto e selecione JDK 17 ou superior em **File → Project Structure → Project**.
2. Se necessário, clique com o botão direito em `src` e selecione **Mark Directory as → Sources Root**.
3. Em **File → Project Structure → Modules → Dependencies**, adicione **todos** os JARs de `lib` usando **+ → JARs or Directories**.
4. Execute `src/Main.java`. Se a IDE pedir, selecione a classe `Main`.

## Executar no VS Code

1. Instale o **Extension Pack for Java**, da Microsoft, e configure um JDK 17 ou superior.
2. Use **File → Open Folder** para abrir a pasta `DAC-4-main`.
3. Confira se os quatro JARs de `lib` aparecem em **Java Projects → Referenced Libraries**. Se necessário, adicione-os pelo botão **+**.
4. Abra `src/Main.java` e clique em **Run**, acima do método `main`.

Bibliotecas: `jfreechart-1.5.6.jar`, `sqlite-jdbc-3.54.1.0.jar`, `slf4j-api-1.7.36.jar` e `slf4j-nop-1.7.36.jar`.

## Localização do banco

O código procura o banco a partir da localização do JAR ou das classes e do diretório de execução, incluindo pastas ancestrais da saída da IDE. Mantenha `database` dentro de `DAC-4-main`. Para um caminho personalizado, configure a opção de VM `-Ddac.banco=CAMINHO` ou a variável `DAC_BANCO`; valores explícitos inválidos produzem erro, sem recorrer a outro arquivo.

## Dados disponíveis

- 3.703 registros, 79 municípios e 599 cadastros de escolas.
- Anos: 2018, 2019, 2020, 2021, 2022, 2023, 2024 e 2026.
- 2025 está ausente na fonte informada pelo grupo e não recebe dados artificiais.
- O arquivo do Pedro foi copiado sem alteração de estrutura ou valores.
- A conexão usa somente leitura; o programa não cria banco vazio nem importa CSV automaticamente.
- Os CSVs de 2024 e 2026 permanecem em `src` apenas como fontes de conferência. Não são necessários para executar esta versão.

Fonte: [Matrículas por unidade escolar — Dados Abertos de MS](https://www.dados.ms.gov.br/dataset/matriculas-por-unidade-escolar)

A carga acontece uma vez pelo DAO, retornando `List<Matricula>`. O controller continua responsável pelos filtros e cálculos, e a interface apresenta os resultados. O identificador de origem do CSV é preservado em `Matricula.id` e só é único em conjunto com o ano.

## Filtros e comparação

O painel usa município e **Ano do painel**. A tabela começa ordenada por matrículas e permite ordenar pelos cabeçalhos. As abas **Tabela**, **Gráfico por município**, **Taxas gerais** e **Comparação entre anos** acompanham a seleção.

A aba **Comparação entre anos** usa o município selecionado acima e dois anos independentes do Ano do painel. Diferença = ano comparado menos ano base. A tabela mostra totais, variação de matrículas em quantidade e porcentagem e diferenças dos percentuais em pontos percentuais. A visualização gráfica fica em **Gráfico de percentuais**.

## Interpretação dos indicadores

Cada percentual é calculado sobre as matrículas do recorte selecionado:

`Percentual de aprovação = (total de aprovados / total de matrículas) × 100`

A mesma lógica é usada para reprovação e abandono. Esses valores não foram redefinidos como taxas oficiais de rendimento e a metodologia deve ser conferida pela equipe antes de apresentá-los dessa forma.

Alunos cursando, dados parciais e abandono zerado na fonte devem ser considerados ao interpretar o histórico:

- Em **2024**, a maior parte dos registros consta como cursando (184.949 de 220.926 matrículas), com apenas 3.484 aprovados e 1.031 reprovados.
- Em **2026**, aprovados, reprovados e abandono estão zerados, e as barras do gráfico de taxas podem não ficar visíveis. Os dados são parciais e não devem ser lidos como resultados anuais consolidados.
- De **2020 a 2024**, o abandono está zerado.

Esses valores fazem parte dos arquivos de origem. Recortes sem registros não são tratados como zero. Base zero impede variação percentual e percentuais sem denominador ficam indisponíveis na comparação.

## Alterar e recompilar

O IntelliJ recompila os fontes ao executar `Main`. Para atualizar o `dac.jar` depois de editar os fontes, execute `compilar.bat` no Windows ou, no Linux e macOS, na pasta `DAC-4-main`:

```sh
mkdir -p build/classes
javac --release 17 -encoding UTF-8 -cp "lib/*" -d build/classes $(find src -name '*.java')
jar --create --file dac.jar --manifest manifest.mf -C build/classes .
```

Executar um JAR antigo não incorpora alterações nos fontes.

## Conferência

Use [docs/TESTES.md](docs/TESTES.md). O console informa a origem SQLite, a quantidade de registros e o resumo de cada ano. Não soma todos os anos como se representassem um único ano letivo.

## Organização

| Caminho | Conteúdo |
| --- | --- |
| `DAC-4-main/src/Main.java` | Carga SQL e inicialização |
| `DAC-4-main/src/Model/dao/ConexaoBanco.java` | Localização e conexão SQLite de leitura |
| `DAC-4-main/src/Model/dao/MatriculaDAO.java` | Consulta com JOIN e mapeamento dos 15 campos; leitura CSV preservada para conferência |
| `DAC-4-main/src/Model/entity/` | Modelos de dados e resultados da comparação |
| `DAC-4-main/src/Controller/IndicadoresController.java` | Filtros, totais e comparação |
| `DAC-4-main/src/Viewer/DashboardView.java` | Interface e gráficos |
| `DAC-4-main/src/Viewer/MainView.java` | Saída de console da versão anterior, sem uso na inicialização atual |
| `DAC-4-main/database/dac_ods4.db` | Banco do Pedro |
| `DAC-4-main/lib/` | Bibliotecas incluídas |
| `DAC-4-main/dac.jar` | Aplicativo compilado para Java 17 |
| `DAC-4-main/executar.bat`, `DAC-4-main/compilar.bat` | Execução e recompilação no Windows |
| `docs/` | Plano e testes manuais |
| `sql/` | Documentação e consulta de referência |

Driver SQLite JDBC: [xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) (3.54.1.0, distribuição com bibliotecas nativas para Windows, Linux e macOS).

## Equipe e colaboração

| Integrante | Frente principal |
| --- | --- |
| Reynaldo | Interface, filtros, indicadores e gráficos |
| João | Código Java, consultas e integração |
| Pedro | Estrutura do banco, importação e consultas SQL |

Utilize uma branch por tarefa e abra pull requests para revisar e integrar as alterações à `main`.

Preferências locais das IDEs e arquivos gerados pela compilação devem permanecer fora do versionamento, conforme o `.gitignore`.
