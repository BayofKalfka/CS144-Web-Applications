#!/bin/bash
GRADING_DIR=$HOME/grading
TMP_DIR=/tmp/p4-grading/
REQUIRED_FILES="team.txt build.xml README.txt"
APPNAME=$CATALINA_BASE/webapps/eBay

# usage
if [ $# -ne 1 ]
then
     echo "Usage: $0 project4.zip" 1>&2
     exit 1
fi

# make sure that the script runs on VM
if [ `hostname` != "class-vm" ]; then
     echo "ERROR: You need to run this script within the class virtual machine" 1>&2
     exit 1
fi

ZIP_FILE=$1

# clean any existing files
rm -rf ${TMP_DIR}

# create temporary directory used for grading
mkdir ${TMP_DIR}

# unzip the zip file
if [ ! -f ${ZIP_FILE} ]; then
    echo "ERROR: Cannot find $ZIP_FILE" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi
unzip -q -d ${TMP_DIR} ${ZIP_FILE}
if [ "$?" -ne "0" ]; then 
    echo "ERROR: Cannot unzip ${ZIP_FILE} to ${TMP_DIR}"
    rm -rf ${TMP_DIR}
    exit 1
fi

# change directory to the partc folder
cd ${TMP_DIR}

# check the existence of the required files
for FILE in ${REQUIRED_FILES}
do
    if [ ! -f ${FILE} ]; then
    echo "ERROR: Cannot find ${FILE} in the root folder of ${ZIP_FILE}" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
    fi
done
JAVA_FILES=`find src -name '*.java' -print`
if [ -z "${JAVA_FILES}" ]; then
    echo "ERROR: No java file is included in src folder of ${ZIP_FILE}" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi

echo "Running 'ant build' to build your war file..."
ant build

echo "Removing existing eBay application files on Tomcat..."
rm -rf ${APPNAME}*

echo "Deploying your eBay application..."
ant deploy

# check if the .war file has been built
if [ ! -f ${APPNAME}.war ]; then
    echo "ERROR: Cannot find your WAR file on Tomcat after 'ant deploy'" 1>&2
    rm -rf ${TMP_DIR}
    exit 1
fi

# clean up
rm -rf ${TMP_DIR}

echo
echo
echo "Now your Tomcat server is running with your application."
echo "Please access your application through your browser."
echo "Make sure that all application functionalities are working fine."
exit 0
