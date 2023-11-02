resource "kubernetes_namespace" "app-namespace" {
  metadata {
    name = var.app-namespace
  }
  depends_on = [ module.kind ]
}

resource "kubernetes_namespace" "measurement-namespace" {
  metadata {
    name = var.measurment-namespace
  }

  depends_on = [ module.kind ]
}

resource "kubernetes_namespace" "worker-namespace" {
  metadata {
    name = var.worker-namespace
  }
  depends_on = [ module.kind ]
}