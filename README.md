# Publisher

A Maven plugin that reads a html file and includes source code into it. The source code is html encoded and is possible
to display in a html page with all characters correctly preserved. The result is

## Usage

Add the following to your build section in your pom:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>se.somath.publisher</groupId>
            <artifactId>maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <executions>
                <execution>
                    <goals>
                        <goal>publish</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
Write your text in the file

    ./src/main/resources/index.html

Execute

    mvn generate-resources

(or any maven goal that will include generate-resources)

The result is written to

    ./target/index.html

###  `<include-source-code>`

Add the tag `<include-source-code>` to include source code

Three attributes are supported:

* root - the root directory that contains the files
* file - the relative name from the root
* fileDisplayName - an alternative file name to display
* displayFileName - a boolean to turn off displaying the file name

An example is:

```xml
<include-source-code root="./blog/"
         file="src/main/files/RentStepdefs.java"
         fileDisplayName="src/test/java/se/waymark/rentit/steps/RentStepdefs.java"/>
```

This would search for the file `src/main/files/RentStepdefs.java` from the relative root `./blog/` The filename would be displayed as `src/test/java/se/waymark/rentit/steps/RentStepdefs.java`

###  `<include-file-tree>`

Add the tag `<include-file-tree>` to include a file tree

One attributes is supported:

* root - the root directory that contains the file tree

An example is:

```xml
<include-file-tree root="./blog/"/>
```

This would include a file tree with all files available in the root.


## Feedback

Send me a mail at tsu@kth.se if you have any questions or suggestions.