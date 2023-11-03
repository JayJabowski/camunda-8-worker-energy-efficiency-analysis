resource "helm_release" "simple-java-print-worker" {
  name       = "simple-java-print-worker"
  chart      = "./modules/worker/"
  namespace  = var.namespace
  version    = 0.1
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./modules/worker/values.yaml")}"
  ]
}