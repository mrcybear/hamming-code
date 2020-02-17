# Hamming code

A simple Java implementation to calculate Hamming code for data words of 64 bits.

Hamming code is a set of error-correction code that can be used to detect and correct bit errors that can occur when computer data is moved or stored.

# Implementation notes

This implementation can detect and fix a single bit error only in a data word.
Double bit errors detection is not implemented for simplicity.
A couple of JUnit tests are provided to demonstrate Hamming code effectiveness.

# Requirements

Built successfully with Java 11. Requires JUnit 4.13.
