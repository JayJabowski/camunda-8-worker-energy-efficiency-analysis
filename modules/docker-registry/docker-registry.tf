resource "helm_release" "docker-registry-local" {
  name       = "docker-registry-local"
  chart      = "./modules/docker-registry/"
  namespace  = var.namespace
  version    = 0.1
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./configs/docker-registry-values.yaml")}"
  ]
}