module "camunda8" {
  source        = "./modules/camunda8"
  namespace     = var.app-namespace
  create_module = true
  depends_on    = [kubernetes_namespace.app-namespace]
}

module "prometheus" {
  source        = "./modules/prometheus"
  create_module = true
  namespace     = var.measurement-namespace
  depends_on    = [kubernetes_namespace.measurement-namespace]
}

module "kepler" {
  source        = "./modules/kepler"
  namespace     = var.measurement-namespace
  create_module = true
  use_emulation = false
  depends_on    = [module.prometheus, kubernetes_namespace.measurement-namespace]
}

module "worker" {
  source                = "./modules/worker"
  namespace             = var.worker-namespace
  create_module         = true
  start_load_controller = true
  depends_on            = [kubernetes_namespace.worker-namespace, module.camunda8]
}

module "Measuring-Endpoints" {
  source        = "./modules/measuring-endpoints"
  namespace     = var.measuring-endpoints-namespace
  create_module = true
}