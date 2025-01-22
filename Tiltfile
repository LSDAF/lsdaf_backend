# Ensure version is valid
version_settings(constraint=">=0.33.22")
print("Run Docker Compose with tilt")

docker_compose("./dc-local.yml", env_file='env/env.properties', project_name='lsadf_backend', profiles=['db', 'monitoring'])

print('Docker Compose running')