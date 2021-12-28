DESCRIPTION = "MotionPlus is a break at version 4.2.2 from the Motion application."
LICENSE = "GPLv3"

LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

S = "${WORKDIR}/git"

SRC_URI = "git://github.com/Motion-Project/motionplus;branch=master;protocol=https file://0001-install-permissions.patch"

SRCREV = "f9c3862f60be86923522500feb792de1309c80f3"

inherit autotools

PV = "0.0.0+git${SRCPV}"

DEPENDS += "libjpeg-turbo libmicrohttpd ffmpeg gettext-native"

do_configure:prepend() {
    export TEMP_LDFLAGS="${LDFLAGS}"
}
