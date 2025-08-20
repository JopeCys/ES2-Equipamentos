# SCB-Equipamentos

## Descrição
Este microsserviço é parte de um projeto de desenvolvimento em equipe de um sistema de gerenciamento de alugueis de bicicletas feito para a disciplina Engenharia de Software 2. Cada integrante ficou responsável por um dos microsserviços (scb-aluguel, scb-equipamentos e scb-externo), sendo gerenciados por um tech lead (o grandioso chibeg).

## Funcionalidades
- Cadastro e mantenimento de Totems.
- Cadastro e mantenimento de Trancas.
- Cadastro e mantenimento de Bicicletas.
- Controle de operações envolvendo os equipamentos.

## Tecnologias Utilizadas
- **Linguagem de Programação**: Java 17
- **Frameworks e Bibliotecas**: Spring Boot 3.3.2, Spring Data JPA, Spring Boot Starter Web, Spring Cloud OpenFeign, Lombok, Spring Boot Starter Validation, Hibernate (para SQLite), Spring Boot DevTools
- **Ferramentas de Teste**: JUnit, Mockito, Jacoco
- **Banco de Dados**: SQLite
- **Ferramentas de Build**: Maven
- **Ferramentas de CI/CD**: GitLab CI/CD, SonarQube Scanner
- **Ferramentas de Deployment**: AWS CodeDeploy, Shell Script

## Arquitetura do Microsserviço
- **Models**: Contém as classes que representam as entidades no banco de dados.
- **Repository**: Interfaces que lidam com a persistência de dados, estendendo `JpaRepository`.
- **Controllers**: Classes que lidam com as requisições HTTP e coordenam as respostas.
- **Services**: Classes que contém a lógica de negócio a ser executada por um determinado endpoint.
- **Clients**: Responsáveis por fazer chamadas a outros microsserviços.

## Pré-requisitos
- **Java Development Kit (JDK) 17**
- **Apache Maven**
- **Microserviço `scb-aluguel`**: Necessário para comunicação externa.
- **Microserviço `scb-externo`**: Necessário para comunicação externa.
- **Serviço AWS S3**: Necessário para o armazenamento do artefato de implantação.
- **Serviço AWS CodeDeploy**: Necessário para a implantação automatizada.
- **Sistema de Gerenciamento de Serviços**: `systemctl`, utilizado nos scripts de deploy para iniciar e parar a aplicação.

## Como testar?
Não é necessário ter uma instância rodando na AWS para testar os endpoints. Basta rodar localmente o microsserviço e utilizar o Postman para chamá-los com localhost:80

OBS: Serviços como envio de email não irão funcionar devido a não existência de instâncias dos outros microsserviços na AWS
