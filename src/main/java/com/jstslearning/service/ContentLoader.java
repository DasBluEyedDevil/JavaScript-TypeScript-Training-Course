package com.jstslearning.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jstslearning.model.Module;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads course content from embedded JSON resources
 */
public class ContentLoader {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Module> modules = new ArrayList<>();

    /**
     * Load all modules from the resources/content directory
     * Attempts to load modules 1-14 (full curriculum)
     */
    public void loadAllModules() {
        // Load all available modules (1-14)
        for (int i = 1; i <= 14; i++) {
            try {
                Module module = loadModule(i);
                if (module != null) {
                    modules.add(module);
                    System.out.println("Loaded Module " + i + ": " + module.getTitle());
                }
            } catch (Exception e) {
                // Non-fatal: module may not exist yet
                System.err.println("Warning: Could not load module " + i + ": " + e.getMessage());
            }
        }

        if (modules.isEmpty()) {
            System.err.println("ERROR: No modules could be loaded!");
        } else {
            System.out.println("Successfully loaded " + modules.size() + " modules");
        }
    }

    /**
     * Load a specific module from JSON
     */
    private Module loadModule(int moduleNumber) {
        String resourcePath = "/content/module" + moduleNumber + ".json";
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Resource not found: " + resourcePath);
                return null;
            }
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            return gson.fromJson(reader, Module.class);
        } catch (Exception e) {
            System.err.println("Error loading module " + moduleNumber + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all loaded modules
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Find a module by ID
     */
    public Module getModuleById(int id) {
        return modules.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
