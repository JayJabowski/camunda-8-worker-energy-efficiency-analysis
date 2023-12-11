resource "helm_release" "camunda-platform" {
  name       = "camunda-platform"
  chart      = "camunda-platform"
  version    = "8.3.0"
  repository = "https://helm.camunda.io"
  namespace  = var.namespace
  count      = var.create_module ? 1 : 0

  values = [
    "${file("./configs/camunda-platform-core-kind-values.yaml")}"
  ]
}

resource "helm_release" "console-worker" {
  name       = "console-worker"
  chart      = "console-worker-chart"
  repository = "https://maxbehr801.github.io/helmrepotest"
  namespace  = var.namespace
  version    = "0.0.2"
  count      = var.create_module ? 1 : 0
}

resource "kubernetes_job_v1" "bpmnmodeldeployment" {
  count = 0 // var.create_module ? 1 : 0
  depends_on = [ kubernetes_config_map_v1.bpmnmodel, helm_release.camunda-platform, helm_release.console-worker ]
  metadata {
    name = "bpmndeployment"
    namespace = var.namespace
  }
  spec {
    template {
      metadata {}
      spec {
        container {
          name  = "bpmndeployment"
          image = "sitapati/zbctl:8.2.10"
          

          env {
            name  = "ZEEBE_ADDRESS"
            value = "camunda-platform-zeebe-gateway:26500"
          }

          command = ["/zbctl"]
          args = [ "deploy", "resource", "/process/example_process.bpmn" , "--insecure"]

          // for future testing purposes I'll leave this here: 
          //image = "alpine:3.14"
          //command = ["bin/cat"]
          //args = [ "/process/example_process.bpmn" ]

          volume_mount {
            name = "bpmnmodelvolume"
            mount_path = "/process"
          }
        }

        restart_policy = "Never"

        volume {
          name = "bpmnmodelvolume"
          config_map {
            name = "bpmnmodeldata"
          }
        }
      }
    }
    backoff_limit = 1
  }
  wait_for_completion = true
}

resource "kubernetes_config_map_v1" "bpmnmodel" {
  count = var.create_module ? 1 : 0
  metadata {
    name = "bpmnmodeldata"
    namespace = var.namespace
  }

  data = {
    "example_process.bpmn" = file("${abspath(path.module)}/process/example_process.bpmn")
  }
  
}

resource "kubernetes_job_v1" "processstart" {
  count = 0 // var.create_module ? 1 : 0
  depends_on = [ kubernetes_job_v1.bpmnmodeldeployment ]
  metadata {
    name = "processstart"
    namespace = var.namespace
  }
  spec {
    template {
      metadata {}
      spec {
        container {
          name  = "processtart"
          image = "sitapati/zbctl:8.2.10"
          

          env {
            name  = "ZEEBE_ADDRESS"
            value = "camunda-platform-zeebe-gateway:26500"
          }

          command = ["/zbctl"]
          args = [ "create", "instance", "Sample_Process" , "--insecure"]
        }

        restart_policy = "Never"
      }
    }
    backoff_limit = 1
  }
  wait_for_completion = true
}