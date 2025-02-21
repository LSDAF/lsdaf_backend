
helmi:
	helm install test my-chart/ -f my-chart/values-secret.yaml

helmu:
	helm uninstall test

helmup:
	helm upgrade test my-chart/ -f my-chart/values-secret.yaml