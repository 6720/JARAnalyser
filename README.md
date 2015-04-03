# JARAnalyser

A little tool for analysing JAR files.

--------

This does:

 * Show bytecode
 * Show default field values
 * Do basic syntax highlighting
 * Find method invocations
 * Find field accesses
 * Show a few other statistics

This does not:

 * Fully decompile (That's why we have JD-GUI, procyon, FernFlower, ...)
 * Find class/type usages
 * Do full syntax highlighting

Analysis of class files (for method invocation, field access, etc.) is once, at the initial launch. Note that this analysis may take some time.

The JAR will be extracted to a temporary directory (which is removed on exit) in order to make building the structure for the JTree in the GUI easier.
