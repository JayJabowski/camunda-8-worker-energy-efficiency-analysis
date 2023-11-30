# Demo BPMN App 
This BPMN Demo App is used to evaluate measurement and optimizations regarding sustainability within BPMN and Camunda running on a Kubernetes cluster.

## Getting started
- Make sure you have AWS CLI, Terraform and Zeebe CLI installed
- Clone Repo
- Make sure your cluster is configured under ``~/.kube/config``. If needed, you can change the path in ``variables.tf``
  - (Optional) In ``main.tf`` you can create your own Kubernetes clusters (eks or kind) which will be set as your main config when provisioning finished. To enable set ``create_module`` and ``set_kubecfg`` to ``true`` in the modules kind and eks accordingly.
- Use ``terraform init`` and ``terraform apply`` in the root of the project
  
- (Camunda 8) When everything is finished you can access 
  - Camunda Operate via: ``kubectl port-forward -n camunda8 svc/camunda-platform-operate  8080:80`` and afterwards via ``localhost:8080`` and creds ``demo:demo``
  - Camunda Tasklist via: ``kubectl port-forward -n camunda8 svc/camunda-platform-tasklist  8081:80`` and afterwards via ``localhost:8081`` and creds ``demo:demo``
  - Camunda Zeebee Gateway via: ``kubectl port-forward -n camunda8 svc/camunda-platform-zeebe-gateway 26500:26500`` and afterwards via ``localhost:26500`` and creds ``demo:demo``
    - The Sample_Process and its worker should already be deployed, therefore you can kick off an instance via ``zbctl create instance Sample_Process --insecure``
    - Alternatively, look at the job resource in camunda8.tf
  - Grafana via: ``kubectl port-forward -n monitoring svc/kube-prometheus-stackr-grafana 9102:80`` and afterwards via ``localhost:9102`` and creds ``admin:prom-operator``
- (Camunda 7) When everything is finished you can access
  - TBD  


## TODO
- Issue with kind deployment: When deploying the kind cluster the first time the kubernetes provider doesnt seem to recognize the context change and therefore there is an error, but afterwards it works. Therefore: execute apply twice to use the kind cluster
- Deploy Camunda 7 properly and in an automated way
- Kepler emulation:  not sure if works as intended or some configuration is wrong, would be great if we could get it running on k8s running on windows
