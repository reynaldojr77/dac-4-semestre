# Banco SQLite

O arquivo usado é ../DAC-4-main/database/dac_ods4.db, fornecido por Pedro e preservado sem alterações. Não precisa instalar PostgreSQL nem criar tabelas para executar o sistema. Pode ser inspecionado no DB Browser for SQLite.

A aplicação abre somente para leitura e consulta municipio, unidade_escolar e registro_educacional. O resultado vira List<Matricula>; os filtros e cálculos permanecem no controller. Consultas para alimentar a interface estão em MatriculaDAO.java.

A conexão interrompe a carga em caso de coluna quantitativa ausente/inválida, erro SQL ou perda de registros no JOIN. Não usa CSV como fallback nem cria um banco substituto.

Pedro deve manter o procedimento de coleta/importação para reconstruir o banco; o arquivo pronto não contém necessariamente o script original.
