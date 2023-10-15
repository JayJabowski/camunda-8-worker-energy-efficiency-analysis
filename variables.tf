variable "kube_config" {
  type    = string
  default = "~/.kube/config"
}

variable "create_camunda8" {
  type = bool
  default = false
}

variable "create_scaphandre" {
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

variable "create_kind" {
  type = bool
  default = false
}

variable "create_eks" {
  type = bool
  default = false
}

variable "measurment-namespace" {
  type = string
  default = "monitoring"
}

variable "app-namespace" {
  type = string
  default = "camunda8"
}