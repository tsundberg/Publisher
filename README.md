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

###  `<include>`

Add the tag `<include>` to include source code

Three attributes are supported:

* root
* file
* fileDisplayName

An example is:

```xml
<include root="./blog/"
         file="src/main/files/RentStepdefs.java"
         fileDisplayName="src/test/java/se/waymark/rentit/steps/RentStepdefs.java"/>
```

This would search for the file `src/main/files/RentStepdefs.java` from the relative root `./blog/` The filename would be displayed as `src/test/java/se/waymark/rentit/steps/RentStepdefs.java`


## Feedback

Send me a mail at tsu@kth.se if you have any questions or suggestions.