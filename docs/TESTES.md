# Conferência da integração

1. Execute DAC-4-main/executar.bat ou src/Main.java no IntelliJ com os quatro JARs de lib associados.
2. O console deve informar Origem: SQLite, 3.703 registros e os anos 2018–2024 e 2026.
3. O seletor Ano do painel deve oferecer os oito anos. 2025 não deve aparecer como se tivesse registros.
4. Todos os municípios em 2026: 199.489 matrículas; Campo Grande: 46.121; Corumba: 6.137.
5. Todos os municípios em 2018: 326.367 matrículas; aprovados 211.088; reprovados 37.938; abandono 10.490.
6. Compare 2024 com 2026 para Todos os municípios: 220.926 → 199.489, diferença -21.437 e cerca de -9,70%.
7. Compare 2018 com 2019: os valores e percentuais devem acompanhar os anos sem mudar o Ano do painel.
8. Clique em Matrículas na tabela e confira a ordenação numérica. Aplicar filtros e Comparar repetidamente não devem duplicar registros ou séries.
9. Compare o mesmo ano e confira diferenças zero e o aviso correspondente.
10. Para testar sem CSV, copie a pasta completa para um local temporário, remova apenas os CSVs dessa cópia e execute: os resultados devem ser iguais.

## Verificação sem interface

Na pasta DAC-4-main, execute:

```sh
java -jar dac.jar --verificar-banco
```

Deve listar todos os anos e terminar com Consulta ao SQLite concluída. Nenhum CSV foi carregado.

Os valores refletem os arquivos de origem. Dados parciais, alunos cursando e zeros de abandono precisam ser contextualizados no artigo e na demonstração.
