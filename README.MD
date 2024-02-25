This repository is part of a bachelor thesis done at **Hochschule Esslingen** in winter semester 2023/2024

## Abstract


Energy efficiency is an important factor for the sustainability of IT systems. In this thesis, the energy efficiency of a Camunda 8 component, the Job Worker, is analysed. Camunda enables the execution of BPMN processes.  Job workers are used for the automated processing of individual tasks within these processes, so-called service tasks. They are implemented as independent applications. In order to minimise resource consumption, it is necessary to measure the influence of different configurations on energy consumption and to analyse the workers' life cycle.

To this end, a test environment is being developed that enables the workers to be controlled and measured. In addition to Camunda 8, Prometheus and KEPLER are used in a Kubernetes cluster to record the energy consumption of the worker and other key metrics.

After designing, conducting and analysing four experiments, best practices for increasing energy efficiency are formulated. The orientation towards the Green Software Measurement Process and the automation of the test sequences offer good transferability of the experiments for repetition on other systems.

## Contents

This repository features three different parts relevant to the thesis:

1) A java/spring implementation of a camunda 8 worker. It can be found under `worker-code/`
2) A Camunda 8 cluster with said job worker, prometheus, grafana and kepler to provide a testbed for energy measurements. It uses **helm and terraform** and can be set up running `terraform init` and `terraform apply` from this repo's root directory
3) shell scripts for 4 different test setups can be found under `worker-measurements/shell-scripts`. These are accompanied by jupyter notebooks under `worker-measurements/` providing analysis of the resulting CSV files exported though Grafana. The shell scripts also create log files to be used with the analysis.

## Architecture

![[documentation/ComponentDiagram_v3.drawio.png]]

The worker implementation establishes a simple REST-Communication, inspired by the camunda-8-benchmark-project. 

## To do

- shell-scripts: add proper documentation for shell scripts running test-setups
- jupyter notebooks: remove various in-between steps not needed for analysis
- jupyter notebooks: translate german markdown


