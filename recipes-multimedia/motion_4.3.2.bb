DESCRIPTION = " \
Motion is a program that monitors the video signal from one or more \
cameras and is able to detect if a significant part of the picture has \
changed. Or in other words, it can detect motion."

LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://doc/COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

S = "${WORKDIR}/git"

SRC_URI = "git://github.com/Motion-Project/motion;branch=4.3;protocol=https"
SRCREV = "release-${PV}"

inherit autotools

DEPENDS += "libjpeg-turbo libmicrohttpd ffmpeg gettext-native"

do_install:append() {
    mv ${D}/etc/motion/motion-dist.conf ${D}/etc/motion/motion.conf
}