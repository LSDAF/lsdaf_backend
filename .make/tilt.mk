tiltup: install-docker
	tilt up

tiltdown:
	tilt down

tiltmonitorup: install-docker
	TILT_PROFILES="db monitoring" tilt up