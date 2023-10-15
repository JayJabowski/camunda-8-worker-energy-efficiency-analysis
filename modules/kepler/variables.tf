variable "namespace" {
  type    = string
  default = "kepler"
}

variable "create_module" {
  type    = bool
  default = false
}

variable "use_emulation" {
  type = bool
  default = false
}