variable "namespace" {
  type    = string
  default = "worker"
}

variable "create_module" {
  type    = bool
  default = false
}

variable "start_load_controller" {
  type    = bool
  default = false
}