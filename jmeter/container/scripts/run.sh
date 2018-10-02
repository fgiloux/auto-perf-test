#!/bin/bash

dir=${RESULT_SUB_DIR:-$(date +"%F_%T")}
# Parse JMeter parameters, replaces the J_ in J_PARAM with -J -> -JPARAM
for param in $(printenv | grep J_); do
    JMETER_PARAMS="$JMETER_PARAMS -J${param:2}"
done

# Runs tests from JMX file, creates results file and reports dashboard
for TEST_FILE in $JMETER_BASE/tests/*.jmx; do
    RESULT_FILE=/opt/jmeter/results/${dir}/$(basename $TEST_FILE .jmx).jtl
    $JMETER_HOME/bin/jmeter -n -t $TEST_FILE -l $RESULT_FILE $JMETER_PARAMS
    # Print results file
    cat $RESULT_FILE
done
# -e -o $2-reports


# Call back the webhook step in Jenkins pipeline
# The name of the test suite pod is provided
if [ -n "${CALLBACK_URL}" ]; then
    # Workaround for my not working DNS resolution
    CALLBACK_URL="http://jenkins/webhook-step/$(echo ${CALLBACK_URL} | sed 's/.*\///')"
    echo " curl -X POST -d ${HOSTNAME} ${CALLBACK_URL}"
    curl -s -X POST -d "${HOSTNAME}" "${CALLBACK_URL}"
fi

# An option would be to sleep for a few minutes to give Jenkins the chance to retrieve dashboard files through oc rsync
# if persistent volumes can't be used
# sleep 600
