Yocto BSP Layer - For AZbox ME/miniME
=====================================

This is the general hardware specific BSP overlay for azbox me/miniME devices.
It should be used with openembedded-core

This layer in its entirety depends on:

    URI: http://git.yoctoproject.org/git/poky
    branch: krogoth
    revision: HEAD

How to use it with yocto
------------------------

## Clone poky
    git clone http://git.yoctoproject.org/git/poky poky

## Switch to krogoth branch
    cd poky
    git checkout -b krogoth origin/krogoth

Independent Steps from poky/oe-core
-----------------------------------

## Clone meta-azbox
    git clone https://github.com/seife/meta-azbox.git meta-azbox

## Initialize the oe-core build environment
    # Initialize the oe-core build environment and edit configuration files 
    #
    # This following command line line will create your build directory, setup your build environment,
    # automatically place the current work directory inside the build dir and
    # print out some useful information on how to bitbake packages.
    # You can rerun this command every time you want to re-setup your build environment!

    source oe-init-build-env build-azbox

## Add meta-stlinux in bblayers.conf 
    vim conf/bblayers.conf
    ...
    BBLAYERS ?= " \
      /home/user/poky/meta \
      /home/user/poky/meta-yocto \
      /home/user/poky/meta-yocto-bsp \
      /home/user/poky/meta-azbox \
    "
    ...

## Set MACHINE to azboxme and package type to ipk in local.conf
    vim conf/local.conf
    ...
    # azboxme covers also azboxminime boxes
    MACHINE ??= "azboxme"
    ...
    PACKAGE_CLASSES ?= "package_ipk"
    ...


## Run bitbake:

    bitbake core-image-minimal


Notes
-----

Some of the azbox recipes have been adopted from or inspired by
https://github.com/oe-alliance/oe-alliance-core, especially the
meta-brands/meta-azbox subdirectory.
I have, however, combined the ``azboxme`` and ``azboxminime``
machines into the ``azboxme`` target, because they really are not
that different to warrant totally separate builds.
The methods to accomplish this have been mostly taken from
https://github.com/seife/historic-buildsystem/ (look there for
``make/flashimage.mk`` and ``make/linuxkernel.mk``)

Prerequisite
------------

TBD

Caution!
--------

TBD

License
-------

Since stuff was copied initially from oe-alliance-core which is GPLv2,
this meta layer is license GPLv2, too.

Layer maintainer: Stefan Seyfried seife at tuxbox-git.slipkontur.de
