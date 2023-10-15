/* resource "helm_release" "prometheus" {
  name       = "prometheus"
  chart      = "kube-prometheus-stack"
  repository = "https://prometheus-community.github.io/helm-charts"
  namespace  = var.namespace
  count = var.create_module ? 1 : 0

  set {
    name = "alertmanager.persistentVolume"
    value = "false"
  }

  set {
    name = "server.persistentVolume.enabled"
    value = "false"
  }
  
} */

resource "helm_release" "prometheus" {
  name       = "kube-prometheus-stackr"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  namespace  = var.namespace
  create_namespace = true
  version    = "45.7.1"
  count = var.create_module ? 1 : 0


  values = [
    "${file("./configs/values.yaml")}"
  ]
  timeout = 2000
  

  set {
    name  = "podSecurityPolicy.enabled"
    value = true
  }

  set {
    name  = "server.persistentVolume.enabled"
    value = false
  }

  set {
    name ="prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues"
    value = false
  }

  # You can provide a map of value using yamlencode. Don't forget to escape the last element after point in the name
  set {
    name = "server\\.resources"
    value = yamlencode({
      limits = {
        cpu    = "200m"
        memory = "50Mi"
      }
      requests = {
        cpu    = "100m"
        memory = "30Mi"
      }
    })
  }
  
}