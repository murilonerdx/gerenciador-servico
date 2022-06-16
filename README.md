
# gerenciador-service
Gerenciador, objetivo principal gerir sessões de votação de forma flexivel

[![Build Status](https://app.travis-ci.com/murilonerdx/gerenciador-service.svg?branch=master)](https://app.travis-ci.com/murilonerdx/gerenciador-service)

Projeto atualmente em: **Spring Boot**

# Prometheus com Docker

Integração com com Grafana utilizando Prometheus.

**Prometheus**: um sistema de coleta de métricas de aplicações e serviços para armazenamento em um banco de dados de séries temporais. É muito eficiente. - AlertManager: trabalha de forma integrada com a Prometheus para avaliar regras de alerta e enviar notificações por e-mail, Jira, Slack, e outros sistemas suportados

**Grafana**: sendo uma solução de código aberto, também nos permite escrever plug-ins do zero para integração com várias fontes de dados diferentes. A ferramenta nos ajuda a estudar, analisar e monitorar dados ao longo de um período de tempo, tecnicamente chamado de análise de série temporal.

- http://localhost:9090/ (prometheus)
- http://localhost:3000/ (grafana)
- prometheus.yml
- appfile.log

```
# my global config
global:
  scrape_interval: 15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
    - static_configs:
        - targets:
          # - alertmanager:9093

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: "prometheus"

    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.

    static_configs:
      - targets: ["localhost:9090"]
  - job_name: 'spring-actuator'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
```


## Spring Actuator
Verificação e integridade da aplicação
**application.properties**

```
management.endpoint.health.show-details=always
management.endpoint.info.enabled=true
management.endpoints.web.exposure.include=info, health, metrics, prometheus
```

## Ambientes

| Nome | Endereço |
| ------ | ------ |
| Test | http://localhost:8080/swagger-ui/index.html# |
| Produção |  |


| Nome | Endereço |
| ------ | ------ |
| Test | https://github.com/murilonerdx/gerenciador-service/blob/master/src/main/resources/application-test.properties |
| Produção |  |




