#!/bin/bash

# Runs tests from JMX file, creates results file and reports dashboard
# I need to build a loop!!!
for TEST_FILE in $JMETER_BASE/tests/*.jmx
    RESULT_FILE = $JMETER_BASE/results/$(basename $TEST_FILE .jmx).jtl
    $JMETER_HOME/bin/jmeter -n -t $TEST_FILE -l $RESULT_FILE
    # Print results file
    cat $RESULT_FILE
done
# -e -o $2-reports


# Curl back to webhook step in Jenkins pipeline
# Give pipeline the name of the test suite pod
# curl -X POST -d "$HOSTNAME" $1

# Sleep for a few minutes to give pipeline time to retrieve dashboard files
# sleep 600
