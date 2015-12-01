#!/bin/bash
pushd generated-site
python -m SimpleHTTPServer 8000 generated-site
popd
