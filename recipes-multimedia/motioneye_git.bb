SUMMARY = "motionEye is a web-based frontend for motion."
HOMEPAGE = "https://pypi.org/project/motioneye/"
LICENSE = "LGPLv3"

LIC_FILES_CHKSUM = "file://LICENSE;md5=4fe869ee987a340198fb0d54c55c47f1"

S = "${WORKDIR}/git"

SRC_URI = " \
    git://github.com/ccrisan/motioneye.git;branch=master;protocol=https \
    file://0001-remove-init-functions-dependency.patch \
    "
# file://0002-replace-asynchronous-with-coroutine.patch
# file://0004-fix-popen.patch

SRCREV = "0.42.1"

inherit setuptools

DEPENDS += ""

RDEPENDS:${PN} += " \
    libffi \
    ffmpeg \
    v4l-utils \
    libjpeg-turbo \
    bash \
    motion \
    lsb-release \
    python-pillow \
    python-tornado \
    python-jinja2 \
    python-pycurl \
    python-multiprocessing \
    python-six \
    python-dateutil \
    python-fcntl \
    python-pkg-resources \
    python-plistlib \
    python-argparse \
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

    install -Dm 644 ${D}/usr/share/motioneye/extra/motioneye.conf.sample ${D}/etc/motioneye/motioneye.conf

    install -d ${D}/var/lib/motioneye
}
