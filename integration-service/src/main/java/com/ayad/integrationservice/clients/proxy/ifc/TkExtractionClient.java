package com.ayad.integrationservice.clients.proxy.ifc;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface TkExtractionClient {

    CompletableFuture<String> callTKExtractionService(String filePath);
}
