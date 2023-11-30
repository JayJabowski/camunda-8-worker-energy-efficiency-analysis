module "camunda8" {
  source        = "./modules/camunda8"
  namespace     = var.app-namespace
  create_module = true
  depends_on    = [kubernetes_namespace.app-namespace]
}

module "scaphandre" {
  source        = "./modules/scaphandre"
  create_module = false
  namespace     = var.measurment-namespace
  depends_on    = [kubernetes_namespace.measurement-namespace]
}

module "grafana" {
  source        = "./modules/grafana"
  create_module = true
  namespace     = var.measurment-namespace
  depends_on    = [kubernetes_namespace.measurement-namespace]
}

module "kepler" {
  source        = "./modules/kepler"
  namespace     = var.measurment-namespace
  create_module = true
  // set to true on some system, not sure if it works as intended
  use_emulation = false
  depends_on    = [module.grafana, kubernetes_namespace.measurement-namespace]
}

module "kind" {
  source        = "./modules/kind"
  create_module = false
  set_kubecfg   = false
  kube_config = var.kube_config
}

module "worker" {
  source                = "./modules/worker"
  namespace             = var.worker-namespace
  create_module         = true
  start_load_controller = true
  depends_on            = [kubernetes_namespace.worker-namespace, module.camunda8]
}

module "Docker-Registry" {
  source        = "./modules/docker-registry"
  namespace     = var.docker-registry-namespace
  create_module = false
}

module "Measuring-Endpoints" {
  source        = "./modules/measuring-endpoints"
  namespace     = var.measuring-endpoints-namespace
  create_module = true
}