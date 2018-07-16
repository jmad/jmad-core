#!/bin/bash

MADX_URL_BASE=http://madx.web.cern.ch/madx/releases/last-rel

for os in {linux,osx,win}; do
    ospath="$os"
    compiler="gnu"
    if [ "$os" == 'osx' ]; then
        ospath='macosx'
    fi
    if [ "$os" == 'linux' ]; then
        compiler="intel" # numerical discrepancies for ... gnu
    fi
    rm "src/java/cern/accsoft/steering/jmad/bin/$os/$destbin"
    wget "$MADX_URL_BASE/madx-${ospath}64-$compiler" -O "src/java/cern/accsoft/steering/jmad/bin/$os/madx64"
done
