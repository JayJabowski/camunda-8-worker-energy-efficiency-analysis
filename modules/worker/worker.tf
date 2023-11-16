resource "helm_release" "load-controller" {
  name       = "load-controller"
  chart      = "./modules/worker/load-controller"
  namespace  = var.namespace
  version    = 0.1
  count      = var.start_load_controller ? 1 : 0

  values = [
    "${file("./modules/worker/load-controller/values.yaml")}"
  ]
}

resource "helm_release" "restworkerjava" {
  name       = "restworkerjava"
  chart      = "./modules/worker/restworkerjava"
  namespace  = var.namespace
  version    = 0.1
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./modules/worker/restworkerjava/values.yaml")}"
  ]
}