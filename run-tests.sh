
#!/bin/bash
set -x
testSetup(){

export SAUCE_USER=""
export SAUCE_KEY=""

export BROW_STACK_USER=""
export BROW_STACK_KEY=""

# local, sauce, browserstack, bitbar (not done)
export PLATFORM_TO_RUN="local"

# 'local' mac and linux
# 'sauce' mac, linux, windows
# 'browserstack' mac and windows

# linux, mac, windows
export PLATFORM_OS="mac"

# firefox, chrome
export BROWSER_TO_TEST="chrome"

export BROWSER_SIZE_X="1920"
export BROWSER_SIZE_Y="1080"

# '$PLATFORM_TO_RUN' variable
# 'local' runs tests from 'techCrunchTest' class
# 'sauce' runs tests from 'techCrunchSauceTest' class
# 'browserstack' runs tests from 'techCrunchBrowserStackTest' class

if [ "$PLATFORM_TO_RUN" = "local"  ]
then
    TEST=${TEST:="techCrunchTest"}
elif [ "$PLATFORM_TO_RUN" = "browserstack"  ]
then
    TEST=${TEST:="techCrunchBrowserStackTest"}
elif [ "$PLATFORM_TO_RUN" = "sauce"  ]
then
    TEST=${TEST:="techCrunchSauceTest"}
else
echo "PLZ set 'PLATFORM_TO_RUN' variable!!!!!"
echo ${PLATFORM_TO_RUN}
exit 0
fi

}

executeTests(){

	mvn clean test -Dtest=${TEST}
    echo "Finished Running Tests!"
}

testSetup
executeTests
