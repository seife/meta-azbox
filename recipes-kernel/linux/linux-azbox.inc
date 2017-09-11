SUMMARY = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-${KV}/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

KERNEL_IMAGE_MAXSIZE_azboxhd = "6815744"
#KERNEL_IMAGE_MAXSIZE_azboxme = "6815744"
#KERNEL_IMAGE_MAXSIZE_azboxminime = "6815744"

DEPENDS = "genromfs-native gcc"
DEPENDS_azboxhd = "genromfs-native azbox-hd-buildimage gcc"
DEPENDS_azboxminime = "genromfs-native azbox-minime-packer gcc"

inherit kernel

PKG_kernel-base = "kernel-base"
PKG_kernel-image = "kernel-image"
RPROVIDES_kernel-base = "kernel-${KERNEL_VERSION}"
RPROVIDES_kernel-image = "kernel-image-${KERNEL_VERSION}"
FILES_kernel-image = "${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KV}-opensat"
ALLOW_EMPTY_kernel-dev = "1"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_IMAGETYPE = "zbimage-linux-xload"
KERNEL_IMAGEDEST = "/tmp"

S = "${WORKDIR}/linux-${KV}"
B = "${WORKDIR}/build"

CFLAGS_prepend = "-I${WORKDIR} "
EXTRA_OEMAKE = "CONFIG_INITRAMFS_SOURCE=${STAGING_KERNEL_DIR}/initramfs"

do_configure_prepend() {
    if false; then
        sed -i "s#usr/initramfs_default_node_list#\$(srctree)/usr/initramfs_default_node_list#" ${STAGING_KERNEL_DIR}/usr/Makefile
        sed -i "s#\$(srctree)/arch/mips/boot/#\$(obj)/#" ${STAGING_KERNEL_DIR}/arch/mips/boot/Makefile
        sed -i "s/ -static//" ${STAGING_KERNEL_DIR}/scripts/Makefile.host
    fi
    # make sure an updated defconfig is really used
    cp "${WORKDIR}/defconfig" "${B}/.config"
}

kernel_do_compile_prepend() {
    gcc ${CFLAGS} ${WORKDIR}/genzbf.c -o ${WORKDIR}/genzbf
    install -d ${B}/arch/${ARCH}/boot/
    install -m 0755 ${WORKDIR}/genzbf ${B}/arch/${ARCH}/boot/
}

kernel_do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
    oe_runmake ${KERNEL_IMAGETYPE} CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${AR}" OBJDUMP="${OBJDUMP}" NM="${NM}" CONFIG_INITRAMFS_SOURCE="${STAGING_KERNEL_DIR}/initramfs"
    oe_runmake modules CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${AR}" OBJDUMP="${OBJDUMP}" CONFIG_INITRAMFS_SOURCE="${STAGING_KERNEL_DIR}/initramfs"
}

kernel_do_compile_append() {
    rm -rf ${B}/arch/${ARCH}/boot/genzbf
    # rm -rf ${B}/arch/${ARCH}/boot/${KERNEL_IMAGETYPE}
    # install -m 0644 ${WORKDIR}/zbimage-linux-xload ${B}/arch/${ARCH}/boot/${KERNEL_IMAGETYPE}
}

# This is part of kernel.bbclass but doesn't get executed when not copied here
do_sizecheck() {
        if [ ! -z "${KERNEL_IMAGE_MAXSIZE}" ]; then
                cd ${B}
                size=`ls -lL ${KERNEL_OUTPUT} | awk '{ print $5}'`
                if [ $size -ge ${KERNEL_IMAGE_MAXSIZE} ]; then
                        die "This kernel (size=$size > ${KERNEL_IMAGE_MAXSIZE}) is too big for your device. Please reduce the size of the kernel by making more of it modular."
                fi
        fi
}
do_sizecheck[dirs] = "${B}"

addtask sizecheck before do_install after do_strip

do_combine_initramfs() {
	# combine initramfs from me and minime into a common one
	set -x
	rm -fr ${S}/initramfs
	cp -a ${WORKDIR}/initramfs-me/linux-${KV}/initramfs ${S}/
	OLD=$PWD
	DEST=${S}/initramfs
	MODDEST=$DEST/lib/modules/${KV}-opensat/kernel/drivers
	BINDEST=$DEST/usr/bin
	cd ${WORKDIR}/initramfs-minime/linux-${KV}/initramfs/lib/modules/${KV}-opensat/kernel/drivers
	cp -a nand_wr.ko $MODDEST/nand_wrminime.ko
	cp -a irvfdminime.ko $MODDEST/
	cp -a xload-38x/audio_*_dts52.xload $MODDEST/xload-38x
	cd ${WORKDIR}/initramfs-minime/linux-${KV}/initramfs/usr/bin
	cp -a progmicom_minime* $BINDEST
	cp -a webinterface $BINDEST/webinterfaceminime
	cd $MODDEST
	mv nand_wr.ko nand_wrme.ko
	ln -s nand_wrme.ko nand_wr.ko
	# this init can handle both machines...
	cp -a ${WORKDIR}/initramfs-azboxmeminime-init $DEST/init
	chmod 0755 $DEST/init
	sed -i 's/^root:.*/root::10933:0:99999:7:::/' $DEST/etc/shadow # empty root password for rescue
	cd $OLD
	set +x
}

addtask do_combine_initramfs before do_configure after do_unpack
