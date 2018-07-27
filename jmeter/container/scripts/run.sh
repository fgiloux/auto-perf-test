#!/bin/bash

# Parse JMeter parameters, replaces the J_ in J_PARAM with -J -> -JPARAM
for param in $(printenv | grep J_); do
    JMETER_PARAMS="$JMETER_PARAMS -J${param:2}"
done

# Runs tests from JMX file, creates results file and reports dashboard
for TEST_FILE in $JMETER_BASE/tests/*.jmx; do
    RESULT_FILE=$JMETER_BASE/results/$(basename $TEST_FILE .jmx)_$(date +"%F_%T").jtl
    $JMETER_HOME/bin/jmeter -n -t $TEST_FILE -l $RESULT_FILE $JMETER_PARAMS
    # Print results file
    cat $RESULT_FILE
done
# -e -o $2-reports


# Curl back to webhook step in Jenkins pipeline
# Give pipeline the name of the test suite pod
# curl -X POST -d "$HOSTNAME" $1

# Sleep for a few minutes to give pipeline time to retrieve dashboard files
# sleep 600
