SUMMARY = "Display splash image on boot using fbida"
DESCRIPTION = "Shows a splash image during boot using fbida's fbi tool."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://splash.png"

RDEPENDS:${PN} = "fbida"

S = "${UNPACKDIR}"

SRC_URI = " \
    file://fbsplash.service \
    file://splash.jpeg \
    file://fb.rules \
"

FBDEV ?= "fb0"
LOGO ?= "splash.jpeg"

SYSTEMD_SERVICE:${PN} = "fbsplash.service"

do_install() {
    install -d ${D}${datadir}/fbsplash
    install -m 0644 ${S}/${LOGO} ${D}${datadir}/fbsplash/${LOGO}

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/fbsplash.service ${D}${systemd_system_unitdir}/fbsplash.service

    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/fb.rules ${D}${sysconfdir}/udev/rules.d/

    sed -i -e "s|FBDEV|${FBDEV}|g" ${D}${systemd_system_unitdir}/fbsplash.service
    sed -i -e "s|LOGO|${LOGO}|g" ${D}${systemd_system_unitdir}/fbsplash.service
    sed -i -e "s|FBDEV|${FBDEV}|g" ${D}${sysconfdir}/udev/rules.d/fb.rules
}

FILES:${PN} += "${datadir}/fbsplash/${LOGO} ${systemd_system_unitdir}/fbsplash.service"

PROVIDES = "virtual/psplash"

inherit systemd
