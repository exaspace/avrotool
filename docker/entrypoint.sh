#!/bin/sh
# Use this script just to exec avrotool as for some reason,
# if the avrotool script is the entrypoint directly arguments are not passed through correctly.
exec /avrotool/target/pack/bin/avrotool "$@"
