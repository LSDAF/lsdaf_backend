

up: secret ministart
	helm upgrade --install local my-chart/ -f my-chart/values.yml -f my-chart/values-local.yml -f my-chart/values-secret.yml

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
	helm upgrade --install local my-chart/ -f my-chart/values.yml -f my-chart/values-monitor.yml -f my-chart/values-secret.yml