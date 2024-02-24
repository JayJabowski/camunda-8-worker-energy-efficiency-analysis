resource "kubernetes_namespace" "app-namespace" {
  metadata {
    name = var.app-namespace
  }
}

resource "kubernetes_namespace" "measurement-namespace" {
  metadata {
    name = var.measurement-namespace
  }
}

resource "kubernetes_namespace" "worker-namespace" {
  metadata {
    name = var.worker-namespace
  }
}

resource "kubernetes_namespace" "measuring-endpoints-namespace" {
  metadata {
    name = var.measuring-endpoints-namespace
  }
}
