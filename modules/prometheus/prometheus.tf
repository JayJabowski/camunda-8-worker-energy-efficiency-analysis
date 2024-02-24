resource "helm_release" "prometheus" {
  name       = "kube-prometheus-stack"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  namespace  = var.namespace
  create_namespace = true
  version    = "45.7.1"
  count = var.create_module ? 1 : 0


  values = [
    "${file("./configs/prometheus-values.yaml")}"
  ]

}
