SUMMARY = "update.ext for USB and WEBIF update packages"
LICENSE = "Proprietary"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"
### there are two downloads under /azbox/me and /azbox/minime, but they are the same file
SRC_URI = " \
    http://source.mynonpublic.com/azbox/me/update.ext \
    file://pack_minime_image.sh \
"

SRC_URI[sha256sum] = "8c19c230e415afc25423e034b9c4e6dfdcb5e0488f667bcf98cfb4bfdb74113b"

PACKAGES = ""
PROVIDES = ""
RPROVIDES = ""

do_install() {
    install -m 0644 ${WORKDIR}/update.ext ${DEPLOY_DIR_IMAGE}/
    # install -m 0755 ${WORKDIR}/pack_minime_image.sh ${DEPLOY_DIR_IMAGE}/
}
