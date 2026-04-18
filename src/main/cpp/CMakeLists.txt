cmake_minimum_required(VERSION 3.20)
project(lagkill_native LANGUAGES CXX)

add_library(lagkill_native SHARED lagkill_native.cpp)
set_target_properties(lagkill_native PROPERTIES CXX_STANDARD 17 CXX_STANDARD_REQUIRED YES)
