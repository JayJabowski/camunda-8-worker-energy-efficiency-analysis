variable "namespace" {
  type    = string
  default = "load-controller"
}

variable "create_module" {
  type    = bool
  default = false
}

variable "start_load_controller" {
  type    = bool
  default = false
}