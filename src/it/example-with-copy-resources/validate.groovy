File outputDir = new File(basedir, "target/generated-docs");

String[] expectedFiles = ["index.html"]

for (String expectedFile : expectedFiles) {
    File file = new File(outputDir, expectedFile);
    println("Check for existence of " + file)
    if ( !file.isFile()) {
        throw new Exception("Missing file : '" + file + "'");
    }
}

// check if images directory exist and contains one file
def imagesDirectory = new File(outputDir, "images")

if (!imagesDirectory.exists()) {
    throw new Exception("Images directory doesn't exist")
} else {
    def files = imagesDirectory.listFiles()
    assert 1, files.length
    assert files[0].name.endsWith(".png")
}


return true;