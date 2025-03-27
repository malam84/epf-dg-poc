package com.rh.poc.db;

import java.io.IOException;
import java.io.InputStream;

import org.infinispan.protostream.DescriptorParserException;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.SerializationContextInitializer;

public class UserSchemaInitializer implements SerializationContextInitializer {

    @Override
    public String getProtoFileName() {
        return "proto/user.proto";  // Match the file location in resources
    }

    @Override
    public String getProtoFile() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(getProtoFileName())) {
            if (stream == null) {
                throw new IOException("Proto file not found: " + getProtoFileName());
            }
            return new String(stream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load proto file", e);
        }
    }

    @Override
    public void registerSchema(SerializationContext ctx) {
        try {
			ctx.registerProtoFiles(FileDescriptorSource.fromResources(getProtoFileName()));
		} catch (DescriptorParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void registerMarshallers(SerializationContext ctx) {
        ctx.registerMarshaller(new UserMarshaller());
    }
}