KV = "3.9.2"
SRC = "2015"
SRCREV = "r3"
SRCDATE = "16092013"
SRCDATE_azboxme = "14092013"
# SRCDATE_azboxminime = "14092013"

AZBOX_SRC = "http://source.mynonpublic.com"

SRC_URI += "${KERNELORG_MIRROR}/linux/kernel/v3.x/linux-${KV}.tar.bz2;name=azbox-kernel \
    file://defconfig \
    file://genzbf.c \
    file://sigblock.h \
    file://zboot.h \
    file://emhwlib_registers_tango2.h \
    file://kernel-3.9.2.patch \
    file://add-dmx-source-timecode.patch \
    file://af9015-output-full-range-SNR.patch \
    file://af9033-output-full-range-SNR.patch \
    file://as102-adjust-signal-strength-report.patch \
    file://as102-scale-MER-to-full-range.patch \
    file://cinergy_s2_usb_r2.patch \
    file://cxd2820r-output-full-range-SNR.patch \
    file://dvb-usb-dib0700-disable-sleep.patch \
    file://dvb_usb_disable_rc_polling.patch \
    file://it913x-switch-off-PID-filter-by-default.patch \
    file://tda18271-advertise-supported-delsys.patch \
    file://fix-dvb-siano-sms-order.patch \
    file://mxl5007t-add-no_probe-and-no_reset-parameters.patch \
    file://nfs-max-rwsize-8k.patch \
    file://0001-rt2800usb-add-support-for-rt55xx.patch \
    file://0001-Revert-MIPS-Fix-potencial-corruption.patch \
    file://kernel-add-support-for-gcc-5.patch \
    file://rtl8712-fix-warnings.patch \
    file://rtl8187se-fix-warnings.patch \
    file://0001-MIPS-Fix-build-with-binutils-2.24.51.patch \
    file://kernel-azbox-miscbuildfixes.patch \
    file://kernel-azbox-fix-sata_tangox-platformdevice.patch \
    "

SRC_URI_append_azboxhd = "${AZBOX_SRC}/initramfs-${MACHINE}-oe-core-${KV}-${SRCDATE}.tar.bz2;name=initrd-hd"
SRC_URI_append_azboxme = " \
    ${AZBOX_SRC}/azbox/initramfs-azboxme-oe-core-${KV}-${SRCDATE}.tar.bz2;name=initrd-me;subdir=initramfs-me \
    ${AZBOX_SRC}/azbox/initramfs-azboxminime-oe-core-${KV}-${SRCDATE}.tar.bz2;name=initrd-minime;subdir=initramfs-minime \
    file://initramfs-azboxmeminime-init \
"
# SRC_URI_append_azboxminime = "${AZBOX_SRC}/azbox/initramfs-${MACHINE}-oe-core-${KV}-${SRCDATE}.tar.bz2;name=azbox-initrd-${MACHINE}"

SRC_URI[azbox-kernel.md5sum] = "661100fdf8a633f53991684b555373ba"
SRC_URI[azbox-kernel.sha256sum] = "dfcaa8bf10f87ad04fc46994c3b4646eae914a9eb89e76317fdbbd29f54f1076"
SRC_URI[initrd-hd.md5sum] = "7effc9bc7eb0ed2e9232dedf6e0c74cc"
SRC_URI[initrd-hd.sha256sum] = "ff7c86cfc89ffedeaea15cd15ec9839ee97d810ac847527bccc7e1f2ab7ee833"
SRC_URI[initrd-me.md5sum] = "59f73c9b9fe95183bd39e2a4010a2ac7"
SRC_URI[initrd-me.sha256sum] = "b98be68bf2d607e57e1cbc48a4eb78c5759d24e0cf0c22e127263788e83665fd"
SRC_URI[initrd-minime.md5sum] = "3b7508985058ac0a5d9d310f669cc5bc"
SRC_URI[initrd-minime.sha256sum] = "b7979e03bd53f6c975079761c3399d5dd80e9db5addeae27726f09f87a86be72"

include linux-azbox.inc

do_rm_work() {
}
