output "cluster_endpoint" {
  description = "Endpoint for EKS control plane"
  value       =  null
}

output "cluster_security_group_id" {
  description = "Security group ids attached to the cluster control plane"
  value       =  null
}

output "region" {
  description = "AWS region"
  value       = null
}

output "cluster_name" {
  description = "Kubernetes Cluster Name"
  value       = "kind-kind"
}
