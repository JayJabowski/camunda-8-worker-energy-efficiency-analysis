resource "helm_release" "scaphandre" {
  name       = "scaphandre"
  chart      = "./helm/scaphandre"
  namespace  = var.namespace
  count = var.create_module ? 1 : 0
}

resource "kubernetes_config_map" "scaphandre-config" {
  metadata {
    name = "scaphandre-config"
  }

  count = var.create_module ? 1 : 0

  data = {
    "scaphandre-dashboard.json" = "${file("./configs/grafana-kubernetes-dashboard.json")}" 
  }
}

resource "kubernetes_namespace" "scaphandre" {
  metadata {
    name = var.namespace
  }
  count = var.create_module ? 1 : 0
}
