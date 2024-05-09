package org.example.s28299tpo7.service;

import org.example.s28299tpo7.model.CodeSnippet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class CodeSnippetService {
    private static final String STORAGE_DIR = "snippets";
    public CodeSnippetService(){
        try{
            Files.createDirectories(Paths.get(STORAGE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCodeSnippet(CodeSnippet snippet) throws IOException{
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                STORAGE_DIR + File.separator + snippet.getId()))){
            out.writeObject(snippet);
        }
    }

    public CodeSnippet loadCodeSnippet(String id) throws IOException, ClassNotFoundException{
        File file = new File(STORAGE_DIR + File.separator + id);
        if(!file.exists()){
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (CodeSnippet) in.readObject();
        }
    }

    public void deleteCodeSnippet(String id) {
        Path path = Paths.get(STORAGE_DIR, id);
        try {
            Files.delete(path);
            System.out.println("Successfully deleted: " + id);
        } catch (Exception e) {
            System.err.println("Failed to delete code snippet with ID: " + id + ", reason: " + e.getMessage());
        }
    }

    @Scheduled(fixedRate = 5000)
    public void removeExpiredSnippets(){
        File directory = new File(STORAGE_DIR);
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (isExpiredSnippet(file)) {
                deleteCodeSnippet(file.getName());
            }
        }
    }

    private boolean isExpiredSnippet(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            CodeSnippet snippet = (CodeSnippet) in.readObject();
            boolean expired = snippet.isExpired();
            return expired;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error checking if snippet is expired: " + e.getMessage());
            return false;
        }
    }
}
