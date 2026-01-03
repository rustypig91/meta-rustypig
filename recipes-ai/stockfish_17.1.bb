SUMMARY = "Stockfish chess engine"
HOMEPAGE = "https://stockfishchess.org"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://Copying.txt;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "git://github.com/official-stockfish/Stockfish.git;protocol=https;branch=master"
SRCREV = "sf_${PV}"

# Choose best Stockfish ARCH for target
STOCKFISH_ARCH:native = "native"
STOCKFISH_ARCH:aarch64 = "armv8"
STOCKFISH_ARCH:arm = "${@'armv7-neon' if bb.utils.contains('TUNE_FEATURES', 'neon', True, False, d) else 'armv7'}"
STOCKFISH_ARCH:x86_64 = "x86-64"

# Fail fast if no Stockfish ARCH was found for the target
python __anonymous() {
    arch = d.getVar('STOCKFISH_ARCH')
    if not arch:
        bb.fatal('stockfish: No valid architecture supported by stockfish was found for TARGET_ARCH=%s (TUNE_FEATURES=%s). Please add a suitable mapping.' % (d.getVar('TARGET_ARCH'), d.getVar('TUNE_FEATURES')))
}

# Ensure cross tools are used
EXTRA_OEMAKE = "\
    CC='${CC}' \
    COMPCXX='${CXX}' \
    AR='${AR}' \
    RANLIB='${RANLIB}' \
    STRIP='${STRIP}' \
    ARCH='${STOCKFISH_ARCH}' \
    COMP=gcc \
    debug=no \
    EXTRALDFLAGS=-lgcov \
"

do_compile() {
    oe_runmake -C src -j build
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/src/stockfish ${D}${bindir}/
}
