resource "helm_release" "kepler" {
  name = "kepler"
  chart = "kepler"
  repository = "https://sustainable-computing-io.github.io/kepler-helm-chart"
  namespace = var.namespace
  count = var.create_module ? 1 : 0

  set {
    name = "serviceMonitor.enabled"
    value = true
  }

  set {
    name = "serviceMonitor.labels.release"
    value = "kube-prometheus-stack"
  }

  set {
    name = "serviceMonitor.labels.app"
    value = "kube-prometheus-stack"
  }

  set {
    name = "extraEnvVars.MODEL_SERVER_ENABLE"
    value = var.use_emulation
  }

  set {
    name = "extraEnvVars.KEPLER_LOG_LEVEL"
    value = 5
  }

  set {
    name = "extraEnvVars.ENABLE_GPU"
    value = !var.use_emulation
  }

  set {
    name = "extraEnvVars.ENABLE_EBPF_CGROUPID"
    value = !var.use_emulation
  }

  set {
    name = "extraEnvVars.EXPOSE_IRQ_COUNTER_METRICS"
    value = !var.use_emulation
  }

  set {
    name = "extraEnvVars.EXPOSE_KUBELET_METRICS"
    value = !var.use_emulation
  }

  set {
    name = "extraEnvVars.ENABLE_PROCESS_METRICS"
    value = !var.use_emulation
  }

}

resource "kubernetes_config_map" "model_server" {
  metadata {
    name = "kepelr-model-server"
    namespace = var.namespace
  }
  count = var.create_module && var.use_emulation ? 1 : 0


  data = {
    MODEL_SERVER_ENABLE = true
    MODEL_SERVER_ENDPOINT = "http://kepler-model-server.${var.namespace}.svc.cluster.local:8099/model"
    MODEL_SERVER_PORT = 8099
    MODEL_SERVER_URL = "http://kepler-model-server.${var.namespace}.svc.cluster.local:8099"
    MODEL_SERVER_MODEL_REQ_PATH = "/model"
    MODEL_SERVER_MODEL_LIST_PATH = "/best-models"
  }

}

resource "kubernetes_config_map_v1" "kepler_grafana_dashboards" {
  metadata {
    name      = "kepler-grafana-dashboard"
    namespace = var.namespace
    labels = {
      grafana_dashboard : "1"
    }
  }
  data = {
    "Kepler-Exporter.json" = file("${abspath(path.module)}/dashboard/Kepler-Exporter.json")
  }

  depends_on = [ helm_release.kepler ]
}
