################################################################
# A minimal CMake file that is compatible with sbt-jni         #
#                                                              #
# All settings required by sbt-jni have been marked so, please #
# add/modify/remove settings to build your specific library.   #
################################################################

cmake_minimum_required(VERSION 3.5.1)

# Define project and related variables
# (required by sbt-jni) please use semantic versioning
#
project (deezerplayer)
set(PROJECT_VERSION_MAJOR 0)
set(PROJECT_VERSION_MINOR 0)
set(PROJECT_VERSION_PATCH 0)


# Required dependencies

find_package(JNI)
if (JNI_FOUND)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
else()
    message (FATAL_ERROR "JNI not found. Have you set JAVA_HOME environment variable to the correct path?")
endif()

if(NOT DEFINED ARCH_TARGET)
    message(STATUS "Targeted architecture not set, getting value from host")
    string(TOLOWER "${CMAKE_SYSTEM_PROCESSOR}-${CMAKE_SYSTEM_NAME}" HOST_ARCH)
    set(ARCH_TARGET ${HOST_ARCH})
endif()

message(STATUS "Compiling for ${ARCH_TARGET}")

find_library(DEEZER_LIB deezer ../resources/native/${ARCH_TARGET}/)
if (DEEZER_LIB)
    message (STATUS "DEEZER_LIB=${DEEZER_LIB}")
else()
    message (FATAL_ERROR "Deezer library not found!")
endif()

set (CMAKE_CXX_STANDARD 11)

# Include directories
include_directories(.)
include_directories(${JNI_INCLUDE_DIRS})
include_directories(include)
include_directories(include/deezer)



# Sources
file(GLOB LIB_SRC
  "*.c"
  "*.cc"
  "*.cpp"
)

set(DEEZERPLAYER_SRC
    include/jni/JNIUtils.cpp
    include/jni/callback/ScalaConnectCallback.cpp
    include/jni/callback/ScalaPlayerCallback.cpp
    include/jni/info_acidflow_homer_modules_music_deezer_jni_DeezerPlayerNative__.cpp
    include/player/DeezerPlayer.cpp
    include/player/callback/DeezerPlayerCallback.cpp
    include/player/callback/DeezerConnectCallback.cpp
)

# Setup installation targets
# (required by sbt-jni) major version should always be appended to library name
#
set (LIB_NAME ${PROJECT_NAME}${PROJECT_VERSION_MAJOR})
add_library(${LIB_NAME} SHARED ${DEEZERPLAYER_SRC})
target_link_libraries(${LIB_NAME} ${DEEZER_LIB})

install(TARGETS ${LIB_NAME} LIBRARY DESTINATION .)

