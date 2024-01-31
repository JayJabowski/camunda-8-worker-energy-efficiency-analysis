resource "helm_release" "camunda-platform" {
  name       = "camunda-platform"
  chart      = "camunda-platform"
  version    = "8.3.0"
  repository = "https://helm.camunda.io"
  namespace  = var.namespace
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./configs/camunda-platform-core-kind-values.yaml")}"
  ]
}
