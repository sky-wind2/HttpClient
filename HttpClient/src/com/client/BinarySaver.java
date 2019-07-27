package com.client;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BinarySaver {
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			try {
				URL root = new URL(args[i]);
				URLConnection uc = root.openConnection();
		        for (int j = 1; ; j++) {
		            String header = uc.getHeaderField(j);
		            if (header == null) break;
		            System.out.println(uc.getHeaderFieldKey(j) + ": " + header);
		        }
				saveBinaryFile(root);
			} catch (MalformedURLException ex) {
			  System.err.println(args[i] + " is not URL I understand.");
			} catch (IOException ex) {
			  System.err.println(ex);
			}
		}
		System.out.println("success");
	}
	public static void saveBinaryFile(URL u) throws IOException {
		URLConnection uc = u.openConnection();
		int contentLength = uc.getContentLength();
		if (contentLength == -1) {
			throw new IOException("This is not a binary file.");
		}
		
		try (InputStream raw = uc.getInputStream()) {
			InputStream in = new BufferedInputStream(raw);
			byte[] data = new byte[contentLength];
			int offset = 0;
			while (offset < contentLength) {
				int bytesRead = in.read(data, offset, data.length - offset);
				if (bytesRead == -1) break;
		         offset += bytesRead;
		         }
						
			if (offset != contentLength) {
				throw new IOException("Only read " + offset 
			            + " bytes; Expected " + contentLength + " bytes");
			}
			String filename = "source";
			try (FileOutputStream fout = new FileOutputStream(filename)) {
				fout.write(data);
				fout.flush();
			}
		}
	}
}
