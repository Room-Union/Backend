package org.codeit.roomunion.user.adapter.out.file;

import org.codeit.roomunion.user.application.port.out.NicknameGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class FileBasedNicknameGenerator implements NicknameGenerator {

    private final List<String> adjectives;
    private final List<String> animals;
    private final Random random = new Random();

    public FileBasedNicknameGenerator() throws IOException {
        this.adjectives = loadWords("nickname/adjectives.txt");
        this.animals = loadWords("nickname/animals.txt");
    }

    @Override
    public String generate() {
        String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String animal = animals.get(random.nextInt(animals.size()));
        String number = String.format("%05d", random.nextInt(100000));

        return adjective + animal + number;
    }

    private List<String> loadWords(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        List<String> words = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        return Collections.unmodifiableList(words);
    }
}