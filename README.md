INSTALL
=======

1. mvn clean install
2. mvn eclipse:eclipse

RUNNING
=======

Note: examples can only be run from eclipse at this time.

1. Copy S3Demo.launch.template to S3Demo.launch. Do the same for CloudFiles.launch.template.
2. Edit S3Demo.launch and supply AWS API credentials.
3. Edit CloudFiles.launch and supply Rackspace API credentials.
4. Import launch configurations in eclipse
5. Launch demo with "Run As..."

If the demo complains about not being able to find sample file, run mvn install again to 
move sample file to the target directory.
