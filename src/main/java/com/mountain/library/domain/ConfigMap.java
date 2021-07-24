package com.mountain.library.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ConfigMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigMap.class);
    private AtomicBoolean reload = new AtomicBoolean(true);
    private Map<String, Map<String, String>> map = new HashMap();

    public ConfigMap() {
    }

    public void setReload(boolean reload) {
        this.reload.set(reload);
    }

    public boolean isReload() {
        return this.reload.get();
    }

    public Map<String, Map<String, String>> getMap() {
        return this.map;
    }

    public String getAppName() {
        return this.get(ConfigGroup.APP.name(), "app.name");
    }

    public void put(String kategori, String key, String val) {
        Map<String, String> configsInKategori = (Map)this.map.get(kategori);
        if (configsInKategori == null) {
            configsInKategori = new HashMap();
            this.map.put(kategori, configsInKategori);
        }

        ((Map)configsInKategori).put(key, val);
    }

    public String get(String kategori, String key) {
        Map<String, String> configsInKategori = (Map)this.map.get(kategori);
        return configsInKategori == null ? "" : (String)configsInKategori.get(key);
    }

    public void clear() {
        if (this.map != null) {
            this.map.clear();
        }

    }

    public void print() {
        int size = this.map.size();
        if (size == 0) {
            LOGGER.info("==== NO DATA  ====");
        }

        Set<String> keySet = this.map.keySet();
        List<String> listKeysSet = new ArrayList(keySet);
        Collections.sort(listKeysSet, Comparator.naturalOrder());
        Iterator var4 = listKeysSet.iterator();

        while(true) {
            String key;
            do {
                if (!var4.hasNext()) {
                    return;
                }

                key = (String)var4.next();
            } while(key.equals(ConfigGroup.DEFAULT.name()));

            LOGGER.info("==== " + key + "====");
            Map<String, String> daftar = (Map)this.map.get(key);
            Set<String> keys = daftar.keySet();
            List<String> listKeys = new ArrayList(keys);
            Collections.sort(listKeys, Comparator.naturalOrder());
            Iterator var9 = listKeys.iterator();

            while(var9.hasNext()) {
                String k = (String)var9.next();
                LOGGER.info(k + " : " + (String)daftar.get(k));
            }
        }
    }

    public String toString() {
        return "ConfigMap{reload=" + this.reload + "}";
    }
}
