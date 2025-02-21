

ministart:
	minikube start --driver=docker --container-runtime=containerd --cpus=4 --memory=8192 --disk-size=20g --addons ingress
	make build-api-dev build-keycloak build-redisinsight
	minikube image load quay.io/keycloak/keycloak:26.0.0-arm64

    minikube image load redis/redisinsight:2.66-arm64
    minikube image load lsadf/lsadf-api-dev-arm64:latest


ministop:
	minikube stop

minidelete:
	minikube delete --all --purge