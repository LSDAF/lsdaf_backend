# Ensure version is valid
version_settings(constraint=">=0.33.22")

profiles = os.getenv("TILT_PROFILES", "").split()

if sys.argv[1] == 'up':
    if (profiles == []):
        profiles = ["db", "monitoring", "backend_dev", "backend_admin_dev"]
    print(profiles)
    print('Docker Compose running')

elif sys.argv[1] == 'down':
    profiles = ["db", "monitoring", "backend_dev", "backend", "backend_admin_dev"]
    print("Killing Docker Compose")

docker_compose("./dc-local.yml", env_file='env/env.properties', project_name='lsadf_backend', profiles=profiles)
docker_build('lsadf/lsadf-api-dev',
    '.',
    dockerfile='docker/Dockerfile-dev',
    build_args={'module_folder':'lsadf_api'},
    ignore=['env', 'keycloak_realm', '.make', '.idea', '.github', 'docker/data'])

docker_build('lsadf/lsadf-admin-dev',
    '.',
    dockerfile='docker/Dockerfile-dev',
    build_args={'module_folder':'lsadf_admin'},
    ignore=['env', 'keycloak_realm', '.make', '.idea', '.github', 'docker/data'])

update_settings(suppress_unused_image_warnings=["lsadf/lsadf-api-dev", "lsadf/lsadf-admin-dev"])