# docker-registry

![Version: 1.9.9](https://img.shields.io/badge/Version-1.9.9-informational?style=flat-square) ![AppVersion: 2.8.1](https://img.shields.io/badge/AppVersion-2.8.1-informational?style=flat-square)

Fork from stable Docker Registry chart

**Homepage:** <https://hub.docker.com/_/registry/>

## Installation

### Add Helm repository

```shell
helm repo add gmelillo https://helm.melillo.me
helm repo update
```

### Install

```shell
helm install --generate-name gmelillo/docker-registry
```

## Source Code

* <https://github.com/docker/distribution-library-image>

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` |  |
| configData.health.storagedriver.enabled | bool | `true` |  |
| configData.health.storagedriver.interval | string | `"10s"` |  |
| configData.health.storagedriver.threshold | int | `3` |  |
| configData.http.addr | string | `":5000"` |  |
| configData.http.headers.X-Content-Type-Options[0] | string | `"nosniff"` |  |
| configData.log.fields.service | string | `"registry"` |  |
| configData.storage.cache.blobdescriptor | string | `"inmemory"` |  |
| configData.version | float | `0.1` |  |
| extraVolumeMounts | list | `[]` |  |
| extraVolumes | list | `[]` |  |
| image.pullPolicy | string | `"IfNotPresent"` |  |
| image.repository | string | `"registry"` |  |
| image.tag | string | `"2.8.1"` |  |
| ingress.annotations | object | `{}` |  |
| ingress.enabled | bool | `false` |  |
| ingress.hosts[0] | string | `"chart-example.local"` |  |
| ingress.labels | object | `{}` |  |
| ingress.path | string | `"/"` |  |
| ingress.tls | string | `nil` |  |
| nodeSelector | object | `{}` |  |
| persistence.accessMode | string | `"ReadWriteOnce"` |  |
| persistence.enabled | bool | `false` |  |
| persistence.size | string | `"10Gi"` |  |
| podAnnotations | object | `{}` |  |
| podDisruptionBudget | object | `{}` |  |
| podLabels | object | `{}` |  |
| priorityClassName | string | `""` |  |
| replicaCount | int | `1` |  |
| resources | object | `{}` |  |
| secrets.haSharedSecret | string | `""` |  |
| secrets.htpasswd | string | `""` |  |
| securityContext.enabled | bool | `true` |  |
| securityContext.fsGroup | int | `1000` |  |
| securityContext.runAsUser | int | `1000` |  |
| service.annotations | object | `{}` |  |
| service.name | string | `"registry"` |  |
| service.port | int | `5000` |  |
| service.type | string | `"ClusterIP"` |  |
| storage | string | `"filesystem"` |  |
| tolerations | list | `[]` |  |
| updateStrategy | string | `nil` |  |