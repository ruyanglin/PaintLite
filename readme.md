
# Paint Lite
Simple Java based paint application that allows users to draw free flowing lines, shapes and colours. Standard Cut/Copy/Paste operations are available.

Upon saving, the objects on the canvas are serialized into the file.

## Requirements
1. JAVA 11 
2. JAVA FX

## How to Run
```sh
make run
```

Compiles and executes the Java classes

```shell script
make clean
```
Cleans up the Java class files

> Users may need to change the location of JAVA_FX_HOME inside the makefile to point it to the install location of 
>JavaFX


## Implementation

To use cut-copy-paste, 

1) Select a shape on the canvas
2) Go to Edit-(Copy/Cut)
3) Click somewhere on the canvas with no other shape
4) Go to Edit-Paste, the shape will be cut/copied over

Note: The functionality doesn't store the Cut/Copied shape indefinitely, 
which means that users will need to Copy/Cut the shape again to paste over and over