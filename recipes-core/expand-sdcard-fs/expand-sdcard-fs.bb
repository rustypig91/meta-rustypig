SUMMARY = "Expand a filesystem to fill SD card on first boot"
DESCRIPTION = "A simple package that expands the root filesystem to fill the entire SD card on first boot."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} = "parted e2fsprogs-resize2fs"

# Mount point to expand
MOUNT_POINT ?= "/"

ALLOW_EMPTY:${PN} = "1"

pkg_postinst_ontarget:${PN}() {
    # Automatically resize the root partition to 100% of the SD card
    root_dev=$(mount | grep "on ${MOUNT_POINT} " | cut -d' ' -f1 | sed 's/p\?[0-9]*$//')
    root_part_num=$(mount | grep "on ${MOUNT_POINT} " | cut -d' ' -f1 | sed 's/.*[^0-9]//')

    # Resize the partition
    parted -s ${root_dev} resizepart ${root_part_num} 100%
    # Resize the filesystem
    resize2fs ${root_dev}p${root_part_num}
}
