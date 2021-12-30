SUMMARY = "motionEye is a web-based frontend for motion."
HOMEPAGE = "https://pypi.org/project/motioneye/"
LICENSE = "LGPLv3"

LIC_FILES_CHKSUM = "file://LICENSE;md5=4fe869ee987a340198fb0d54c55c47f1"

S = "${WORKDIR}/git"

SRC_URI = " \
    git://github.com/Mictronics/motioneye.git;branch=python3;protocol=https \
    file://0001-remove-init-functions-dependency.patch \
    file://0003-various-fixes.patch \
    file://motioneye.conf \
    "
# file://0002-replace-asynchronous-with-coroutine.patch
# file://0004-fix-popen.patch

SRCREV = "ddc2435cb4266798d31e2d370a48db309672a966"

inherit setuptools3

DEPENDS += ""

RDEPENDS:${PN} += " \
    libffi \
    ffmpeg \
    v4l-utils \
    libjpeg-turbo \
    bash \
    motion \
    curl \
    lsb-release \
    python3-pillow \
    python3-tornado \
    python3-jinja2 \
    python3-pycurl \
    python3-multiprocessing \
    python3-six \
    python3-dateutil \
    python3-fcntl \
    python3-setuptools \
    "
# mariadb
FILES:{PN} += "/usr/share/motioneye/*"

inherit ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'update-rc.d', d)}

SYSTEMD_SERVICE:${PN} = "motioneye.service"
INITSCRIPT_NAME = "motioneye"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -Dm 755 ${D}/usr/share/motioneye/extra/motioneye.systemd-unit-local ${D}${systemd_system_unitdir}/motioneye.service
    else
        install -Dm 755 ${D}/usr/share/motioneye/extra/motioneye.init-debian ${D}${INIT_D_DIR}/motioneye
    fi

    install -Dm 644 ${WORKDIR}/motioneye.conf ${D}/etc/motioneye/motioneye.conf

    install -d ${D}/var/lib/motioneye
}
