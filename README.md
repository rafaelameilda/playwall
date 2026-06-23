# TV-Wall

O **TV-Wall** é uma aplicação de digital signage para cadastrar ambientes, grupos de telas, TVs, conteúdos e playlists. O projeto também oferece um player para exibir imagens e vídeos em TVs individuais ou em uma composição de video wall.

## Tecnologias

- Backend: Java 17, Spring Boot 3.2.5, Spring Data JPA e PostgreSQL.
- Frontend: HTML, JavaScript, jQuery e Tailwind CSS.
- Build: Maven.

## Estrutura do projeto

```text
tvwall/
├── backend/    # API REST e persistência
└── frontend/   # Painel administrativo e player das TVs
```

## Requisitos

- Java JDK 17.
- Maven 3.9 ou superior.
- PostgreSQL.
- Python 3 ou a extensão Live Server do VS Code para servir o frontend.

## Preparação do banco de dados

Crie o banco e o schema usados no ambiente de desenvolvimento:

```sql
CREATE DATABASE prismadb;
```

Depois, conectado ao banco `prismadb`:

```sql
CREATE SCHEMA IF NOT EXISTS prisma AUTHORIZATION postgres;
```

Configure o usuário e a senha locais em:

```text
backend/src/main/resources/application-dev.properties
```

Não envie esse arquivo com credenciais reais para um repositório público.

> Atenção: a configuração atual usa `spring.jpa.hibernate.ddl-auto=create`. Portanto, as tabelas são recriadas sempre que o backend inicia, apagando os dados anteriores. Para preservar dados durante o desenvolvimento, avalie usar `update`.

## Executando o backend

Na raiz do projeto:

```powershell
cd backend
mvn spring-boot:run
```

O backend ficará disponível em:

```text
http://localhost:8090/tv-wall
```

Também é possível gerar e executar o pacote:

```powershell
cd backend
mvn clean package -DskipTests
java -jar target/tv-wall-backend-0.0.1-SNAPSHOT.jar
```

## Executando o frontend

Mantenha o backend em execução. Em outro terminal, na raiz do projeto:

```powershell
python -m http.server 5501 --directory frontend
```

Abra no navegador:

```text
http://localhost:5501
```

Como alternativa, abra a pasta `frontend` no VS Code e execute o arquivo `index.html` com a extensão **Live Server**.

## Páginas principais

- Controle do TV-Wall: `http://localhost:5501/dashboard.html`
- Painel de visualização: `http://localhost:5501/tvwall.html`
- Player de uma TV: `http://localhost:5501/tv.html?id=ID_DA_TV`

## Desenvolvedor

**Wagner de Almeida Silveira**

- Suporte: [44 9 9876 5432](tel:+5544998765432)
- Site: [www.wagneralmeida.com.br](https://www.wagneralmeida.com.br)
- Instagram: [@as__wagner](https://www.instagram.com/as__wagner)

