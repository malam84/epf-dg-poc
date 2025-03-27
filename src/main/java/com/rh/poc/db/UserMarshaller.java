package com.rh.poc.db;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class UserMarshaller implements MessageMarshaller<User> {

    @Override
    public String getTypeName() {
        return "com.kwsp.poc.User";  // Match the package in the .proto file
    }

    @Override
    public Class<User> getJavaClass() {
        return User.class;
    }

    @Override
    public void writeTo(MessageMarshaller.ProtoStreamWriter writer, User user) throws IOException {
        writer.writeInt("id", user.getId());
        writer.writeString("email", user.getEmail());
        writer.writeString("name", user.getName());
        writer.writeInt("version", user.getVersion());
    }

    @Override
    public User readFrom(MessageMarshaller.ProtoStreamReader reader) throws IOException {
        int id = reader.readInt("id");
        String email = reader.readString("email");
        String name = reader.readString("name");
        int version = reader.readInt("version");
        return new User(id, email, name, version);
    }
}
