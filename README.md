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

**NOTE**: This requires ObjectWeb's ASM (v5.0.3), but this is _not_ included in `lib/` because it was linked from elsewhere. It'll be fixed at some point.
