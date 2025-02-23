

up: secret ministart
	helm upgrade --install local lsadf-k8s/ -f lsadf-k8s/values.yml -f lsadf-k8s/values-local.yml -f lsadf-k8s/values-secret.yml

down:
	helm uninstall local


miniload:
		minikube image load quay.io/keycloak/keycloak:26.0.0-arm64
        minikube image load redis/redisinsight:2.66-arm64
        minikube image load lsadf/lsadf-api-dev-arm64:latest

ministart:
	minikube start --driver=docker --container-runtime=containerd --cpus=4 --memory=8192 --disk-size=20g --addons ingress
	make build-api-dev build-keycloak build-redisinsight
	make miniload


ministop:
	minikube stop

minidelete: ministop
	minikube delete --all --purge


monitorup: ministart
	helm upgrade --install local lsadf-k8s/ -f lsadf-k8s/values.yml -f lsadf-k8s/values-monitor.yml -f lsadf-k8s/values-secret.yml