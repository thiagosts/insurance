
# Insurance Application

## Introdução
Este projeto é uma aplicação para gerenciamento de cotações de seguros, implementada com arquitetura **hexagonal**, que separa a lógica de negócios da infraestrutura. A aplicação é desenvolvida utilizando **Spring Boot**, com suporte a mensageria via **RabbitMQ**, **Resilience4j** para resiliência, e **PostgreSQL** como banco de dados relacional.

## Arquitetura Hexagonal
A arquitetura hexagonal facilita a manutenção e escalabilidade, permitindo a troca de implementações sem impactar o núcleo do sistema.

### Vantagens:
- **Independência tecnológica**: A lógica de negócios não depende de frameworks específicos.
- **Flexibilidade**: Adaptações na infraestrutura não afetam o domínio.
- **Facilidade de testes**: Simulações e testes são mais simples devido à separação clara de responsabilidades.

---

## Tecnologias Utilizadas
- **RabbitMQ**: Sistema de mensageria para comunicação assíncrona.
- **Resilience4j**: Implementação de padrões de resiliência como _Circuit Breaker_ e _Rate Limiting_.
- **PostgreSQL**: Banco de dados relacional escalável.
- **WireMock**: Mock de serviços externos para testes.
- **Spring Boot**: Framework principal da aplicação.

---

## Configurações do RabbitMQ
O **RabbitMQ** é configurado para garantir resiliência e recuperação de mensagens em caso de falhas.

### Definições de Filas e Exchanges:
- **Filas**:
    - `insurance_quota_received`: Recebe mensagens relacionadas às cotações.
    - `insurance_policy_created`: Recebe mensagens relacionadas a políticas criadas.
    - `manual_fallback_queue`: Fila para cenários de fallback manual.
    - `insurance_quota_received.dlq`: Fila _dead-letter_ para mensagens não processadas de `insurance_quota_received`.
    - `insurance_policy_created.dlq`: Fila _dead-letter_ para mensagens não processadas de `insurance_policy_created`.

- **Exchanges**:
    - `quotation.exchange`: Exchange principal para mensagens de cotações.
    - `manual_fallback_exchange`: Exchange para fallback manual.
    - `quotation.dlx`: Exchange para _dead-letters_.

### Problema Resolvido:
- Garantia de que mensagens que falham no processamento sejam redirecionadas para filas de _dead-letter_, permitindo análise posterior e reprocessamento.
- Isolamento de mensagens relacionadas a diferentes processos, como cotações e políticas.

---

## Testes
### Tipos de Testes Implementados:
1. **Testes Unitários**:
    - Cobrem casos de uso isolados no domínio e adapters.
2. **Testes de Integração**:
    - Validam a integração com RabbitMQ, PostgreSQL e outros serviços externos.
3. **Validações via Spring**:
    - Garante consistência nos dados de entrada.
4. **Tratamento de Exceções**:
    - Erros são tratados e retornados em formato padronizado.

---

## Como Executar

### Pré-requisitos
- **Docker** e **Docker Compose**
- **JDK 17**

### Passos
1. Clone o repositório:
   ```bash
   git clone [https://github.com/thiagosts/insurance.git]
   cd insurance-app
   ```

2. Suba os serviços com Docker Compose:
   ```bash
   docker-compose up
   ```

3. Acesse a aplicação:
    - **API**: [http://localhost:8080](http://localhost:8080)
    - **RabbitMQ Dashboard**: [http://localhost:15672](http://localhost:15672)  
      Usuário: `guest` | Senha: `guest`

---

## Exemplos de Uso

### Criar Cotação
```bash
curl --location 'http://localhost:8080/quotations' \
--header 'Content-Type: application/json' \
--data-raw '{
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "bdc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000.00,
    "coverages": {
        "Incêndio": 250000.00,
        "Desastres naturais": 500000.00,
        "Responsabiliadade civil": 75000.00
    },
    "assistances": [
        "Encanador",
        "Eletricista",
        "Chaveiro 24h"
    ],
    "customer": {
        "document_number": "36205578900",
        "name": "John Wick",
        "type": "NATURAL",
        "gender": "MALE",
        "date_of_birth": "1973-05-02",
        "email": "johnwick@gmail.com",
        "phone_number": 11950503030
    }
}'
```

## Pós-Requisição de Cotação

Após realizar a requisição de cotação, siga os passos abaixo:

1. Acesse o RabbitMQ Management pelo endereço: [http://localhost:15672/](http://localhost:15672/).

2. Publicar uma mensagem na fila `insurance_policy_created` com o seguinte payload:

```json
{
    "quotation_id": ID_RETORNADO_NA_RESPOSTA,
    "policy_number": GERAR_NUMERO_PARA_SIMULAR
}
```

3. Após publicar a mensagem, utilize o seguinte endereço para consultar a cotação com o número da `policy_number` atualizado:

```bash
GET http://localhost:8080/quotations/ID_RETORNADO_NA_RESPOSTA
```

Substitua `ID_RETORNADO_NA_RESPOSTA` pelo ID retornado na resposta da requisição de cotação.

### Consultar Cotação
```bash
curl --location 'http://localhost:8080/quotations/1'
```

---

Para dúvidas ou contribuições, abra um _issue_ neste repositório.
