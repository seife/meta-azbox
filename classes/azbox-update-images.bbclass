inherit image_types

IMAGE_TYPEDEP_azboxme-update-img = "jffs2.sum"

IMAGE_DEPENDS_azboxme-update-img = " \
	virtual/kernel \
	azbox-update-ext \
"

IMAGEDATESTAMP = "${@time.strftime('%Y.%m.%d',time.gmtime())}"
# https://www.mail-archive.com/yocto@yoctoproject.org/msg29667.html
IMAGE_CMD_azboxme-update-img[vardepsexclude] = "DATETIME"
IMAGE_CMD_azboxme-update-img[vardepsexclude] += "IMAGEDATESTAMP"

IMAGE_CMD_azboxme-update-img () {

    USB=${WORKDIR}/USB
    WEB=${WORKDIR}/WEBIF
    rm -fr ${USB} ${WEB}
    mkdir ${USB} ${WEB}

    cd ${DEPLOY_DIR_IMAGE}

    test -d USB   || mkdir USB
    test -d WEBIF || mkdir WEBIF

    cp -L zbimage-linux-xload ${WEB}
    cp -L zbimage-linux-xload ${USB}

    cp -L ${IMAGE_LINK_NAME}.jffs2.sum ${WEB}/flash.jffs2
    cp -L ${IMAGE_LINK_NAME}.jffs2.sum ${USB}/image0.jffs2

    # azbox-update-ext puts update.ext and pack_minime_image.sh into ${DEPLOY_DIR_IMAGE}
    cp update.ext ${USB}

    rm -f ${WORKDIR}/webif-update.tar
    tar -C ${WEB}/ -cf ${WORKDIR}/webif-update.tar .
    zip -j WEBIF/${IMAGE_NAME}_webif-me.zip ${WORKDIR}/webif-update.tar
    # minime has special "image" format for webif
    # note that this does not seem to work from a minime webinterface, so disabled for now
    ## ./pack_minime_image.sh ${WEB}/zbimage-linux-xload ${WEB}/flash.jffs2 > WEBIF/${IMAGE_NAME}_webif-update-minime.img

    # USB image is also not yet tested... ;-)
    zip -j USB/${IMAGE_NAME}_usb.zip ${USB}/*
}
