# 📖 Leitor de Versículos — JavaFX & SQLite

Aplicação desktop desenvolvida em **Java**, utilizando **JavaFX, JDBC e SQLite**, para consulta e apresentação de versículos bíblicos em igrejas e eventos.

O sistema permite pesquisar passagens em diferentes traduções, organizar uma sequência de slides e realizar apresentações em tela cheia, inclusive em um segundo monitor ou projetor.

## 🛠️ Tecnologias utilizadas

- **Java 17** — linguagem principal.
- **JavaFX 21** — interface gráfica.
- **SQLite** — armazenamento dos textos bíblicos.
- **JDBC** — comunicação com o banco de dados.
- **Maven** — gerenciamento de dependências e execução.
- **CSS** — personalização da interface.

## ✨ Funcionalidades

- Pesquisa de versículos por livro, capítulo e número.
- Seleção entre três traduções: NVI, ACF e NAA.
- Autocomplete dos livros bíblicos.
- Consulta de intervalos, como João 3:16-18.
- Adição de várias passagens à fila de apresentação.
- Seleção, remoção e limpeza dos slides.
- Prévia dos versículos selecionados.
- Apresentação em tela cheia com interface personalizada.
- Navegação contínua entre slides.
- Suporte à apresentação em monitor secundário.
- Controles por teclado e botão de encerramento.

## 🏗️ Organização do projeto

O projeto separa responsabilidades entre:

- `model` — representação dos dados.
- `database` e `repository` — conexão e consultas SQLite.
- `service` — lógica de pesquisa e preparação dos versículos.
- `ui` — janelas e componentes JavaFX.
- `resources` — estilos e recursos da aplicação.

Essa organização facilita a manutenção e a evolução do sistema.

## 🚀 Como executar

**Pré-requisitos:**

- JDK 17 ou compatível.
- Maven.
- Bancos SQLite configurados localmente.

Clone o projeto:

`git clone https://github.com/ArielGColeto/leitor-versiculos.git`

Entre no diretório:

`cd leitor-versiculos`

Execute:

`mvn clean javafx:run`

### Configuração dos bancos

A aplicação utiliza a pasta `db` na raiz do projeto, com os seguintes arquivos:

- `db/nvi.sqlite`
- `db/acf.sqlite`
- `db/naa.sqlite`

**Os bancos contendo os textos completos das traduções não acompanham este repositório**, devido à necessidade de respeitar os direitos de distribuição das traduções bíblicas.

Para executar as consultas, é necessário configurar localmente arquivos de banco compatíveis, obtidos ou preparados com a devida autorização.

## ⌨️ Controles da apresentação

| Tecla | Ação |
|---|---|
| → ou Espaço | Próximo slide |
| ← | Slide anterior |
| Home | Primeiro slide |
| End | Último slide |
| Esc | Encerrar apresentação |

A navegação é circular, permitindo avançar continuamente entre os slides cadastrados.

## 📌 Sobre o projeto

O sistema foi desenvolvido para atender a uma necessidade prática de apresentação de textos bíblicos.

Durante o desenvolvimento, foram aplicados conceitos de programação orientada a objetos, consultas SQL, manipulação de coleções, tratamento de eventos e desenvolvimento de interfaces desktop.

## 🔧 Melhorias futuras

- Personalização de temas e estilos de apresentação.
- Opções avançadas de configuração do projetor.
- Salvamento e carregamento de listas de apresentação.
- Ampliação dos testes automatizados.

---

**Desenvolvido por Ariel Grassi Coleto**

Estudante de Análise e Desenvolvimento de Sistemas, com foco em desenvolvimento Backend Java.
