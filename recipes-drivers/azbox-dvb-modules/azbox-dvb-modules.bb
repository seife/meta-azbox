SUMMARY = "Hardware drivers for AZbox ME/miniME"
SECTION = "base"
PRIORITY = "required"
LICENSE = "Proprietary"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

KV = "3.9.2-opensat"

# kernel modules are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRCDATE = "20130917"
SRCGET = "17092013"

PV = "${KV}+${SRCDATE}"
PR = "r0"

AZBOX_SRC = "http://source.mynonpublic.com/azbox"

SRC_URI = " \
    ${AZBOX_SRC}/azboxme-dvb-modules-${KV}-oe-core-${SRCGET}.tar.gz;name=dvb-me;subdir=me \
    ${AZBOX_SRC}/azboxminime-dvb-modules-${KV}-oe-core-${SRCGET}.tar.gz;name=dvb-minime;subdir=minime \
    file://azbox-dvb-modules \
"

SRC_URI[dvb-me.md5sum] = "3d7b8d240626a08f16c170e5832be618"
SRC_URI[dvb-me.sha256sum] = "48b48a94094ecce34398efcee7e17e780d9cce0ecf1510758078ed4e18f9ce6d"
SRC_URI[dvb-minime.md5sum] = "2c037462af10a7909f4c803a90b82a1d"
SRC_URI[dvb-minime.sha256sum] = "7ac3c8ac567ffe627850fda0c6713dce71305886af6e3cc286800fbfc394709a"

S = "${WORKDIR}"

INHIBIT_PACKAGE_STRIP = "1"

# inherit module

DEPENDS_append = " update-rc.d-native "

do_compile() {
}

do_install() {
    CP="cp --preserve=mode,timestamps"
    install -d ${D}/lib/modules/${KV}/extra
    for j in me minime; do
        for i in llad em8xxx 865xi2c avl6211 avl2108 mxl241sf nimdetect 865xdvb; do
            $CP ${WORKDIR}/$j/$i.ko ${D}/lib/modules/${KV}/extra
        done
        $CP ${WORKDIR}/$j/sci.ko ${D}/lib/modules/${KV}/extra/sci$i.ko
    done
    install -d ${D}/lib/firmware
    $CP ${WORKDIR}/me/*.fw ${D}/lib/firmware/
    install -d ${D}/etc/init.d
    $CP ${WORKDIR}/azbox-dvb-modules ${D}/etc/init.d/
    chmod 0755 ${D}/etc/init.d/*
    update-rc.d -r ${D} azbox-dvb-modules start 30 S .
}

FILES_${PN} += " \
	/etc/init.d \
	/etc/rcS.d \
	/lib/firmware \
	/lib/modules \
"
