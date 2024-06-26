package net.serg.resourceprocessor.client;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceServiceClient {

    byte[] getAudioById(Long id);

    InputStream getAudioStreamById(Long id) throws IOException, InterruptedException;
}
