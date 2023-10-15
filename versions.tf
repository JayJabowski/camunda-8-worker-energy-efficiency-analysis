terraform {
  required_version = ">= 1.0.0"

  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "2.10.0"
    }

    helm = {
      source  = "hashicorp/helm"
      version = "2.4.1"
    }

    kind = {
      source = "justenwalker/kind"
      version = "0.17.0"
    }
  }
}


