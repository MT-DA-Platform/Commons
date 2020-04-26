package com.MTDap.commons.db.elasticsearch.queryhelper;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.MTDap.commons.constants.Constants.FILE_SEPARATOR;
import static com.MTDap.commons.constants.Constants.UTF_8_ENCODING;

public class QueryLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryLoader.class);
    private static final List<String> FILE_EXTENSIONS = new ArrayList<>();
    private static QueryLoader instance = new QueryLoader();

    static {
        FILE_EXTENSIONS.add("sql");
        FILE_EXTENSIONS.add("json");
    }

    private String QUERIES_FOLDER = "." + FILE_SEPARATOR + "config" + FILE_SEPARATOR + "queries";
    private Map<String, String> queries;

    private QueryLoader() {
        try {
            load();

        } catch (IOException e) {
            LOGGER.error("Error in loading queries", e);
        }
    }

    public static QueryLoader getInstance() {
        return instance;
    }

    private void load() throws IOException {
        File apps = new File(QUERIES_FOLDER);
        queries = new HashMap<>();
        for (File app : apps.listFiles()) {
            readFile(app);
        }
    }

    private void readFile(File file) throws IOException {
        String fileExt = FilenameUtils.getExtension(file.getName());
        if (!FILE_EXTENSIONS.contains(fileExt)) {
            return;
        }

        String name = FilenameUtils.getBaseName(file.getName());
        try (InputStream in = new FileInputStream(file)) {
            queries.put(name, IOUtils.toString(in, UTF_8_ENCODING));
        }
    }

    public String getNamedPreparedStatement(String queryName) {
        return getNamedPreparedStatement(queryName, new HashMap<>());
    }

    public String getNamedPreparedStatement(String queryName, Map<String, Object> params) {
        String query = queries.get(queryName);

        try {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query = query.replace("{" + param.getKey() + "}", param.getValue().toString());
            }
        } catch (NullPointerException e) {
            LOGGER.error(queryName + " not found");
        }

        return query;
    }
}
