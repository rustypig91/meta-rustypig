# Ensure header files are not part of the normal runtime package
# They will be packaged under the -dev package only.

SUMMARY = "Lc0 is a UCI-compliant chess engine designed to play chess via neural network, specifically those of the LeelaChessZero project."
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e49f4652534af377a713df3d9dec60cb"

SRC_URI = "git://github.com/LeelaChessZero/lc0.git;branch=release/0.32;protocol=https;recurse-submodules=1"
SRCREV = "v0.32.1"

SRC_URI += "https://github.com/abseil/abseil-cpp/releases/download/20240722.0/abseil-cpp-20240722.0.tar.gz;name=abseil;subdir=${S}/subprojects/"
SRC_URI += "https://github.com/mesonbuild/wrapdb/releases/download/eigen_3.4.0-2/eigen-3.4.0.tar.bz2;name=eigen;subdir=${S}/subprojects/"
SRC_URI += "https://github.com/mesonbuild/wrapdb/releases/download/gtest_1.15.2-4/gtest-1.15.2.tar.gz;name=googletest;subdir=${S}/subprojects/"
SRC_URI += "http://zlib.net/fossils/zlib-1.2.11.tar.gz;name=zlib;subdir=${S}/subprojects/"

SRC_URI += "file://abseil-cpp-meson.build"
SRC_URI += "file://eigen-meson.build"
SRC_URI += "file://googletest-meson.build"

SRC_URI[abseil.sha256sum] = "f50e5ac311a81382da7fa75b97310e4b9006474f9560ac46f54a9967f07d4ae3"
SRC_URI[eigen.sha256sum] = "b4c198460eba6f28d34894e3a5710998818515104d6e74e5cc331ce31e46e626"
SRC_URI[googletest.sha256sum] = "7b42b4d6ed48810c5362c265a17faebe90dc2373c885e5216439d37927f02926"
SRC_URI[zlib.sha256sum] = "c3e5e9fdd5004dcb542feda5ee4f0ff0744628baf8ed2dd5d66f8ca1197cb1a1"

do_configure:prepend() {
    cp ${UNPACKDIR}/abseil-cpp-meson.build ${S}/subprojects/abseil-cpp-20240722.0/meson.build
    cp ${UNPACKDIR}/eigen-meson.build ${S}/subprojects/eigen-3.4.0/meson.build
    cp ${UNPACKDIR}/googletest-meson.build ${S}/subprojects/googletest-1.15.2/meson.build
}

FILES:${PN}:remove = "${includedir} ${libdir}/pkgconfig ${datadir}/pkgconfig"
FILES:${PN}-dev += "${includedir} ${libdir}/pkgconfig ${datadir}/pkgconfig"

DEPENDS += "zlib zlib-native abseil-cpp googletest"

inherit meson pkgconfig
