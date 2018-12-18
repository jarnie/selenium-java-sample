
#!/bin/bash
set -x
testSetup(){

export SAUCE_USER="name"
export SAUCE_KEY="key"

# local, sauce, bitbar
export PLATFORM_TO_RUN="local"

# linux, mac
export PLATFORM_OS="mac"

# firefox, chrome
export BROWSER_TO_TEST="firefox"

export BROWSER_SIZE_X="1920"
export BROWSER_SIZE_Y="1440"

TEST=${TEST:="techCrunchTest"}
#TEST=${TEST:="techCrunchSauceTest"}


}

executeTests(){

	mvn clean test -Dtest=${TEST}
    echo "Finished Running Tests!"
}

testSetup
executeTests
