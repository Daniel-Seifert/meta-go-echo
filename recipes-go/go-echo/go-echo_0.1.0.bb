SUMMARY = "Go-Echo"
DESCRIPTION = "Simple echo client written in Go in version 0.1.0."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"


FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI = "file://go-echo-linux-amd64 \
           file://go-echo-linux-arm64 \
           file://go-echo-linux-arm \
           file://go-echo.service"

S = "${WORKDIR}"

inherit systemd distro_features_check
require go-echo-bin-arch.inc

do_install() {
    # Install binary
    install -d ${D}${bindir}
    install -m 755 ${S}/go-echo-linux-${GO_ECHO_PKG_ARCH} ${D}${bindir}/go-echo

    # Copy systemd files
    install -Dm0644 ${S}/go-echo.service ${D}${systemd_unitdir}/system/go-echo.service

    # Expand paths
    sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
        -e 's,@BINDIR@,${bindir},g' \
        -e 's,@SBINDIR@,${sbindir},g' \
        ${D}${systemd_unitdir}/system/go-echo.service
}

FILES_${PN} += " \
    ${bindir}/go-echo \
    ${systemd_unitdir}/system/go-echo.service \
"

SYSTEMD_SERVICE_${PN} = "go-echo.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"
