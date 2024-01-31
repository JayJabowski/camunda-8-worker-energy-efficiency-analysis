resource "helm_release" "kepler" {
  name = "kepler"
  chart = "kepler"
  repository = "https://sustainable-computing-io.github.io/kepler-helm-chart"
  namespace = var.namespace
  count = var.create_module ? 1 : 0

  values = [
    "${file("./configs/kepler-values.yaml")}"
  ]


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
