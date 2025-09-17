javac -d out src/*.java
jar cfm Snake.jar manifest.txt -C out . -C src images
java -jar Snake.jar