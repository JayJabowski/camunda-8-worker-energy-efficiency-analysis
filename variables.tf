variable "kube_config" {
  type    = string
  default = "~/.kube/config"
}

variable "create_camunda8" {
  type = bool
  default = false
}

variable "create_grafana" {
  type = bool
  default = false
}

variable "create_kepler" {
  type = bool
  default = false
}

variable "measurement-namespace" {
  type = string
  default = "monitoring"
}

variable "app-namespace" {
  type = string
  default = "camunda8"
}

variable "worker-namespace" {
  type= string
  default = "worker"
}

variable "measuring-endpoints-namespace" {
  type= string
  default = "measuring-endpoints"
}

variable "create-load-controller" {
  type= bool
  default = false
}