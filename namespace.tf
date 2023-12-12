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

resource "kubernetes_namespace" "docker-registry-namespace" {
  metadata {
    name = var.docker-registry-namespace
  }
  depends_on = [ module.kind ]
}

resource "kubernetes_namespace" "measuring-endpoints-namespace" {
  metadata {
    name = var.measuring-endpoints-namespace
  }
  depends_on = [ module.kind ]
}

resource "kubernetes_namespace" "load-controller-namespace" {
  metadata {
    name = var.load-controller-namespace
  }
  depends_on = [ module.kind ]
}