# DAC — Indicadores Educacionais de Mato Grosso do Sul

Projeto do 4º semestre de **Análise e Desenvolvimento de Sistemas** da **Universidade Católica Dom Bosco (UCDB)**, relacionado ao **ODS 4 — Educação de Qualidade**. O sistema reúne dados públicos de matrículas da rede estadual de Mato Grosso do Sul em um painel para consultar indicadores por município e acompanhar diferenças entre anos.

## Equipe

- Alexandre
- Henrique
- João Victor
- Lucatonni
- Pedro Henrique
- Reynaldo

## Como executar

Você precisa de **Java 17 ou superior**. Para recompilar ou executar pela IDE, use um **JDK 17 ou superior**. O banco SQLite, as bibliotecas e o aplicativo compilado já vêm no repositório. Mantenha a estrutura de pastas ao baixar o projeto.

**No Windows**, baixe o repositório em **Code → Download ZIP**, extraia o arquivo e abra `DAC-4-main/executar.bat`. O script procura o Java em `JAVA_HOME`, no `PATH` e na pasta de JDKs do IntelliJ.

**No Linux ou macOS**, clone o repositório e execute o JAR.

```sh
git clone https://github.com/reynaldojr77/dac-4-semestre.git
cd dac-4-semestre/DAC-4-main
java -jar dac.jar
```

Se já tiver baixado o ZIP, abra o terminal na pasta `DAC-4-main` e execute apenas `java -jar dac.jar`.

### Executar a partir do código-fonte

- No **IntelliJ IDEA**, abra `DAC-4-main` como projeto, selecione o JDK, marque `src` como **Sources Root** se necessário, adicione todos os JARs de `lib` em **Project Structure → Modules → Dependencies** e execute a classe `Main`.
- No **VS Code**, abra `DAC-4-main` com o **Extension Pack for Java**, selecione o JDK, confira os JARs de `lib` em **Java Projects → Referenced Libraries** e execute `src/Main.java`.

Para gerar um novo `dac.jar` após editar os fontes, use `compilar.bat` no Windows. No Linux ou macOS, execute estes comandos dentro de `DAC-4-main`.

```sh
mkdir -p build/classes
find src -name '*.java' | sed 's/.*/"&"/' > build/fontes.txt
javac --release 17 -encoding UTF-8 -cp 'lib/*' -d build/classes @build/fontes.txt
jar --create --file dac.jar --manifest manifest.mf -C build/classes .
```

## Como usar o painel

Selecione um **município** ou **Todos os municípios**, escolha o **Ano do painel** e clique em **Aplicar filtros**. Os cartões mostram o total de matrículas e os totais e percentuais de aprovação, reprovação e abandono.

| Aba | O que mostra |
| --- | --- |
| **Tabela** | Registros por unidade escolar. Clique nos cabeçalhos para ordenar. |
| **Gráfico por município** | Até dez municípios com mais matrículas no ano ou o total do município selecionado. |
| **Taxas gerais** | Percentuais de aprovação, reprovação e abandono para o recorte selecionado. |
| **Comparação entre anos** | Resumo, diferenças e gráfico de percentuais para dois anos no mesmo recorte geográfico. |

Na comparação, escolha o **ano-base** e o **ano comparado** independentemente do ano do painel. O sistema mostra a variação de matrículas em quantidade e em porcentagem sobre o ano-base. As diferenças entre percentuais são expressas em **pontos percentuais**. Quando falta um denominador válido, o percentual correspondente fica indisponível.

## Fonte e interpretação dos dados

O banco contém **3.703 registros**, distribuídos por **79 municípios** nos anos de **2018 a 2024 e 2026**. O ano de **2025** está ausente na fonte consultada pelo grupo. Cada registro representa dados agregados por unidade escolar. [Matrículas por unidade escolar — Portal de Dados Abertos de MS](https://www.dados.ms.gov.br/dataset/matriculas-por-unidade-escolar)

Os percentuais de aprovação, reprovação e abandono dividem o total de cada situação pelo total de matrículas do recorte. Esses percentuais descrevem os registros da base consultada. As taxas oficiais de rendimento escolar podem usar outra metodologia.

- Em **2024**, 184.949 das 220.926 matrículas constam como **cursando**.
- Em **2026**, aprovação, reprovação e abandono estão zerados nos registros disponíveis. A maior parte das matrículas ainda consta como **cursando**, por isso os valores devem ser lidos como parciais.
- O abandono aparece zerado nos anos disponíveis a partir de **2020**. A base, por si só, não permite concluir que deixou de ocorrer.

## Banco e estrutura do projeto

O aplicativo abre `DAC-4-main/database/dac_ods4.db` em modo somente leitura. Na inicialização, `MatriculaDAO` consulta as tabelas de municípios, unidades escolares e registros educacionais. O controlador aplica os filtros e calcula os indicadores, e a interface exibe os resultados. Os CSVs de 2024 e 2026 servem para conferência da fonte.

| Caminho | Conteúdo |
| --- | --- |
| [DAC-4-main/src/](DAC-4-main/src/) | Código Java da aplicação, incluindo conexão, consultas, controlador e interface Swing. |
| [DAC-4-main/database/](DAC-4-main/database/) | Banco SQLite usado pelo painel. |
| [DAC-4-main/lib/](DAC-4-main/lib/) | Bibliotecas SQLite JDBC, JFreeChart e SLF4J. |
| [DAC-4-main/LEIA-ME.md](DAC-4-main/LEIA-ME.md) | Início rápido e instruções para a IDE. |
| [docs/TESTES.md](docs/TESTES.md) | Roteiro de testes e valores de referência. |
| [sql/](sql/) | Documentação do banco e consultas SQL de referência. |

Para usar uma cópia do banco em outro local, configure a opção de VM `-Ddac.banco=CAMINHO` ou a variável de ambiente `DAC_BANCO`.

## Como verificar a carga

Na pasta `DAC-4-main`, rode o comando abaixo para consultar o banco sem abrir o painel.

```sh
java -jar dac.jar --verificar-banco
```

A saída deve listar **3.703 registros** e os **oito anos disponíveis**. Para uma conferência rápida pela interface, selecione **2026**. O painel deve mostrar **199.489 matrículas** para todos os municípios e **46.121** para Campo Grande. Para os demais cenários, consulte o [roteiro de testes](docs/TESTES.md).
