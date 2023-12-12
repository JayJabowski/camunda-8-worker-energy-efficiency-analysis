resource "helm_release" "load-controller" {
  name       = "load-controller"
  chart      = "./modules/worker/load-controller"
  namespace  = var.namespace
  version    = 0.1
  count      = var.start_load_controller ? 1 : 0

  values = [
    "${file("./configs/load-controller-values.yaml")}"
  ]
}