#!/bin/bash

chmod +x gradlew;
./gradlew jar;
java -jar build/libs/The\ Game\ Of\ Life-*.jar;

exit 0