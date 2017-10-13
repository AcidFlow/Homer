#ifndef LOGGER_HPP
#define LOGGER_HPP

#include <cstdlib>
#include <cstdio>
#include <cstring>

#define log_i(fmt, ...) fprintf(stdout, "[%s:%d]" fmt, __FILE__, __LINE__, ##__VA_ARGS__); fflush(stdout);
#define log_e(fmt, ...) fprintf(stderr, "[%s:%d]" fmt, __FILE__, __LINE__, ##__VA_ARGS__); fflush(stderr);

#endif