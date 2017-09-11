#!/bin/bash
# this is oe-alliance's meta-brands/meta-azbox/recipes-bsp/azbox-minime-packer/pack_minime_image.c
# reimplemented in shell (the original c file has no free license)
#
#  Copyright © 2017 Stefan Seyfried
# This program is free software. It comes without any warranty, to
# the extent permitted by applicable law. You can redistribute it
# and/or modify it under the terms of the Do What The Fuck You Want
# To Public License, Version 2, as published by Sam Hocevar. See
# http://www.wtfpl.net/ for more details. */
#  as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.

function write_size {
    local A=$1
    local A0=$(((A>>0)&0xFF))
    local A1=$(((A>>8)&0xFF))
    local A2=$(((A>>16)&0xFF))
    local A3=$(((A>>24)&0xFF))
    local AX=$(printf "\\\\x%02x\\\\x%02x\\\\x%02x\\\\x%02x" $A0 $A1 $A2 $A3)
    echo -en $AX
}
function pack_minime_image {
    # kernel_zbimage image_jffs2
    local kernel="$1"
    local image="$2"
    local kernel_size=$(stat -L -c %s "$kernel")
    local image_size=$(stat -L -c %s "$image")
    __write_size $kernel_size
    __write_size $image_size
    cat "$kernel" "$image"
}

pack_minime_image "$1" "$2"
