# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java Maven project implementing a simple shopping cart system. The project uses Java 11 and JUnit 5 for testing.

**Package Structure:**
- `com.example.cart` - Main package containing the Cart implementation

## Build System

This project uses **Maven** as the build tool.

### Key Commands

**Compile the project:**
```bash
mvn compile
```

**Run all tests:**
```bash
mvn test
```

**Run a specific test class:**
```bash
mvn test -Dtest=CartTest
```

**Run a specific test method:**
```bash
mvn test -Dtest=CartTest#testAddNewProduct
```

**Clean and rebuild:**
```bash
mvn clean install
```

**Package the project:**
```bash
mvn package
```

## Architecture

### Cart System

The cart implementation (`src/main/java/example/cart/Cart.java`) is a simple in-memory collection that:
- Stores items as strings in an ArrayList
- Prevents duplicate items from being added
- Validates product inputs (rejects null or empty strings)
- Returns boolean success indicators for add/remove operations

**Note:** The main class is in package `com.example.cart` but the file is located at `src/main/java/example/cart/Cart.java` - there's a mismatch between the package declaration and directory structure.

## Testing

Tests are written using JUnit 5 and located in `src/test/java/example/cart/CartTest.java`. The test suite covers:
- Adding new products
- Preventing duplicate additions
- Input validation
- Product removal
- Handling nonexistent products

## Development Notes

- Java version: 11
- JUnit version: 5.10.0
- Maven Surefire Plugin version: 3.1.2
- The `target/` directory contains compiled classes and is not tracked in git
