# Monitoring and Health Check using Prometheus

### How to use
Run the docker-compose file 
```
docker-compose up
```

if you intend to use the demo applications given, you will need to create docker images for both the applications. Below is an example for creating a docker image for one of the applications.

```
mvn clean package
docker build -t spring-app/alert-demo-one .
```

The compose file will set up a service discovery emvironment using Consul by Hashicorp, along with monitoring and alerting for all the services using Prometheus, Grafana and Alert Manager.

### [Prometheus](https://github.com/prometheus/prometheus/blob/main/README.md#prometheus)
Prometheus is a time series database and a system monitoring service which scrapes and exposes application metrics at `/actuator/prometheus` endpoint.

### [Grafana](https://github.com/grafana/grafana#readme)
Grafana allows you to query, visualize, alert on and understand your metrics no matter where they are stored.

## References
https://prometheus.io/docs/introduction/overview/
https://grafana.com/docs/
https://www.consul.io/docs
https://docs.datadoghq.com/


