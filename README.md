
# gerenciador-service
Gerenciador, objetivo principal gerir sessões de votação de forma flexivel

```bash
$ git clone https://github.com/murilonerdx/gerenciador-service.git
$ cd gerenciador-service
$ mvn clean install
$ mvn clean package
$ mvn spring-boot:run

```


[![Build Status](https://app.travis-ci.com/murilonerdx/gerenciador-service.svg?branch=master)](https://app.travis-ci.com/murilonerdx/gerenciador-service)

Projeto atualmente em: **Spring Boot**

# Prometheus com Docker

Integração com com Grafana utilizando Prometheus.

**Prometheus**: um sistema de coleta de métricas de aplicações e serviços para armazenamento em um banco de dados de séries temporais. É muito eficiente. - AlertManager: trabalha de forma integrada com a Prometheus para avaliar regras de alerta e enviar notificações por e-mail, Jira, Slack, e outros sistemas suportados

OBS: GRAFANA, SONARQUBE, PROMETHEUS implementação com docker

**Grafana**: sendo uma solução de código aberto, também nos permite escrever plug-ins do zero para integração com várias fontes de dados diferentes. A ferramenta nos ajuda a estudar, analisar e monitorar dados ao longo de um período de tempo, tecnicamente chamado de análise de série temporal.
- http://localhost:8080/swagger-ui/index.html#/ (swagger)
- http://localhost:8080/actuator (actuator/info)
- http://localhost:9090/ (prometheus) 
- http://localhost:3000/ (grafana)
- prometheus.yml
- appfile.log
- logs/**

```
docker-compose up
```



## Prometheus + Grafana
Sistem de coleta de metricas processamento paineis, estabilidade, leitura de logs, integridade.

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

## Log4j
Guardando registros da aplicação mantendo a integridade da aplicação
- log4j.xml
- log4j2.xml
- logback-spring.xml
**application.properties**

```
management.info.git.mode=simple
logging.level.org.hibernate.SQL=DEBUG
```



## ActiveMQ
Recurso de mensageria


## Ambientes

| Nome | Endereço |
| ------ | ------ |
| Test/Dev | http://localhost:8080/swagger-ui/index.html# |
| Produção | https://gerenciador-service.herokuapp.com/swagger-ui/index.html# |


| Nome | Endereço |
| ------ | ------ |
| Test | https://github.com/murilonerdx/gerenciador-service/blob/master/src/main/resources/application-test.properties |
| Dev | https://github.com/murilonerdx/gerenciador-service/blob/master/src/main/resources/application-dev.properties |
| Produção | https://github.com/murilonerdx/gerenciador-service/blob/master/src/main/resources/application-prod.properties |

## Features não implementadas
feature v1: https://github.com/murilonerdx/gerenciador-service/tree/feature/security_implementation




