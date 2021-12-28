#!/bin/bash
# -----------------------------------------------------------------------------
# This script sets up and runs a Docker container for Yocto development.
#
# It performs the following steps:
#   1. Determines the workspace directory relative to the script location.
#   2. Extracts DL_DIR and SSTATE_DIR paths from the Yocto local.conf file.
#   3. Prepares Docker volume mount points for the workspace, DL_DIR, and SSTATE_DIR.
#   4. Allows for additional Docker mount points via the DOCKER_EXTRA_MOUNTS variable.
#   5. Sets the Docker image to 'crops/poky:ubuntu-22.04' if not specified.
#   6. Runs the container interactively, mounting the necessary directories.
#   7. Initializes the Yocto build environment and executes the provided command,
#      or starts a bash shell if no command is given.
#
# Usage:
#   ./docker.sh [command]
#
# Environment Variables:
#   DOCKER_IMAGE         - (Optional) Docker image to use.
#   DOCKER_EXTRA_MOUNTS  - (Optional) Additional Docker volume mounts.
#
# Example:
#   ./docker.sh bitbake core-image-minimal
# -----------------------------------------------------------------------------

workspace=$(realpath "$(dirname "$0")/..")

DL_DIR=$(grep '^DL_DIR' "$workspace/build/conf/local.conf" | cut -d'=' -f2 | tr -d ' "')
SSTATE_DIR=$(grep '^SSTATE_DIR' "$workspace/build/conf/local.conf" | cut -d'=' -f2 | tr -d ' "')

MOUNT_POINTS="-v $workspace:$workspace"

if [ ! -z "$DL_DIR" ]; then
    echo "Using DL_DIR: $DL_DIR"
    MOUNT_POINTS="$MOUNT_POINTS -v $DL_DIR:$DL_DIR"
fi

if [ ! -z "$SSTATE_DIR" ]; then
    echo "Using SSTATE_DIR: $SSTATE_DIR"
    MOUNT_POINTS="$MOUNT_POINTS -v $SSTATE_DIR:$SSTATE_DIR"
fi

if [ $# -eq 0 ]; then
    CMD="exec bash"
else     
    CMD="$*"
fi

if [ -z "$DOCKER_IMAGE" ]; then
    DOCKER_IMAGE="crops/poky:ubuntu-22.04"
fi

docker run --rm -it \
    $MOUNT_POINTS \
    $DOCKER_EXTRA_MOUNTS \
    $DOCKER_IMAGE \
    bash -c "cd $workspace && source openembedded-core/oe-init-build-env && $CMD"
