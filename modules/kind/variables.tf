variable "create_module" {
  type    = bool
  default = false
}

variable "kube_config" {
  type    = string
  default = "~/.kube/config"
}

variable "set_kubecfg" {
  type = bool
  default = false
}