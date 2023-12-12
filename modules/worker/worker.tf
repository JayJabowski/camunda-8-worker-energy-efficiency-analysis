resource "helm_release" "restworkerjava" {
  name       = "restworkerjava"
  chart      = "./modules/worker/restworkerjava"
  namespace  = var.namespace
  version    = 0.1
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./configs/restworkerjava-values.yaml")}"
  ]
}