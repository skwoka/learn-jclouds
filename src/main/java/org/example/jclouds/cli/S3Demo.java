package org.example.jclouds.cli;

public class S3Demo extends DemoBase {

	public S3Demo(String provider, String authUser, String authKey) {
		super(provider, authUser, authKey);
	}

	public static void main(String[] args) throws Exception {
		String user = args[0];
		String key = args[1];
		String provider = "aws-s3";
		S3Demo demo = new S3Demo(provider, user, key);

		try {
			demo.run();
		} finally {
			demo.cleanup();
		}
	}
}
