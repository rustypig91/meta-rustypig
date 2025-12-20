SUMMARY = "Kiosk browser based on Qt6"
DESCRIPTION = "A simple kiosk web browser using Qt6 WebEngine."
HOMEPAGE = "https://github.com/44670/FBrowser"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "git://github.com/rustypig91/kiosk-browser.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

SRC_URI += " \
    file://kiosk-browser.service \
"

inherit cmake_qt5 systemd

EXTRA_OECMAKE = "USE_QT5=ON"

DEPENDS += "qtwebkit qttools-native xkeyboard-config"
RDEPENDS:${PN} += "qtwebkit"

KIOSK_BROWSER_URL ?= "http://example.com"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/kiosk-browser.service ${D}${systemd_system_unitdir}/

    sed -i 's|KIOSK_BROWSER_URL|'"${KIOSK_BROWSER_URL}"'|g' ${D}${systemd_system_unitdir}/kiosk-browser.service
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/kiosk-browser.service \
"
