resource "helm_release" "measuring-endpoints" {
  name       = "measuring-endpoints"
  chart      = "./modules/measuring-endpoints/"
  namespace  = var.namespace
  version    = 0.1
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./configs/test-responder-values.yaml")}"
  ]
}